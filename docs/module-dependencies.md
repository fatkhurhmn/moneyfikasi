# Module Dependencies — Moneyfikasi

> Copied from actual `build.gradle.kts` files (dependency audit, all edges verified). When adding a dependency, edit the file named here — never from memory. Code patterns live in `docs/coding-patterns.md`; build commands and secrets live in `AGENTS.md`.

## 1. Module map

24 modules registered in `settings.gradle.kts:31-54`, plus `build-logic` as an included build (`settings.gradle.kts:4`):

- `:app` — sole aggregator and entry point.
- `:shared:resource`, `:shared:utils`, `:shared:domain`, `:shared:data`, `:shared:navigation`, `:shared:common-ui` — shared layers.
- 17 × `:feature:*` — `home`, `transaction`, `wallet`, `category`, `budget`, `statistic`, `search`, `export`, `backup_restore`, `applock`, `settings`, `recurring_transaction`, `notification`, `about`, `splash`, `more`, `preset`.

## 2. `:app` — depends on everything, nothing depends on it

`app/build.gradle.kts:42-66` declares `implementation` on all 6 shared modules and all 17 features (accessor style: `projects.feature.backupRestore`, `projects.feature.recurringTransaction`, …), plus `core-ktx`/`appcompat`/`lifecycle`/`activity-compose`, Compose BOM + bundle, Hilt + KSP, `work-runtime`/`hilt-work`, and `threetenabp`. No module in the repo depends on `:app` — the direction is proven one-way.

## 3. `:shared:*` — acyclic layer graph

| Module | Declared in | Direct project deps |
|---|---|---|
| `resource` | `shared/resource/build.gradle.kts` | none (leaf; only `androidx.appcompat`) |
| `utils` | `shared/utils/build.gradle.kts` | `shared:resource` |
| `domain` | `shared/domain/build.gradle.kts` | `shared:{utils,resource}` (+ Hilt, threetenabp, `paging-common`, `kotlinx.serialization.json`) |
| `data` | `shared/data/build.gradle.kts` | `shared:{domain,resource,utils}` (+ Room, DataStore, WorkManager, Paging, Retrofit/Groq, Drive/Credential libs) |
| `navigation` | `shared/navigation/build.gradle.kts` | `shared:{resource,commonUi,domain}` — never `data` |
| `common-ui` | `shared/common-ui/build.gradle.kts` | `shared:{resource,domain,utils}` (+ Compose, threetenabp, MPAndroidChart, Paging) |

Responsibilities follow the edges: `domain` and `navigation` provably never touch `data` at the Gradle level, which is what enforces "no repositories in ViewModels" structurally. `resource` is the dependency leaf.

## 4. `:feature:*` — four verified groups, not one template

**Full (12):** `home`, `transaction`, `wallet`, `category`, `budget`, `statistic`, `search`, `preset`, `backup_restore`, `export`, `applock`, `recurring_transaction` declare all 6 shared modules. Per-module extras: `transaction` (+ Paging, + 5 catalog test deps), `statistic`/`search` (+ Paging), `home`/`transaction` (+ `capturable`), `backup_restore` (+ WorkManager/Hilt-Work, + Drive API libs, + 8 test deps incl. hardcoded Robolectric), `applock` (+ Biometric).

**Without data/utils (2):** `feature/notification/build.gradle.kts` and `feature/settings/build.gradle.kts` declare only `shared:{domain,navigation,commonUi,resource}` (+ Hilt). Verified consistent with source: `feature/notification/src` contains zero `data.*` imports; `feature/settings/src` imports only `common_ui`/`domain`/`resource`.

**`more` anomaly (1):** `feature/more/build.gradle.kts` declares `shared:data` but NOT `shared:utils`, and `feature/more/src` imports neither `data.*` … verified: no `moneyfikasi.utils` import in `more/src`. Harmless today (unused), but inconsistent — see §6.

**Non-Hilt (2):** `feature/about/build.gradle.kts` and `feature/splash/build.gradle.kts` apply only `moneyfikasi.android.library` + `moneyfikasi.android.compose` (no KSP/Hilt plugins). Consistent: neither module contains a ViewModel (`about` is static license data; `splash` is a static screen).

## 5. What convention plugins provide (and don't)

`build-logic/src/main/kotlin/Android{Application,Library,Compose}ConventionPlugin.kt`, registered in `build-logic/build.gradle.kts` as `moneyfikasi.android.{application,library,compose}`:

- They add **zero dependencies** — only plugin application, SDK levels (compile 37 / min 26 / target 36 via `AppConfig.kt`), Java/Kotlin 17, `compose = true`, signing/debug-release config.
- Every `implementation(...)` in §2–§4 is therefore an explicit per-module declaration. KSP and Hilt plugin aliases are re-declared in each module that needs them (everything except `about`/`splash`).
- The whole repo uses `implementation`. There is no `api`, and `compileOnly`/`runtimeOnly` appear only in `build-logic/build.gradle.kts:8-9` (AGP + kotlin-gradle-plugin for compiling the plugins themselves). No API leakage between modules by declaration.

## 6. Rules for adding dependencies

- New feature → copy the `build.gradle.kts` of the CLOSEST existing feature, not a generic template (`about`/`splash` prove the Hilt/KSP/data stack is optional).
- Reference modules via typesafe accessors only: `implementation(projects.shared.domain)` — enabled by `TYPESAFE_PROJECT_ACCESSORS` in `settings.gradle.kts:1`. Never use project-path strings.
- New libraries go through the version catalog (`gradle/libs.versions.toml`); do not hardcode versions (existing violations, do not copy: test deps in `shared/data`, `shared/utils`, `feature/backup_restore`).
- `api` is effectively forbidden (repo is 100% `implementation`); feature→feature edges are forbidden unless justified.
- New preferences/remote infra belongs in `:shared:data` (DataStore/Retrofit/Drive libs live only there — verified in `shared/data/build.gradle.kts`); UI widgets shared by ≥2 features belong in `:shared:common-ui`.

## 7. Resolved during audit (kept for history)

- `feature/settings/build.gradle.kts` no longer declares `projects.feature.notification` — verified unused (zero imports in `feature/settings/src`) and removed; `:feature:settings:assembleDebug` + `:app:assembleDebug` green after removal.
- `feature/more/build.gradle.kts` now declares `projects.shared.utils` like the other 12 full features (previously the only one missing it); `:feature:more:assembleDebug` green.
