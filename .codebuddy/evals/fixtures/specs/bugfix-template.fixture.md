---
status: draft
acceptance_module: HARNSDKFIX
flow:
  type: bugfix
  priority: P2
bug:
  reproduce_steps:
    - "执行 python3 -m agentharness export-review-annotations"
    - "确认 review-notes.md 生成"
  root_cause: "fixture-only placeholder"
  regression_case: "fixture-regression-case"
---

# Bug 修复单模板（Fixture）

> 用途：给 `python3 -m agentharness eval tests test-harness-flow-model` 提供 `复现步骤` 与 `regression case` 锚点。

## 复现步骤
1. 创建 fixture
2. 调用脚本

## 修复策略
N/A — fixture only

## regression case
`fixture-regression-case`

## 7. 可编号验收标准
- **[ACC-HARNSDKFIX-010]** T1：bugfix 模板含「复现步骤」与「regression case」锚点
