---
description: 汇总 SPEC、Eval、风险和回滚，生成 Release Summary
argument-hint: "<spec-path> <eval-result-json>"
allowed-tools: Bash(python -m agentharness release-summary:*)
---

运行 release summary：

!`python -m agentharness release-summary $ARGUMENTS`

输出交付摘要，必须包含：

- 变更范围
- Eval 结果
- evidence
- 风险
- 回滚方式
- 是否允许 release
