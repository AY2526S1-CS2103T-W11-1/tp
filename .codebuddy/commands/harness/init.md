---
description: |
  Initialize a new project (or upgrade an existing one) with the AgentHarness SDK
  knowledge base, rules, commands, scripts, eval suites, workbench, and core skills.

  Creates `.codebuddy/<sdk-dir>/` (read-only SDK assets, auto-detected directory name) and `.codebuddy/harness-project/`
  (project overlay) without overwriting any existing project files. Idempotent.

  ACC-HARNSDK-003 / ACC-HARNSDK-004: see SPEC §6.4
allowed-tools:
  - bash
arguments:
  - name: mode
    description: "Init mode: --dry-run | --check | --apply | --repair"
    required: false
    default: "--dry-run"
  - name: profile
    description: "Eval profile: minimal | standard | high_risk"
    required: false
    default: "standard"
---

# /harness:init

Initialize the AgentHarness SDK + project overlay. **By default** runs `--dry-run`
(no file is modified) and emits an init report describing the planned actions.

## Usage

```bash
/harness:init                                  # dry-run, default profile=standard
/harness:init --check                          # idempotent self-check on existing setup
/harness:init --apply                          # actually create/merge files
/harness:init --apply --profile minimal        # minimal eval profile
/harness:init --repair                         # restore missing SDK assets
```

## Behavior

The wrapper invokes:

```bash
python -m agentharness harness harness-init "${mode:-"--dry-run"}" \
  --profile "${profile:-"standard"}" \
  --report .codebuddy/harness-runs/harness-init-report.json
```

Output:

- JSON init report at `.codebuddy/harness-runs/harness-init-report.json`
- Markdown summary at `.codebuddy/harness-runs/harness-init-report.md`

## Init plan (minimum content)

1. Workspace detection and existing `.codebuddy/` snapshot
2. SDK asset validation (`python -m agentharness harness check-sdk-dir`)
3. Project overlay creation/merge (`harness-project/harness.project.yaml`)
4. Knowledge README + optional scan
5. Skills install (core / optional / project-only)
6. Non-destructive self-checks (yaml parse / template var substitution / sample spec/plan check / workbench self-test / secret scan)
7. Init report

## Safety

- Default behavior never overwrites existing project files
- `--apply` only creates missing files; conflicts produce patch suggestions in the report
- High-risk paths (`.env*`, `mcp.json`, `Migrations/*.sql`, etc.) are **never** copied into SDK
- Dry-run is enforced before any destructive operation

## ACC tags

- `[ACC-HARNSDK-003]` — minimum init content
- `[ACC-HARNSDK-004]` — idempotent / dry-run / check / apply / repair behavior
