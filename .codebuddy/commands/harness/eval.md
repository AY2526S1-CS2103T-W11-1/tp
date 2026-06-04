---
description: 对 SPEC 对应实现运行统一 Eval 收口
argument-hint: "<spec-path>"
allowed-tools: Bash(python -m agentharness eval run-all:*)
---

运行统一 Eval：

!`python -m agentharness eval run-all $1`

解释 JSON 结果，输出：

- `status`
- `release_allowed`
- suites 结果
- blocking issues
- warnings
- evidence 路径
- rerun_required

`release_allowed=false` 时不得交付。
