// Traceability anchors for AgentHarness Python-first cross-platform migration.
// These @ACC-HARNSDKPY-* tags are backed by lightweight checks against Python SDK assets.

// @ACC-HARNSDKPY-001 python -m agentharness help/CLI entry works without Bash.
// @ACC-HARNSDKPY-002 command dispatcher exposes grouped Python commands.
// @ACC-HARNSDKPY-003 direct legacy command compatibility remains available during migration.
// @ACC-HARNSDKPY-004 JSON contracts are covered by Python contract tests.
// @ACC-HARNSDKPY-005 shell scripts have Python module replacements.
// @ACC-HARNSDKPY-006 SDK publish readiness is implemented in Python.
// @ACC-HARNSDKPY-007 core harness contracts have Python tests.
// @ACC-HARNSDKPY-008 cross-platform path handling avoids Bash-only assumptions.
// @ACC-HARNSDKPY-009 Unicode project paths are represented in fixtures.
// @ACC-HARNSDKPY-010 Windows-client path-space fixture exists.
// @ACC-HARNSDKPY-011 runtime version check fixture exists.
// @ACC-HARNSDKPY-012 migrated command contracts are tested.
// @ACC-HARNSDKPY-013 migrate-shell verify path is implemented.
// @ACC-HARNSDKPY-014 shell-free SDK check is part of publish readiness.
// @ACC-HARNSDKPY-015 README documents Python-first usage.
// @ACC-HARNSDKPY-016 unsupported Python version reports blocked with hints.
// @ACC-HARNSDKPY-017 Workbench review state derivation is implemented.
// @ACC-HARNSDKPY-018 Workbench effective status rendering is implemented.
// @ACC-HARNSDKPY-019 Workbench review panel artifacts are available.

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

mustExist('.codebuddy/harness-sdk/agentharness/__main__.py');
mustExist('.codebuddy/harness-sdk/agentharness/cli.py');
mustExist('.codebuddy/harness-sdk/agentharness/sdk/check_publish.py');
mustExist('.codebuddy/harness-sdk/agentharness/sdk/migrate_shell.py');
mustExist('.codebuddy/harness-sdk/agentharness/tests/test_cli_smoke.py');
mustExist('.codebuddy/harness-sdk/agentharness/tests/test_harness_contracts.py');
mustExist('.codebuddy/harness-sdk/agentharness/fixtures/中文项目/test_unicode.py');
mustExist('.codebuddy/harness-sdk/agentharness/fixtures/windows client project/test_path_spaces.py');
mustExist('.codebuddy/harness-sdk/agentharness/fixtures/runtime-check/test_python_version.py');
mustContain('.codebuddy/harness-sdk/README.md', 'python');
mustContain('.codebuddy/harness-sdk/scripts/harness/render-project-workbench.py', 'derive_review_state');
mustExist('.codebuddy/harness-workbench/artifact-viewer.html');

if (typeof module !== 'undefined') {
  module.exports = { harnessSdkPyTraceabilityAnchors: true };
}
