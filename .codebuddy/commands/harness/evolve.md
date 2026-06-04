---
description: 受控 harness 自进化入口，只生成/验证 proposal，不自动合入
argument-hint: "analyze|propose|validate|compare|promote|rollback <args>"
allowed-tools: Bash(python -m agentharness evolve:*)
---

根据 `$ARGUMENTS` 选择自进化动作：

- `analyze <eval-result> [trace]`
- `propose <analysis-json>`
- `validate <proposal-dir>`
- `compare <baseline> <candidate>`
- `promote <proposal-dir>`：默认要求人工审批且不自动合入
- `rollback <proposal-dir>`

禁止降低 Eval gate、删除失败样例、放宽权限或修改生产配置。
