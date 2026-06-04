---
description: 运行 Harness 轨迹级 Eval，检查 Plan/Execute/Eval 流程是否按约束执行
argument-hint: "<spec-path>"
allowed-tools: Bash(python -m agentharness eval run-trajectory:*)
---

运行 trajectory Eval：

!`python -m agentharness eval run-trajectory $1`

输出：

- `status`
- `release_allowed`
- trajectory warnings / blocking issues
- evidence 路径
