---
description: 检查实现是否符合 SPEC 基础门禁
argument-hint: "<spec-path>"
allowed-tools: Bash(python -m agentharness eval run-spec-alignment:*)
---

!`python -m agentharness eval run-spec-alignment $1`

解释 JSON 结果，列出阻塞项和 evidence。
