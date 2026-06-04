// Traceability anchors for AgentHarness iteration / bugfix flow management.
// The executable checks live in .codebuddy/scripts/eval/tests/ and harness/eval scripts.

// @ACC-HARNESSFLOW-001 iteration and bugfix work item types are defined and parsed.
// @ACC-HARNESSFLOW-002 iteration lifecycle and branches are represented in flow metadata/workbench lanes.
// @ACC-HARNESSFLOW-003 bugfix lifecycle is simplified and evidence checks are separate from status.
// @ACC-HARNESSFLOW-004 minimal flow fields and bug fields are parsed from SPEC plus process-dir .flow.json/template.
// @ACC-HARNESSFLOW-005 Plan todos require spec_refs/verification and scope expansion returns to SPEC/Plan.
// @ACC-HARNESSFLOW-006 workbench renders dashboard, lanes, evidence cards, filters and evidence links.
// @ACC-HARNESSFLOW-007 Review Queue includes bug/iteration flow-specific missing evidence rules.
// @ACC-HARNESSFLOW-008 bugfix release requires Eval evidence plus regression link or explicit manual exception.
// @ACC-HARNESSFLOW-009 iteration planned readiness checks non-blocking SPEC, ACC-ID, Plan and Eval plan.
// @ACC-HARNESSFLOW-010 HTML remains derived; gates use Markdown/JSON/JSONL authoritative artifacts.
// @ACC-HARNESSFLOW-011 inline review annotation can source flow review/blocking items.
// @ACC-HARNESSFLOW-012 Eval/self-tests cover SPEC, Plan, workbench, Review Queue, regression and traceability.
// @ACC-HARNESSFLOW-013 Socratic question generation supports dimensions and statuses.
// @ACC-HARNESSFLOW-014 Agent self-answered questions record answer, confidence and evidence.
// @ACC-HARNESSFLOW-015 HTML question panel supports grouping, context, controls, evidence links and response writeback.

if (typeof module !== 'undefined') {
  module.exports = { harnessFlowTraceabilityAnchors: true };
}
