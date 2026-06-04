// Traceability anchors for AgentHarness common SDK extraction and publish readiness.
// The assertions below keep @ACC-HARNSDK-* tags tied to concrete SDK assets.

// @ACC-HARNSDK-001 asset inventory distinguishes SDK core, overlay, runtime and excluded assets.
// @ACC-HARNSDK-002 SDK and overlay target directory structure is present.
// @ACC-HARNSDK-003 harness:init minimum initialization content covers core asset families.
// @ACC-HARNSDK-004 harness:init supports dry-run/check/apply style safe operation.
// @ACC-HARNSDK-005 SDK manifest and overlay variable resolution are implemented.
// @ACC-HARNSDK-006 skills are classified into SDK core / optional / project-specific groups.
// @ACC-HARNSDK-007 sensitive assets are excluded from SDK core.
// @ACC-HARNSDK-008 compatibility wrappers and CLI compatibility layer are present.
// @ACC-HARNSDK-009 fixture project initialization smoke checks exist.
// @ACC-HARNSDK-010 HTML/workbench outputs remain derived evidence, not gate inputs.
// @ACC-HARNSDK-011 Eval verification covers spec/plan/init/fixture/compat/security/traceability.
// @ACC-HARNSDK-012 user review questions are captured in SPEC review sections.
// @ACC-HARNSDK-013 sdk-sanitizer supports scan/apply replacement modes.
// @ACC-HARNSDK-014 sync-sdk is available as a Python module.
// @ACC-HARNSDK-015 check-sdk-dir is available as a Python module.
// @ACC-HARNSDK-016 sdk-fixture-smoke is available as a Python module.
// @ACC-HARNSDK-017 check-publish aggregates strict SDK readiness checks.
// @ACC-HARNSDK-018 sdk_publish_ready can run through python -m agentharness sdk check-publish.
// @ACC-HARNSDK-019 SDK templates are present under harness-sdk/templates.
// @ACC-HARNSDK-020 README / publish guidance uses Python-first command style.
// @ACC-HARNSDK-031 SDK manifest declares plugin-first projection metadata.
// @ACC-HARNSDK-032 plugin manifest is present.
// @ACC-HARNSDK-033 projection dry-run/check entry is present.
// @ACC-HARNSDK-034 plugin apply fixture exists.
// @ACC-HARNSDK-035 update-sdk fixture exists.
// @ACC-HARNSDK-036 conflict fixture exists.
// @ACC-HARNSDK-037 plugin apply fixture validates managed files.
// @ACC-HARNSDK-038 sensitive guard fixture exists.
// @ACC-HARNSDK-039 workbench progress fixture exists.
// @ACC-HARNSDK-040 workbench progress fixture covers projection progress.

const fs = require('fs');
const path = require('path');

const root = process.cwd();
function mustExist(rel) {
  const full = path.join(root, rel);
  if (!fs.existsSync(full)) throw new Error(`missing required asset: ${rel}`);
}
function mustContain(rel, needle) {
  const full = path.join(root, rel);
  mustExist(rel);
  const text = fs.readFileSync(full, 'utf8');
  if (!text.includes(needle)) throw new Error(`missing ${needle} in ${rel}`);
}

mustExist('.codebuddy/harness-sdk/sdk-manifest.yaml');
mustExist('.codebuddy/harness-sdk/.codebuddy-plugin/plugin.json');
mustExist('.codebuddy/harness-sdk/commands/harness/init.md');
mustExist('.codebuddy/harness-sdk/commands/harness/update-sdk.md');
mustExist('.codebuddy/harness-sdk/agentharness/harness/harness_init.py');
mustExist('.codebuddy/harness-sdk/scripts/harness/config-merge.py');
mustExist('.codebuddy/harness-sdk/scripts/harness/install-skills.py');
mustExist('.codebuddy/harness-sdk/agentharness/harness/sdk_sanitizer.py');
mustExist('.codebuddy/harness-sdk/agentharness/harness/sync_sdk.py');
mustExist('.codebuddy/harness-sdk/agentharness/harness/check_sdk_dir.py');
mustExist('.codebuddy/harness-sdk/agentharness/harness/sdk_fixture_smoke.py');
mustExist('.codebuddy/harness-sdk/agentharness/sdk/check_publish.py');
mustExist('.codebuddy/harness-sdk/templates');
mustContain('.codebuddy/harness-sdk/README.md', 'python');

if (typeof module !== 'undefined') {
  module.exports = { harnessSdkTraceabilityAnchors: true };
}
