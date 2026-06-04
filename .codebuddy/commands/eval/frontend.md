---
description: 运行前端构建/类型检查 Eval
argument-hint: "[spec-path]"
allowed-tools: Bash(python -m agentharness eval run-frontend:*)
---

!`python -m agentharness eval run-frontend $ARGUMENTS`

解释 JSON 结果。默认不强制 build；需要真实前端构建时设置 `HARNESS_FRONTEND_BUILD=true`。
