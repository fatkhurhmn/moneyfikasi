# Coding Patterns — Moneyfikasi

> Every pattern below was read from actual source code; each item names the file to read before copying it. Project-wide rules (commands, secrets, release flow) live in `AGENTS.md` and are not repeated here.

## 1. Compose Screen — stateless, state + callbacks

**Purpose.** Screens render state and forward user actions. They never hold a ViewModel or `NavController`.

```kotlin
// feature/budget/.../add_edit/AddEditBudgetScreen.kt
@Composable
fun AddEditBudgetScreen(
    state: AddEditBudgetState,
    eventFlow: SharedFlow<AddEditBudgetViewModel.UiEvent>,
    onAmountChange: (String) -> Unit,
    // ...
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { CommonTopAppBar(/* ... */) },
        // ...
    ) { /* ... */ }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditBudgetViewModel.UiEvent.SaveBudget -> onBackClick()
                is AddEditBudgetViewModel.UiEvent.ShowMessage ->
                    snackbarHostState.showMessage(/* ... */)
            }
        }
    }
}
```

- Reference: `feature/budget/src/main/java/dev/muffar/moneyfikasi/budget/add_edit/AddEditBudgetScreen.kt`, `feature/transaction/src/main/java/dev/muffar/moneyfikasi/transaction/transfer/TransferTransactionScreen.kt`, `feature/settings/src/main/java/dev/muffar/moneyfikasi/settings/SettingsScreen.kt`
- Signature order: `state` first, then `eventFlow` (only screens with one-shot events — 13 verified), then one `onXxx` lambda per action. Navigation callbacks (`onBackClick`, `onAddWalletClick`) arrive as lambdas from `navigation/`.
- Sub-UI goes to the sibling `component/` folder; cross-feature widgets come from `shared/common-ui/.../component/`. Strings come from `resource.R`, never hardcoded.
- Naming: `XxxScreen`; callbacks `onNounVerb` (`onAmountChange`, `onBudgetClick`, `onBackClick`); sub-components `XxxSection`/`XxxForm`/`XxxItem`/`XxxTopBar`.
- Rules: keep the Screen free of business logic; navigation-after-save and snackbars go through `eventFlow`, not direct calls; show snackbars with the `showMessage(msg, type)` helper from `shared/common-ui/.../component/message/SnackbarMessage.kt`.
- Avoid: calling `viewModel`/`NavController` inside a Screen; raw `showSnackbar` (still present in `TransferTransactionScreen.kt`, do not copy); putting snackbar text or destinations in state.
- Variations: list screens without one-shot events take no `eventFlow` (e.g. `TransactionsScreen`); `modifier: Modifier = Modifier` exists on some Screens only — include it on new ones; `feature/splash/.../SplashScreen.kt` is the only static screen with no ViewModel/State at all.

## 2. ViewModel — MVI with State, Event, one-shot UiEvent

**Purpose.** Own UI state as a `StateFlow`, translate UI intents via `onEvent`, and emit fire-and-forget occurrences (snackbar, navigate-back) via `eventFlow`.

```kotlin
// feature/transaction/.../transfer/TransferTransactionViewModel.kt
@HiltViewModel
class TransferTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    // ...
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(TransferTransactionState())
    val state = _state.asStateFlow()
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: TransferTransactionEvent) {
        when (event) {
            is TransferTransactionEvent.AmountChanged -> onAmountChange(event.amount)
            // ...
        }
    }
    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveTransaction : UiEvent()
    }
}
```

