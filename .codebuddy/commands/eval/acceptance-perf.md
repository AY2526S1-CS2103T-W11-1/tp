---
description: 性能预算 eval suite（k6 + Lighthouse CI）。soft 门禁。
allowed-tools: Bash(python -m agentharness eval run-acceptance-perf:*)
---

# /eval:acceptance-perf — 性能预算

两部分：

- **A. API 性能**：k6 跑 `k6-api-budget.js`，断言 p95 thresholds
- **B. 前端性能**：Lighthouse CI 跑 `http://127.0.0.1:8080/`，断言 Web Vitals

- **门禁**：soft（fail 仅记，不阻断 release；待 baseline 稳定再升 hard）
- **预算**：见 `.codebuddy/evals/acceptance/perf-budget.json`（首版宽松：API p95 < 1500ms / LH perf ≥ 60）

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" python -m agentharness eval run-acceptance-perf`

## 环境变量

- `PERF_SKIP_API=true`：仅跑前端 LH
- `PERF_SKIP_FRONTEND=true`：仅跑 k6
- `API_BASE` / `FRONTEND_URL`：默认 staging 5100/8080
- `STAGING_DRY_RUN=true`：仅前置检查

## evidence

- `k6-summary.json`、`k6.log`、`lhci-report/`、`lhci.log`
