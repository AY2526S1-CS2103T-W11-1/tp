---
description: E2E 用户旅程 eval suite（Playwright）。hard 门禁，进 before_release.require。
allowed-tools: Bash(python -m agentharness eval run-acceptance-e2e:*)
---

# /eval:acceptance-e2e — E2E 用户旅程

基于 Playwright 的关键 user journey 自动化。

- **门禁**：hard（任一 user journey fail → release blocked）
- **示例用例**：首页加载、列表 API 联通、健康检查端点
- **acceptance_id**：以 `@ACC-EXAMPLE-E2E-NNN` 起步；业务侧扩展时按 `@ACC-<MODULE>-NNN`

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" python -m agentharness eval run-acceptance-e2e`

## 环境变量

- `FRONTEND_URL`（默认 `http://127.0.0.1:8080`）
- `API_BASE`（默认 `http://127.0.0.1:5100`）
- `STAGING_DRY_RUN=true`：仅前置检查

## evidence

- `playwright-report.json`、`playwright-html/`、`trace.zip`（仅失败时）
