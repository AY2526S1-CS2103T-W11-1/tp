---
description: |
  Refresh SDK plugin registration and minimal root projection from .codebuddy/<sdk-dir>/.
  The SDK directory is auto-detected (supports harness-sdk, harness_sdk, etc.).
  Use this after pulling a new SDK version. Default is dry-run.

  Plugin-first + Minimal Projection model:
    - commands / agents / skills / hooks       → read directly from SDK plugin
    - scripts / rules / templates / evals      → minimal projection to root
    - workbench/vendor                          → minimal projection to root
    - .env*, .codebuddy/mcp.json, harness-runs, knowledge → never_manage (never overwritten)

  Acceptance: ACC-HARNSDK-035 / ACC-HARNSDK-039 (SPEC §5.4 §5.5)
allowed-tools:
  - bash
arguments:
  - name: mode
    description: "Update mode: --check | --dry-run | --apply"
    required: false
    default: "--dry-run"
  - name: backup
    description: "When set to --backup-conflicts, conflicting root files are backed up before overwrite"
    required: false
    default: ""
---

# /harness:update-sdk

Refresh the SDK → root projection after the SDK has been upgraded.

## Usage

```bash
/harness:update-sdk                              # dry-run (default), no file is modified
/harness:update-sdk --check                      # read-only verification of plugin manifest + state
/harness:update-sdk --apply                      # apply minimal projection; fails on conflicts
/harness:update-sdk --apply --backup-conflicts   # apply and back up conflicting root files
```

## Behavior

The command invokes the SDK installer:

```bash
python -m agentharness harness install-sdk-assets \
  "${mode:-"--dry-run"}" ${backup}
```

It outputs a single JSON object to stdout with keys:

```text
status              pass | fail | conflict
mode                --check | --dry-run | --apply
plugin_registered   true | false (true means SDK .codebuddy-plugin/plugin.json present + plugin.enabled)
managed_files       integer
created             [ relpath, ... ]
updated             [ relpath, ... ]
skipped             [ relpath, ... ]
conflicts           [ relpath, ... ]
never_manage_blocked[ relpath, ... ]
backup_dir          string | null
state_file          .codebuddy/harness-project/sdk-projection-state.json
exit_code           0 = pass, 1 = conflict / soft-fail, 2 = hard error
```

## Safety

- Default mode is `--dry-run`; never modifies the file system unless `--apply` is given
- Conflicts (root file diverges from SDK source and is not in projection state) **block by default**
- `--backup-conflicts` is the only way to overwrite divergent files; backups are stored in
  `.codebuddy/harness-project/projection-backups/<timestamp>/`
- `never_manage` paths (`.env*`, `mcp.json`, `harness-runs`, `harness-project`, `knowledge`, …) are
  never written, even in `--apply --backup-conflicts`
- The maintainer-only command `python -m agentharness sync-sdk` runs in the **opposite** direction (root → SDK) and is
  unrelated to this command

## When to run

- After upgrading the SDK in place (e.g. updating `.codebuddy/harness-sdk/` or `.codebuddy/harness_sdk/` content)
- When `python -m agentharness harness check-sdk-publish` reports `projection_drift`
- When the projection state file is missing or out of date

## ACC tags

- `[ACC-HARNSDK-035]` — explicit user-facing update command
- `[ACC-HARNSDK-039]` — drift detection and conflict policy
