---
status: draft
acceptance_module: PLANMODEGUARD
flow:
  type: iteration
  priority: P1
---

# Plan Mode Guard Fixture SPEC

> 用途：给 `python3 -m agentharness grep-halt-message-consistency` 提供"标准停机话术 §11"的 grep 锚点。
> 本 fixture **必须**与 `rules/harness-execution-protocol.mdc §15.3`、`python3 -m agentharness check-plan-mode`
> 三处保持逐字一致。

## 11. 附录：标准停机话术（权威版本）

下面是当 `plan_create` 工具不可用时，Agent 必须原样输出给用户的话术：

```
🛑 已停机：需要切换到 plan mode

触发原因：<REASON>
当前障碍：plan_create 工具在本轮会话不可用

需要你做：切换到 plan mode（IDE 右下角 mode 切换，或重新发一条消息时选 plan）
切换后：直接发"继续"或本次原始需求，我会自动 plan_create + 落盘 *.plan.json + 过 plan-check，然后请你 approve

升级方案草案（如已准备好）：见对话上文
```

## 7. 可编号验收标准
- **[ACC-PLANMODEGUARD-001]** T1：三处停机话术 grep 锚点一致
- **[ACC-PLANMODEGUARD-002]** T2：fixture 与 rule/script 同源更新
