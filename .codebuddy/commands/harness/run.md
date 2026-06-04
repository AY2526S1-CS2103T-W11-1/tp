---
description: 自动执行 Plan → Execute → Eval → Repair 循环，适合长任务
argument-hint: "<spec-path> [plan-path] [max-repair-rounds]"
allowed-tools: Read, Edit, Write, Bash(python -m agentharness run-loop:*), Bash(python -m agentharness spec-check:*), Bash(python -m agentharness plan-check:*), Bash(python -m agentharness plan-next:*), Bash(python -m agentharness plan-update:*), Bash(python -m agentharness run-verification:*), Bash(python -m agentharness classify-failure:*), Bash(python -m agentharness capture-regression:*), Bash(python -m agentharness eval-to-repair:*), Bash(python -m agentharness permission-check:*), Bash(python -m agentharness check-plan-mode:*), Bash(python -m agentharness assert-plan-binding:*), Bash(python -m agentharness eval run-all:*)
---

这是 Standard / High-risk 长任务的默认入口。不要在每个 todo 或每轮 Eval 后等待用户输入“继续”；必须自动推进，直到 `release_allowed=true`、达到修复轮次上限，或触及高风险边界。

## 入口参数

- `$1`：`spec-path`，必填。
- `$2`：`plan-path`，可选；为空时默认使用 SPEC 同目录 `<spec文件名>.plan.json`。
- `$3`：`max-repair-rounds`，可选；默认 `3`。

## 状态机入口

**Pre-flight 守卫（[@ACC-HARNESS-017]）**：`python -m agentharness run-loop` 启动后会自动执行 `python -m agentharness assert-plan-binding`，阻断"未走双登记直接跑 run"的场景。无需 Agent 手工调用，但若 pre-flight 返回 `status=blocked` → 必须立即停止并按 §15 切 plan mode。

先调用状态机脚本获取下一步动作：

```bash
python -m agentharness run-loop <spec-path> [plan-path] [max-repair-rounds]
```

根据 JSON 的 `action` 自动处理：

### `action=generate_plan`

**前置工具可用性检查（[@ACC-HARNESS-007]）**：在调 IDE `plan_create` 之前，必须确认本轮工具列表含 `plan_create`。若不含：

```bash
python -m agentharness check-plan-mode <spec-path> --force-require
```

`exit=2` 时读取输出 JSON 的 `halt_message` 字段**原样输出给用户**（禁止改写），停止自动循环等用户切 plan mode 后再重新进入 `/harness:run`。禁止降级为"只写 `*.plan.json` 跳过 IDE 登记"——违反 `harness-execution-protocol.mdc §15` 三条禁止动作。

若 `plan_create` 可用，继续：

1. 根据 SPEC 生成 Plan JSON。
2. 写入 JSON 中的 `plan_path`。
3. 运行 `python -m agentharness plan-check <spec-path> <plan-path>`。
4. 继续调用 `python -m agentharness run-loop`，不要停下来等用户。

### `action=execute_todo`

1. 只执行 JSON 中的 `todo`。
2. 只改该 todo 的 `content` / `spec_refs` / `verification` 覆盖范围内文件。
3. 可用 `python -m agentharness run-verification <plan-path> <todo-id>` 解析 verification；执行其中 `type=command` 的可自动化项，记录 `type=manual` 为待验收证据。
4. 成功后运行：

```bash
python -m agentharness run-loop <spec-path> <plan-path> <max-repair-rounds> complete <todo-id> "verification passed"
```

5. 失败后运行：

```bash
python -m agentharness run-loop <spec-path> <plan-path> <max-repair-rounds> fail <todo-id> "failure reason"
```

6. 然后继续调用 `python -m agentharness run-loop`，不要停下来等用户。

### `action=repair_added`

`python -m agentharness run-loop` 已经把 Eval 失败转换为 `REPAIR-*` todo 并追加到同一 Plan。继续调用 `python -m agentharness run-loop` 自动执行 repair todo。

### `action=done`

`release_allowed=true`。输出最终摘要，并提示可以运行 `/harness:release`。

### `status=blocked`

停止自动循环，输出 blocking issues、evidence、run_dir、plan_path 和下一步建议。

## 硬性边界

- 不得跳过 Plan 依赖。
- 不得把完整 Plan 简化为临时 todo。
- 不得把新生成的 Plan、flow metadata、review notes、freeze review、run-id 平铺在主 SPEC 同级；必须使用 SPEC 专属过程目录，除非显式处理历史 fallback。
- 不得删除失败测试、降低断言、放宽安全/SQL/MCP/Eval 门禁。
- 不得修改 `.env*`、`.codebuddy/mcp.json`、真实 token/secret/key、生产配置。
- 涉及部署、数据库写入、远程更新、生产变更时必须停止并要求人工确认。
- 发现 SPEC 错误时停止实现，先回到 `/harness:spec` 修订 SPEC。

## 输出要求

最终输出必须包含：

- `plan_path`
- completed / pending / repair todo 列表
- changed files
- Eval 结果与 evidence 路径
- repair 轮次与结论
- `release_allowed`
- 若失败，给出明确 blocking issues 和下一步动作
