---
description: 运行安全和敏感路径 Eval
argument-hint: "[spec-path]"
allowed-tools: Bash(python -m agentharness eval run-security:*)
---

!`python -m agentharness eval run-security $ARGUMENTS`

解释 JSON 结果，列出 secret、token、生产配置等风险。
