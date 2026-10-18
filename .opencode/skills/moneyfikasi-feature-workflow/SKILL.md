---
name: moneyfikasi-feature-workflow
description: Step-by-step Moneyfikasi feature workflow — inspect, plan, implement minimally, test, validate, report
metadata:
  opencode/slash: "true"
---

Reusable workflow for Moneyfikasi feature work. Cite `AGENTS.md`, `docs/coding-patterns.md`, and `docs/module-dependencies.md` instead of copying them. If the request is ambiguous in a way that materially changes the implementation, ask before coding; otherwise proceed.

## Stages

1. **Understand.** Restate the requirement in one paragraph. If `/audit-feature` has not been run for the area, run it first.
2. **Inspect.** Read the related files and the closest existing implementation. List file paths and their responsibilities.
3. **Plan.** Write the change list (files to create/edit and why), the pattern to follow, and open questions. Wait for approval on risky architectural choices.
4. **Implement.** Smallest coherent diff following existing patterns. No new libraries, modules, or abstractions without justification; no unrelated changes.
5. **Test.** Add or update unit tests per `docs/coding-patterns.md` §7 (only where the module supports them — state it when it doesn't).
6. **Validate.** Pick the tier from `AGENTS.md` §Workflow (fast / targeted / full) by change scope, plus `git diff --check`. Triage failures: AI-caused vs environment vs pre-existing.
7. **Report.** Changed files, validation commands with real results (including failed/skipped checks and why), and remaining risks.

## Checklist (verify each before finishing)

- [ ] Architecture: MVI shape, `*UseCases`-only ViewModel, no repository/`Context`/navigation in ViewModel
- [ ] Dependencies: edges allowed per `docs/module-dependencies.md`; accessors, catalog, `implementation` only
- [ ] State/events: persistent UI in `State`, one-shot occurrences in `eventFlow`, errors as `UiEvent.ShowMessage`
- [ ] Error handling: failure paths emit messages; no silent catches except documented cases
- [ ] Edge cases: empty lists, null args/defaults, same-wallet transfer guard where relevant
- [ ] Regression: touched screens' existing behavior preserved; full Hilt graph compiles (`:app:assembleDebug` for DI/nav changes)
