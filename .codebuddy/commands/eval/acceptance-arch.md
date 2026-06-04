---
description: 架构契约 eval suite（NetArchTest + dependency-cruiser）。hard 门禁。
allowed-tools: Bash(python -m agentharness eval run-acceptance-arch:*)
---

# /eval:acceptance-arch — 架构契约（Fitness Function）

两部分：

- **后端**：`dotnet test --filter Category=architecture`（NetArchTest 规则集）
- **前端**：`npx depcruise --config dependency-cruiser.config.cjs`

## 当前规则示例

| 规则 | acceptance_id | 描述 |
|---|---|---|
| 后端 | `ACC-EXAMPLE-ARCH-001` | Controllers 不得直接引用 `Microsoft.Data.Sqlite` |
| 后端 | `ACC-EXAMPLE-ARCH-002` | Services 不得引用 `Microsoft.AspNetCore.Mvc` |
| 后端 | `ACC-EXAMPLE-ARCH-003` | Models 不得反向引用 Controllers/Services |
| 前端 | `ACC-EXAMPLE-ARCH-FE-001` | `views/` 不得直接 import `axios` |
| 前端 | `ACC-EXAMPLE-ARCH-FE-002` | 禁止循环依赖 |
| 前端 | `ACC-EXAMPLE-ARCH-FE-003` | `views/` 不得引用 `utils/internal/*` |

- **门禁**：hard

## 执行

!`STAGING_DRY_RUN="${STAGING_DRY_RUN:-false}" python -m agentharness eval run-acceptance-arch`

## evidence

- 后端：`arch-test.trx` + `dotnet-test.log`
- 前端：`dep-cruiser.json`
