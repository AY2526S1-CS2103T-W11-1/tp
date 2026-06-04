---
description: 需求追溯 eval suite。扫描 SPEC + 测试 tag，生成覆盖矩阵。hard 门禁。
allowed-tools: Bash(python -m agentharness eval run-acceptance-traceability:*)
---

# /eval:acceptance-traceability — 需求追溯矩阵

工作流：

1. `python -m agentharness extract-acceptance-ids` 从 `Docs/**/*.md` 抽 `[ACC-XXX-NNN]` 标识（剥离代码块/inline code）
2. grep 测试代码 `@ACC-XXX-NNN` tag
3. `python -m agentharness build-traceability-matrix` 生成 `traceability-matrix.md`
4. coverage（active）= 100% → pass；否则 fail

## acceptance_id 规约

详见 `.codebuddy/rules/acceptance-id-convention.mdc`。

- SPEC 标注：`**[ACC-MODULE-NNN]**` 或 `**[ACC-MODULE-NNN] [deferred-until: <reason>]**`
- 测试 tag：Playwright `{ tag: '@ACC-MODULE-NNN' }`、xUnit `[Trait("AcceptanceId","ACC-MODULE-NNN")]`、注释 `// @ACC-MODULE-NNN`
- 覆盖率公式：`covered_active / total_active`

- **门禁**：hard

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" ACC_DOCS_GLOB="${ACC_DOCS_GLOB:-${PROJECT_ROOT}/Docs/**/*.md}" python -m agentharness eval run-acceptance-traceability`

## 环境变量

- `ACC_DOCS_GLOB`：SPEC 扫描 glob（默认全部 Docs）
- `ACC_TESTS_GLOB`：测试代码扫描 glob（默认覆盖前后端 + evals）

## evidence

- `ids.txt`：抽到的 ID 列表（含 active/deferred）
- `traceability-matrix.md`：人读矩阵
- `result.json`：覆盖率统计
