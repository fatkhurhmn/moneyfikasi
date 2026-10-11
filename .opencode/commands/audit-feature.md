---
description: Audit a Moneyfikasi feature before changing code (read-only, no edits)
agent: explorer
---

Audit the Moneyfikasi feature named $ARGUMENTS (e.g. `/audit-feature transfer`). DO NOT create, edit, refactor, or delete any file, and do not start implementing.

Read these first — they are verified against this repo:

- Project rules: @AGENTS.md
- Implementation patterns: @docs/coding-patterns.md
- Module boundaries: @docs/module-dependencies.md

Then produce the report below. Every conclusion must come from actual source code: verify each module, package, class, function, and dependency before naming it. If something cannot be verified, mark it UNVERIFIED instead of guessing.

1. **Current flow.** Trace the relevant path Screen → ViewModel (`*State`/`*Event`/`eventFlow`) → UseCase (`*UseCases` aggregate) → Repository interface → data impl/DAO, only where relevant. Name each file.
2. **Related files.** List every relevant file with one line each explaining why it matters.
3. **Closest existing pattern.** Name a similar implemented feature and the exact pattern to copy (per `docs/coding-patterns.md`).
4. **Boundaries.** State the architectural and dependency limits that apply: allowed module edges (`docs/module-dependencies.md`), `*UseCases`-only ViewModels, `SavedStateHandle` + `Screen` keys for args, ThreeTenABP time, `collectAsStateWithLifecycle()`.
5. **Risks.** Edge cases, regression risks, and behaviors that must not change.
6. **Test plan.** Existing test files covering the area (if any) and what new tests the change would need.
7. **Change plan.** Concrete steps and likely affected files — a plan only, no implementation.
