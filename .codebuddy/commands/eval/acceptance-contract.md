---
description: 接口契约 eval suite（Swagger + ajv schema 校验）。hard 门禁。
allowed-tools: Bash(python -m agentharness eval run-acceptance-contract:*)
---

# /eval:acceptance-contract — 接口契约

基于 staging 后端 Swagger（`/swagger/v1/swagger.json`）+ Node 自研校验器（swagger-parser + ajv）。

- **门禁**：hard
- **校验内容**：
  1. Swagger 文档自身合法（OpenAPI 3.x）
  2. 全部 GET 端点（无 path param 者）实际请求 + 响应 schema 匹配
  3. Coverage 输出（`covered/total`）
- **不在范围**：POST/PUT/DELETE 端点（需 fixture）、带 `{id}` 的端点（需测试数据，由 e2e 覆盖）

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" CONTRACT_SAMPLE_ONLY="${CONTRACT_SAMPLE_ONLY:-false}" python -m agentharness eval run-acceptance-contract`

## 环境变量

- `SWAGGER_URL`（默认 `http://127.0.0.1:5100/swagger/v1/swagger.json`）
- `API_BASE`（默认 `http://127.0.0.1:5100`）
- `CONTRACT_SAMPLE_ONLY=true`：仅抽 3 个端点（CI 烟雾）
- `STAGING_DRY_RUN=true`：仅前置检查

## evidence

- `swagger.json` 快照、`contract-result.json`、`uncovered-endpoints.txt`