- Reference (interactive): `feature/transaction/.../transfer/TransferTransactionViewModel.kt` with `TransactionsState.kt`/`TransactionsEvent.kt` in `.../transaction/list/`; (paging): `TransactionsViewModel.kt` uses `flatMapLatest { ...getAllTransactionsPaged(...) }.cachedIn(viewModelScope)`; (simple): `feature/category/.../list/CategoriesViewModel.kt`; (combine): `feature/more/.../MoreViewModel.kt` merges 5 Flows with `combine`
- Dependencies: `@HiltViewModel` + `@Inject` constructor taking `*UseCases` aggregates ONLY — verified with zero exceptions (including `BackupRestoreViewModel.kt`, standardized afterwards). Single-shot reads use `viewModelScope.launch { ...collectLatest { _state.update { it.copy(...) } } }`. Navigation args come from `SavedStateHandle` with keys from `Screen` (e.g. `handle.get<String>(Screen.TransferTransaction.AMOUNT)`); 11 ViewModels use it, all add-edit/detail/statistic.
- Naming: `XxxViewModel`; `XxxState` (data class, every field defaulted); `XxxEvent` (sealed, `XxxChanged`/`XxxSelected`/`SaveXxx`); private handlers `onXxx`/`loadXxx`; nested `sealed class UiEvent` with `ShowMessage` + past-tense outcomes (`SaveTransaction`, `DeleteBudget`).
- Rules: errors are NOT state — emit `UiEvent.ShowMessage(msg/resId, SnackbarType)`; per-field form errors stay in State as nullable `xxxError`; `isLoading` defaults `false` in State. Read-only list screens (7 verified: Categories, Presets, More, Wallets, CategoryDistribution, StatisticDetail, Budgets list) legitimately have no `onEvent` — do not force one.
- Avoid: injecting repositories, helpers, or `Context` into ViewModels; `mutableStateOf` for screen state (StateFlow everywhere); putting navigation calls in the ViewModel.

## 3. Domain — single use cases, aggregates, repository contracts

**Purpose.** One thin operation per class; aggregates bundle what a ViewModel needs; interfaces define what data must provide.

```kotlin
// shared/domain/.../usecase/wallet/UpsertWallet.kt
class UpsertWallet(private val repository: WalletRepository) {
    suspend operator fun invoke(wallet: Wallet) {
        repository.upsertWallet(wallet)
    }
}

// shared/domain/.../usecase/wallet/WalletUseCases.kt
data class WalletUseCases(
    val getAllWallets: GetAllWallets,
    val getWalletById: GetWalletById,
    val upsertWallet: UpsertWallet,
    val deleteWallet: DeleteWallet,
)
```

- Reference: `shared/domain/src/main/java/dev/muffar/moneyfikasi/domain/usecase/wallet/`, `.../usecase/transaction/GetAllTransactionsPaged.kt` (maps `Set<Category>/Set<Wallet>` → `Set<UUID>` with `ifEmpty { null }` = no filter), `.../repository/WalletRepository.kt`, `.../repository/TransactionRepository.kt`
- Structure: plain classes (no annotations), exactly one `operator fun invoke` each (verified: every use case has one) — `suspend` for writes/get-by-id, `Flow` for observations. 12 groups under `usecase/`; 14 repository interfaces under `repository/`. Repository filters are nullable `Set<UUID>?`; dates cross layers as epoch-millis `Long`.
- Rules: new operation = new class + field in the aggregate + construction in `UseCaseModule.kt` (`shared/domain/.../di/UseCaseModule.kt`, one `@Provides` per aggregate). ID-mapping and empty-means-all logic belongs in the use case, not the ViewModel.
- Avoid: calling repositories from ViewModels; fat use cases (orchestration across repositories is rare — only `ProcessRecurringTransactions` and `DeleteLatestBackup` take two repos, both verified in `UseCaseModule.kt`).
- Variations: `SaveTransactionImage()` takes no repository (pure infra helper); `preferences` splits into `ui`/`security`/`backup` subgroups; `DriveSignInResult` (`.../model/DriveSignInResult.kt`, sealed interface carrying a `PendingIntent?`) shows data-layer results must be mapped to domain types before crossing.

## 4. Data — impls, Room, mappers, transactions

**Purpose.** Implement domain contracts with Room/DataStore/remote, hiding all Android/infra details behind the interface.

```kotlin
// shared/data/.../repository/TransactionRepositoryImpl.kt
override fun getAllTransactionsPaged(/* ... */): Flow<PagingData<Transaction>> {
    return Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20,
            enablePlaceholders = false, prefetchDistance = 5),
        pagingSourceFactory = { transactionDao.getAllTransactionsPaged(/* ... */) }
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
}
```

```kotlin
// shared/data/.../db/dao/TransactionDao.kt
@Transaction
open suspend fun performTransfer(/* ... */) {
    // TRANSFER_OUT (source, balance-amount) + TRANSFER_IN (target, balance+amount)
    // sharing one transactionReference, plus an EXPENSE fee row if fee > 0
}
```

