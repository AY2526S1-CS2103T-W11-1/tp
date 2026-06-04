---
description: 从已确认 SPEC 生成并落盘 Plan
argument-hint: "<spec-path> [plan-path]"
allowed-tools: Read, Write, Bash(python -m agentharness spec-check:*), Bash(python -m agentharness plan-check:*), Bash(python -m agentharness check-plan-mode:*)
---

## 工具可用性前置检查（[@ACC-HARNESS-008]）

本命令依赖 IDE `plan_create` 工具完成"双登记"（IDE plan 卡片 + `*.plan.json`）。若本轮会话工具列表不含 `plan_create`（用户未处于 plan mode），**不得**只写 `*.plan.json` 绕过 IDE 登记——这违反 `harness-execution-protocol.mdc §15` 的三条禁止降级动作（A/B/C）。

正确做法：先运行 check-plan-mode 获取标准停机话术，原样输出给用户，等用户切到 plan mode 后再继续。

```bash
python -m agentharness check-plan-mode "$1" --force-require
```

若脚本 `exit=2`（`status=requires_plan_mode`），读取输出 JSON 的 `halt_message` 字段**原样**输出给用户（禁止改写话术），然后停止本命令。

若 `exit=0` 或工具列表含 `plan_create`，继续下面流程。

## 先检查 SPEC 门禁：

!`python -m agentharness spec-check $1`

如果通过，生成 CodeBuddy Plan JSON，并写入 `$2` 指定路径；如果 `$2` 为空，默认写入与 SPEC 同目录的 `<spec文件名>.plan.json`。

每个 todo 必须包含：

- `id`
- `content`
- `dependencies`
- `spec_refs`
- `verification`
- `status`

生成并写入 Plan 文件后，必须立即用实际 plan path 运行 Plan 门禁：

```bash
python -m agentharness plan-check <spec-path> <generated-plan-path>
```

门禁通过后，只输出 plan path、todo 数量、首个可执行 todo、warnings。不得扩大 SPEC 范围，不得只把 Plan 留在聊天消息中；不得把新增 Plan、flow metadata、review notes、run-id 平铺到主 SPEC 同级目录。

## 双登记硬约束（[@ACC-HARNESS-016] [@ACC-HARNESS-018]）

调用 IDE `plan_create` 后，**同一回合**内必须完成：

1. `write_to_file` 落盘 `*.plan.json` 到 SPEC 推算路径（或显式 `<plan-path>` 参数）
2. 运行 `python -m agentharness plan-check <spec> <plan>` 通过

**禁止**只调 `plan_create` 而不落盘 `*.plan.json`——这是 `harness-execution-protocol.mdc §15.2` 的禁止降级动作 D，事后会被 `python -m agentharness plan-check` 的 ACC-HARNESS-016 frontmatter 一致性校验与 `python -m agentharness assert-plan-binding` 的 ACC-HARNESS-017 pre-flight 双重阻断。

SPEC frontmatter `status` **不要手动改**——进入 `/harness:run` 后由状态机自动推进（见 §16）。
