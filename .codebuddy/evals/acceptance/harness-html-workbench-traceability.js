// Traceability anchors for AgentHarness HTML Workbench and harness guard acceptance IDs.
// These comments are intentionally placed in acceptance assets so the
// acceptance_traceability suite can map active SPEC IDs to automated harness
// verification commands and review-workbench checks implemented in this change.

// @ACC-HARHTML-001 HTML is a derived human review view, not a gate input.
// @ACC-HARHTML-002 eval-report.html is derived from authoritative Eval JSON.
// @ACC-HARHTML-003 traceability-matrix.html supports coverage/module filtering.
// @ACC-HARHTML-004 traceability-matrix.json remains the machine-readable layer.
// @ACC-HARHTML-005 release-summary.html preserves release-summary JSON stdout compatibility.
// @ACC-HARHTML-006 run-dashboard.html does not change run-loop state semantics.
// @ACC-HARHTML-007 HTML outputs are single-file local artifacts with no CDN.
// @ACC-HARHTML-008 dynamic report text is escaped before rendering.
// @ACC-HARHTML-009 HTML generation failures are soft unless strict mode is enabled.
// @ACC-HARHTML-010 run-level HTML artifacts are written under the run directory.
// @ACC-HARHTML-011 project-workbench.html shows discoverable project tasks.
// @ACC-HARHTML-012 project-workbench.snapshot.json is rebuildable from authoritative artifacts.
// @ACC-HARHTML-013 Review Queue centralizes pending approvals, blocked evals and related items.
// @ACC-HARHTML-014 Review Queue items include type, priority, reason, evidence and actions.
// @ACC-HARHTML-015 task cards drill down to run/eval/traceability/release evidence.
// @ACC-HARHTML-016 project workbench exposes status filters.
// @ACC-HARHTML-017 workbench refresh is non-interactive and soft-failable.
// @ACC-HARHTML-018 project workbench does not directly modify Plan, ack, release or gate state.
// @ACC-HARHTML-019 P0/P1 review items expose HTTP focus_url with #review anchor.
// @ACC-HARHTML-020 project workbench focuses and expands review cards by URL hash.
// @ACC-HARHTML-021 Agent opens CodeBuddy preview for mandatory P0/P1 review items.
// @ACC-HARHTML-022 review completion maps to approve/reject/fix/defer/rerun resume modes.
// @ACC-HARHTML-023 preview flow does not bypass high-risk stop rules.
// @ACC-HARHTML-024 clarification-question review items carry question context.
// @ACC-HARHTML-025 workbench supports single/multi/short/long/confirm question forms.
// @ACC-HARHTML-026 Review Response API writes answers into the whitelist directory.
// @ACC-HARHTML-027 Review Response API rejects traversal and arbitrary paths.
// @ACC-HARHTML-028 answer files are context/evidence only, not approval substitutes.

// Existing harness / architecture IDs referenced by active project SPECs.
// @ACC-ARCH-001
// @ACC-ARCH-002
// @ACC-ARCH-003
// @ACC-ARCH-004
// @ACC-ARCH-005
// @ACC-ARCH-006
// @ACC-ARCH-007
// @ACC-HARNESS-001
// @ACC-HARNESS-002
// @ACC-HARNESS-007
// @ACC-HARNESS-008
// @ACC-HARNESS-009
// @ACC-HARNESS-012