- Reference: `shared/data/src/main/java/dev/muffar/moneyfikasi/data/repository/{TransactionRepositoryImpl.kt,WalletRepositoryImpl.kt}`, `.../db/dao/{WalletDao.kt,TransactionDao.kt}`, `.../db/entity/WalletEntity.kt`, `.../db/MoneyfikasiDatabase.kt` (v1, 6 entities, `exportSchema = true`), `.../mapper/{Mapper.kt (12 fns),BudgetMapper.kt,AiErrorMapper.kt}`
- Structure: impls are `@Inject` classes bound `@Provides @Singleton` in `.../di/RepositoryModule.kt` (`DriveBackupRepository` in `.../di/DriveModule.kt`; DB + 6 DAOs in `.../di/RoomDatabaseModule.kt`; 4 DataStores with `ReplaceFileCorruptionHandler` in `.../di/DataStoreModule.kt`; Groq Retrofit client in `.../di/AiModule.kt`). Entities use plural snake_case tables (`wallets`), snake_case columns, `UUID` PKs. Mappers are top-level `XxxEntity.toDomain()` / `Xxx.toEntity()`. Multi-row writes are `@Transaction` in the DAO (Transaction 13×, Budget/Preset/Recurring 2×, Category/Wallet 0×); balance/relation updates happen in the DAO, never the impl.
- Rules: never leak entities above the impl (always `.toDomain()`); new preferences = serializer in `data/preferences/serializer/` + `@Provides` DataStore + repository + use cases; keep schema export on and commit the JSON under `shared/data/schemas/.../`.
- Avoid: transaction logic in impls/ViewModels; queries returning entities; new abstraction layers for scheduling (WorkManager scheduling lives in `BackupRestoreRepositoryImpl`, following the existing repository-hides-infra pattern).
- Verified quirk: impls carry redundant `@Inject` constructors AND manual `@Provides` construction (Hilt uses the latter) — harmless, do not "clean up" one side.

## 5. Hilt — what is bound where

**Purpose.** Single lookup table so AI edits the right module.

- `shared/domain/.../di/UseCaseModule.kt` — every `*UseCases` aggregate, manual construction, `SingletonComponent`.
- `shared/data/.../di/RepositoryModule.kt` — 13 repository bindings (context-requiring impls like `CategoryRepositoryImpl` receive `@ApplicationContext` here).
- `shared/data/.../di/DriveModule.kt` — `DriveBackupRepository` + `CredentialManager` + `AuthorizationClient`.
- `shared/data/.../di/RoomDatabaseModule.kt` — `MoneyfikasiDatabase.create(context)` + 6 DAOs, all `@Singleton`.
- `shared/data/.../di/DataStoreModule.kt` — 4 typed DataStores (`ui/backup/notification/security_settings.json`).
- `shared/data/.../di/AiModule.kt` — Groq Retrofit service (base `https://api.groq.com/`, kotlinx-serialization, `ignoreUnknownKeys = true`).
- `app/src/main/java/dev/muffar/moneyfikasi/MoneyfikasiApplication.kt` — `@HiltAndroidApp`, `HiltWorkerFactory` for WorkManager, `AndroidThreeTen.init`, applies theme from settings, schedules recurring transactions on launch.
- Drive collaborators (`DriveAuthHelper`, `DriveBackupDataSource` in `.../remote/drive/`) are `@Singleton @Inject` with no `@Provides` — that is the normal case, not a gap.
- Avoid: `@Binds` (the codebase uses `@Provides` everywhere — match it); new modules for one binding.

## 6. Navigation — routes, per-feature graphs, assembly

**Purpose.** Central route catalog plus per-screen wiring; features never touch `NavController`.

```kotlin
// feature/transaction/.../detail/navigation/TransactionDetailNavigation.kt
fun NavGraphBuilder.transactionDetailNavigation(
    onNavigateToEditTransaction: (TransactionType?, UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = Screen.TransactionDetail.route,
        arguments = listOf(navArgument(Screen.TransactionDetail.IS_TRANSFER) {
            type = NavType.BoolType }),
        deepLinks = listOf(navDeepLink {
            uriPattern = "moneyfikasi://transaction_detail/{transaction_id}/{is_transfer}" })
    ) {
        val viewModel = hiltViewModel<TransactionDetailViewModel>()
        val state by viewModel.state.collectAsState()
        // ...
    }
}

fun NavController.toTransactionDetail(id: UUID, isTransfer: Boolean) {
    navigate(Screen.TransactionDetail.routeWithArg(id, isTransfer))
}
```

