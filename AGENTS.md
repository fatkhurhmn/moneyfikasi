# AGENTS.md — Moneyfikasi

Android multi-module app (Kotlin, Compose M3, Hilt, Room). JDK 17 required (CI: Temurin 17).

## Structure

- `:app` — entry point only (`MainActivity` → `MainScreen`, DI wiring). All screens live in feature modules.
- `:shared:domain` — use cases + repository interfaces; `:shared:data` — Room/DataStore/remote impls + Hilt modules (`data/di/`).
- `:shared:navigation` — centralized NavGraphs (e.g. `TransactionsNavGraph`); each feature exposes its own `*NavGraph`. Add cross-feature navigation here, not in `:app`.
- `:shared:{resource,common-ui,utils}` — strings/theme/components/helpers shared by features.
- `:feature:*` — 17 modules (`home`, `transaction`, `wallet`, `category`, `budget`, `statistic`, `search`, `export`, `backup_restore`, `applock`, `settings`, `recurring_transaction`, `notification`, `about`, `splash`, `more`, `preset`). Feature deps: `shared:{domain,data,navigation,common-ui,resource,utils}` (not all use paging/`capturable` — copy the closest feature's build file, not a template).
- Convention plugins in `build-logic/src/main/kotlin/` (`moneyfikasi.android.{application,library,compose}`); `AppConfig.kt` is the single source for `APPLICATION_ID`, SDK levels (compile 37 / min 26), `VERSION_NAME`/`VERSION_CODE`. Bump versions there.
- Typesafe project accessors enabled — reference modules as `projects.shared.domain`, etc.

## Conventions (verified, follow these)

- MVI per screen: `XxxState` (data class, all defaults) + `XxxEvent` (sealed) + `@HiltViewModel` with `MutableStateFlow`/`_state`/`onEvent(when)`. Screen is stateless (`state` + lambdas); wiring lives in `navigation/*Navigation.kt` via `hiltViewModel<T>()` + `collectAsStateWithLifecycle()`. Ref: `feature/transaction/.../list/`.
- One-shot UI events (snackbar, navigate-after-save) go through a separate `MutableSharedFlow<UiEvent>` + `eventFlow`, NOT state. Show via `SnackbarHostState.showMessage(msg, SnackbarType)` with `snackbarHost = { SnackbarMessage(state) }` in the Screen. Ref: `feature/transaction/.../transfer/TransferTransactionViewModel.kt`.
- ViewModels inject `*UseCases` aggregates only, never repositories. Single use cases are classes with `operator fun invoke`, registered in `shared/domain/.../di/UseCaseModule.kt`. Repository impls are bound in `shared/data/.../di/RepositoryModule.kt` — except `DriveBackupRepository`, which is bound in `DriveModule.kt` (with `CredentialManager` + `AuthorizationClient`).
- Navigation: add route to `shared/navigation/.../Screen.kt` (with `routeWithArg`), add `NavGraphBuilder.xxxNavigation` + `NavController.toXxx` in the feature, assemble in `app/.../navigation/MainNavigation.kt`. Features never hold `NavController` — accept lambda callbacks. Bottom nav (4 tabs + center add-sheet) lives in `MainScreen.kt`/`MainBottomNav.kt`, visible only on Home/Transactions/Statistic/More.
- Time = ThreeTenABP (`org.threeten.bp.*`), not `java.time`. New preferences = serializer in `data/preferences/serializer/` + `@Provides DataStore` in `DataStoreModule.kt` (with `ReplaceFileCorruptionHandler`) + repository + use cases.
- Transfers are atomic in `TransactionDao.performTransfer` (`@Transaction`: TRANSFER_OUT + TRANSFER_IN sharing one `transactionReference`, plus EXPENSE fee row if fee > 0; same-wallet guard throws in repository). Recurring = `RecurringTransactionScheduler` (AlarmManager) → Receiver → `@HiltWorker RecurringTransactionWorker` → reschedule; `BootReceiver` reschedules after reboot.
- Package convention: `dev.muffar.moneyfikasi.<feature>` for Kotlin packages (matches Gradle `namespace`); repository impls live in `data.repository`.

## Workflow

- Verify-first: inspect existing files and trace a comparable implementation before writing code; never assume a class/API/pattern exists. State uncertainty instead of guessing.
- Smallest coherent diff: follow existing patterns, don't introduce new libraries/modules/patterns, don't touch unrelated files, never disable tests/lint to make things pass.
- After changes: review the full diff (imports, packages, DI wiring), run the narrowest relevant build/test, then broader checks when practical.
- Validation tiers (choose by scope, don't run the full gate for small changes):
  - Fast (single-module, no API/DI/nav change): `./gradlew :<mod>:assembleDebug`, plus `:testDebugUnitTest` only if the module has tests (`feature:transaction`, `feature:backup_restore`, `shared:{utils,data}` — others have none, state that instead of claiming coverage).
  - Targeted (feature/shared change, use case, DI, navigation, Room): add `:testDebugUnitTest` + `:lintDebug` for the module and `:app:assembleDebug` (only task compiling all NavGraphs + the full Hilt graph). For Room also check `git status` for new schema JSON.
  - Full (before PR to `develop`/release): CI gate `./gradlew lint test assembleRelease bundleRelease` + smoke test; mandatory whenever adding serialization/reflection (release R8 with template-only ProGuard).
- After code changes always run `git diff --check` plus the tier above; no emulator-dependent checks (instrumented tests are template-only and never run in CI).
- On failure, triage before fixing: AI-caused (your diff) vs environment (local JDK defaults to 21, project requires 17; missing secrets stub AI/Drive silently) vs pre-existing (verify via `git stash` + rerun when in doubt).
- Report actual commands executed with real results — including checks not run or failed. Never claim success without a completed run.

## Commands

- Quality gate (same as GitHub Actions CI): `./gradlew lint test assembleRelease bundleRelease`
- Focused unit test: `./gradlew :feature:transaction:testDebugUnitTest --tests "dev.muffar.moneyfikasi.transaction.list.TransactionsViewModelCalendarTest"`
- Single module build: `./gradlew :shared:data:assembleDebug`
- Only a few modules have unit tests (`feature:transaction`, `feature:backup_restore`, `shared:{utils,data}`). `./gradlew test` runs all; instrumented tests need emulator (`app/src/androidTest`).
- Release notes (required before Play publish): edit root `release-notes-en.txt` / `release-notes-id.txt`, then `./scripts/sync-release-notes.sh` (enforces 500-char Play limit into `app/src/main/play/release-notes/`).
- Publish: `./gradlew publishReleaseBundle` (default track `internal`; override `-PPLAY_TRACK=production`).

## Secrets & build gotchas

- `local.properties` (gitignored) provides `GROQ_API_KEY`, `DRIVE_WEB_CLIENT_ID` → `BuildConfig` in `:shared:data`. Resolution order: Gradle property > env var > `local.properties`; missing = empty string, so AI smart-input / Drive backup silently fail at runtime. CI injects from secrets — never commit these files (`local.properties`, `app/play-account.json`).
- Release signing config is created only if all four `MONEYFIKASI_RELEASE_*` props are present; otherwise release builds are unsigned. Release has `isMinifyEnabled = true` but `app/proguard-rules.pro` is still template-only (no `-keep` rules) — test `assembleRelease` + smoke test whenever adding serialization/reflection.
- Room: `exportSchema = true`, schemas versioned under `shared/data/schemas/.../`. Keep schema export on; schema JSON is a build artifact to commit.
- PRs target `develop` (see README); `v*` tags trigger Play deploy. CircleCI config is legacy — GitHub Actions (`.github/workflows/ci.yml`) is current.
