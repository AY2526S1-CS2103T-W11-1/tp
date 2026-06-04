---
description: 对 high-risk 的 staging_deploy_verify 结果进行人工 ack，解除 pending-human-approval 阻塞
argument-hint: "<run-id> [ack-reason]"
allowed-tools: Bash(printf:*), Bash(tee:*)
---

# /harness:staging-approve — 高风险变更的人工验收确认

当 `/eval:staging` 或 `/harness:eval` 返回 `status=pending-human-approval` 时使用。

## 什么情况需要人工 approve

`python -m agentharness classify-risk-level` 命中以下白名单路径会触发 high-risk：

- 数据库迁移脚本（`*/Migrations/*.sql`）
- MCP schema（`.codebuddy/mcp.json`）
- harness 权限/执行规则
- 生产目录 `${PROD_PATH}/**`
- 生产配置文件（如 appsettings.json / 部署脚本 / nginx.conf / docker-compose）

即使自动冒烟全绿，这些变更也必须有人工复核后才能推到生产。

## 执行

写入 ack 记录到当前 run_id 的 staging-approve.ack 文件：

!`mkdir -p .codebuddy/harness-runs/$1 && printf '%s | approved by: %s\n' "$(date -Iseconds)" "${2:-manual-ack-via-command}" | tee .codebuddy/harness-runs/$1/staging-approve.ack`

## 后续步骤

ack 写入后：

1. 复跑 `/eval:staging` 或 `/harness:eval`，`staging_deploy_verify` 将输出 `status=pass`
2. 然后可以继续 `/harness:release`

## 使用范例

```
/harness:staging-approve 20260510111240-staging "DBA reviewed migration; backup taken"
```
