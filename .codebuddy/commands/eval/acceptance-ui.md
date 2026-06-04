---
description: UI 视觉回归 eval suite（Playwright `toHaveScreenshot()`）。soft 门禁。
allowed-tools: Bash(python -m agentharness eval run-acceptance-ui:*)
---

# /eval:acceptance-ui — UI 视觉回归

基于 Playwright `expect(page).toHaveScreenshot()` 的视觉回归。

- **门禁**：soft（fail 仅记 evidence，不阻断 release）
- **baseline**：`.codebuddy/evals/baselines/acceptance-ui/`
- **首次跑**：自动写 baseline，exit 0
- **后续跑**：PNG diff，超阈值 (`maxDiffPixelRatio: 0.02`) → fail

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" python -m agentharness eval run-acceptance-ui`

## 环境变量

- `UI_UPDATE_BASELINE=true`：显式更新 baseline（触发 high-risk classify）
- `FRONTEND_URL`：staging 前端地址（默认 `http://127.0.0.1:8080`）
- `STAGING_DRY_RUN=true`：仅前置检查
