---
description: 运行 SQL 性能规则 Eval
argument-hint: "[spec-path]"
allowed-tools: Bash(python -m agentharness eval run-sql:*)
---

!`python -m agentharness eval run-sql $ARGUMENTS`

解释 JSON 结果，重点检查 1 秒阈值、先分页后关联和预计算表规则。