- Reference: `shared/navigation/src/main/java/dev/muffar/moneyfikasi/navigation/Screen.kt` (29 `data object` destinations, `const` arg keys, `routeWithArg(...)` with `Uri.encode` for free text), `app/src/main/java/dev/muffar/moneyfikasi/navigation/{MainNavigation.kt,AppNavHost.kt}`, `shared/navigation/.../MainBottomNav.kt`
- Structure: 35 navigation files — per-subfeature `NavGraphBuilder.xxxNavigation(<lambda callbacks>)` + `NavController.toXxx`, per-feature aggregate (`*NavGraph.kt`), all assembled in `MainNavigation.kt` via callback hoisting. `AppNavHost.kt` owns transitions (slide between bottom-nav tabs, slide+fade elsewhere). Custom bottom nav (4 tabs + center add-sheet) in `MainScreen.kt`/`MainBottomNav.kt`, visible on Home/Transactions/Statistic/More only.
- Rules: new destination = `Screen` entry + `navigation/` file + aggregate registration + `MainNavigation` entry; optional args are query params with `defaultValue = ""`, read in the ViewModel via `SavedStateHandle` (never `backStackEntry.arguments` in the Screen); new code uses `collectAsStateWithLifecycle()`.
- Avoid: navigating from ViewModels/Screens; holding `NavController` in a feature.
- Variations (do not unify): aggregate file names differ (`TransactionsNavGraph`, `WalletsNavGraph.kt`, `budgetsNavGraph`, `presetGraph`, `appLockNavGraph`); state collection is mixed — 15 files use `collectAsStateWithLifecycle()`, 11 still use bare `collectAsState()` (e.g. all of `:feature:statistic`, `:feature:wallet`, transfer/detail/add-edit transaction); only two destinations declare deep links (`TransactionDetail`, add-edit recurring in `RecurringTransactionNavigation.kt`).

## 7. Testing — fakes, relaxed mocks, dispatcher rule

**Purpose.** Fast JVM tests for ViewModel logic and pure helpers; no UI tests exist.

```kotlin
// feature/transaction/.../list/TransactionsViewModelCalendarTest.kt
@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelCalendarTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val transactionUseCases = TransactionUseCases(
            getAllTransactions = GetAllTransactions(FakeTransactionRepository()),
             /* ... */
            addTransaction = mockk(relaxed = true),
            // ... remaining aggregate fields: relaxed mocks
        )
        viewModel = TransactionsViewModel(/* ... */)
    }
}
```

- Reference: `feature/transaction/src/test/.../transaction/list/TransactionsViewModelCalendarTest.kt` (hand-written Fakes + relaxed-mock fillers + `runTest`/`advanceUntilIdle`), `feature/backup_restore/src/test/.../backup_restore/BackupRestoreViewModelDriveTest.kt` (full-mock variant; green via `:feature:backup_restore:testDebugUnitTest`), `shared/utils/src/test/.../utils/extensions/LocalDateTimeExtTest.kt` (pure logic, no coroutine setup), `shared/data/src/test/.../data/remote/drive/DriveAuthHelperTest.kt`
- Rules: mirror the source package in tests; name `<SUT><Aspect>Test` with snake_case fns; real tests exist in only 4 modules (`feature:transaction`, `feature:backup_restore`, `shared:{utils,data}`) — ignore the `Example*` templates in `:app`; only `:feature:backup_restore` uses Robolectric (`androidx.test:core`, `work-testing` hardcoded in its build file, outside the version catalog).
- Avoid: starting a Compose UI-test setup without agreement; `mockkStatic` (removed from the backup test during standardization — prefer fakes).
- Not verified: instrumented tests need an emulator and were never run in this project.

## 8. Cross-cutting rules

- Time = ThreeTenABP (`org.threeten.bp.*`), initialized in `MoneyfikasiApplication`; never `java.time`.
- Feedback: `SnackbarType` (`SUCCESS/ERROR/PLAIN`) + `SnackbarHostState.showMessage(msg, type)` + `SnackbarMessage` host (`shared/common-ui/.../component/message/`). Destructive confirmations use `CommonAlertDialog` (8 screens: all add-edit screens + `TransactionDetailScreen`); `LoadingDialog` only on long processes (`BackupRestoreScreen`, `ExportScreen`).
- Imports: import the class, never fully-qualified names inline. Known pre-existing violations (`android.net.Uri.encode` across `Screen.kt`, `BackupRestoreNavigation.kt`, `Theme.kt`) — fix only when already touching the file; default `lint` does not flag this.
- Package convention: `dev.muffar.moneyfikasi.<feature>`, matching the Gradle `namespace`.
