---
description: 在 ${STAGING_PATH}/ 测试环境部署 + 冒烟，产出 eval suite 证据
argument-hint: "[frontend|backend|all] (默认 all)"
allowed-tools: Bash(python -m agentharness eval run-staging-deploy-verify:*)
---

# /eval:staging — 测试环境部署与验收

本命令运行 harness 的 `staging_deploy_verify` suite：

1. 调用 `${STAGING_PATH}/` 下的部署脚本完成构建 + 部署（含 bootstrap 幂等逻辑）
2. 调用 `${STAGING_PATH}/` 下的冒烟脚本做 4 项健康检查 + 3 接口冒烟
3. 通过 `python -m agentharness classify-risk-level` 判断本次变更是否 high-risk
4. 产出符合 eval schema 的 JSON 证据到 `.codebuddy/harness-runs/<run-id>/eval/staging_deploy_verify/`

> **消费者项目约定**：`${STAGING_PATH}/` 下的部署和冒烟脚本由消费者项目自行提供，格式不限（`.sh`、`.py`、`.bat` 等均可）。SDK 仅调用，不限定格式。

## 行为规范

- **退出码语义**：0 pass / 1 fail / 2 blocked / 3 pending-human-approval
- **release_allowed** 只在 `status=pass` 时为 true，是 `before_release` 硬门禁
- **high-risk 命中** 时，即使冒烟全绿也会返回 pending-human-approval；需 `/harness:staging-approve` 人工 ack 后复跑才能通过
- **生产隔离**：仅操作 `${STAGING_PATH}/`；生产目录 `${PROD_PATH}/` 只读（cp 源、软链源）

## 执行

!`STAGING_DEPLOY_MODE="${1:-all}" HARNESS_PLAN_PATH="${HARNESS_PLAN_PATH:-}" python -m agentharness eval run-staging-deploy-verify`

## 环境变量

- `STAGING_DEPLOY_MODE`：frontend / backend / all（默认 all）
- `STAGING_SKIP_DEPLOY=true`：跳过实际部署，仅冒烟（回归 / 快速检查用）
- `STAGING_DRY_RUN=true`：只做前置检查，不部署不启动
- `HARNESS_PLAN_PATH`：传入 plan.json 路径供 risk 分级（也会读 git diff）
