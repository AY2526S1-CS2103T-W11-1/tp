---
description: 运行 6 类自动化验收 suite（UI/E2E/Contract/Perf/Arch/Traceability）的聚合入口；并行调度，输出统一 JSON。
argument-hint: "[suite-list] (默认全跑：ui e2e contract perf arch traceability)"
allowed-tools: Bash(python -m agentharness eval run-acceptance-all:*)
---

# /eval:acceptance-all — 全自动化验收聚合

并行运行 6 类 acceptance_* suite，输出聚合 JSON：

| Suite | 维度 | 门禁 |
|---|---|---|
| `acceptance_ui` | UI 视觉回归 | soft |
| `acceptance_e2e` | E2E 用户旅程 | hard |
| `acceptance_contract` | 接口契约（OpenAPI vs impl） | hard |
| `acceptance_perf` | 性能预算（k6 + LH CI） | soft |
| `acceptance_arch` | 架构契约（NetArchTest + dep-cruiser） | hard |
| `acceptance_traceability` | 需求追溯矩阵 | hard |

## 行为规范

- **退出码**：0 = 全部 hard suite pass（release_allowed=true）；1 = 至少一个 hard fail；2 = blocked
- **聚合输出**：`.codebuddy/harness-runs/<run-id>/eval/aggregated-result.json`
- **soft 门禁**：UI / Perf 即使 fail 也不阻断 release（仅记 evidence）
- **生产隔离**：仅打 staging 8080/5100，禁止指向 `${PROD_PATH}/` 或生产端口

## 执行

!`ACC_SUITES="${1:-ui e2e contract perf arch traceability}" STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" python -m agentharness eval run-acceptance-all`

## 环境变量

- `ACC_SUITES="ui e2e"`：仅跑指定 suite
- `ACC_FAIL_FAST=true`：任一 hard fail 立即终止
- `STAGING_DRY_RUN=true`：所有 suite 走 dry-run（CI 烟雾）
- `HARNESS_RUN_ID`：复用上层 run_id

## 关联

- 单 suite 命令：`/eval:acceptance-{ui,e2e,contract,perf,arch,traceability}`
- 工具安装：`python -m agentharness install-acceptance-tools`
- 规则：`.codebuddy/rules/acceptance-id-convention.mdc`
