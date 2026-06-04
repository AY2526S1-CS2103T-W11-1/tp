---
name: harness-core
description: |
  AgentHarness 标准流程、Plan 绑定、Eval、Repair、Release 闭环与证据格式规范。
  在用户请求触及多文件改动、跨模块变更、API/数据/权限/部署相关需求时主动加载本 skill，
  确保进入 Execute 前已完成 Plan 双登记，并按 SPEC → Plan → Execute → Eval → Repair → Release 流程推进。
allowed-tools:
  - bash
  - read
  - edit
---

# Harness Core Skill

> SDK core skill。新项目 `harness:init` 默认安装。

## 触发场景

- 用户提出"长任务"（≥3 文件改动 / 跨模块 / 涉及 API、数据、权限、部署）
- 用户要求按 SPEC 推进、跑 Plan、跑 Eval、跑 Release
- Agent 进入 Execute 前需要确认 Plan 双登记
- 出现高风险路径触发 6 类停机条件

## 标准流程

```
SPEC → Plan → Execute → Eval → Repair → Release
```

每一步的入口与脚本：

| 阶段 | IDE 命令 | SDK CLI |
|---|---|---|
| SPEC | `/harness:spec` | `python3 -m agentharness spec-check <spec-path>` |
| Plan | `/harness:plan` | `python3 -m agentharness plan-check <spec-path> <plan-path>` + `plan_create` IDE 工具（双登记） |
| Execute | `/harness:run` | `python3 -m agentharness run-loop <spec-path> [plan-path]` |
| Eval | `/harness:eval` | `python3 -m agentharness eval run-all <spec-path>` |
| Repair | (auto) | 由 run-loop 自动触发 |
| Release | `/harness:release` | `python3 -m agentharness pre-release-gate <spec-path>` |

## 双登记铁律

进入 Execute 前必须有 `*.plan.json` 落盘 **且** `plan_create` IDE 卡片登记。
若 `plan_create` 工具不可用：

```bash
python3 -m agentharness check-plan-mode "<intent>" --force-require
```

读取命令输出 JSON 的 `halt_message` 原样输出给用户，停机。**禁止**只写 `*.plan.json` 跳过 IDE 登记。

## 自动连推

进入 `/harness:run` 后自动推进，直到：

- `release_allowed=true`，或
- 命中 6 类高风险停机（部署 / 数据库写 / 生产配置 / 密钥 / SPEC 冲突 / permission-blocked）

期间 **禁止** 询问"C2/继续吗"。

## 证据格式

每个 todo 完成必须输出：

```
todo: <id>
  status: completed
  changed: <file list>
  verification:
    - cmd: <实际命令>
      exit: 0
      log: <evidence path>
  evidence: <run_dir>/...
```

## 参考

- `rules/harness-execution-protocol.mdc`
- `rules/spec-first-development.mdc`
- `rules/eval-gate.mdc`
- `rules/verification-evidence-standard.mdc`
