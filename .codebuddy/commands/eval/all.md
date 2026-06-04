---
description: 聚合运行适用 Eval suites
argument-hint: "<spec-path>"
allowed-tools: Bash(python -m agentharness eval run-all:*)
---

!`python -m agentharness eval run-all $1`

解释聚合 JSON 结果，输出 release gate 结论。
