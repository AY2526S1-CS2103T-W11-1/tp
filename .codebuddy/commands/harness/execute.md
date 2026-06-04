---
description: 按已落盘 Plan 执行一个可执行 todo
argument-hint: "<spec-path> <plan-path> [todo-id]"
allowed-tools: Read, Edit, Write, Bash(python -m agentharness spec-check:*), Bash(python -m agentharness plan-check:*), Bash(python -m agentharness permission-check:*), Bash(python -m agentharness assert-plan-binding:*), Bash(python -m agentharness check-plan-mode:*)
---

<!-- [@ACC-HARNESS-009] python -m agentharness check-plan-mode 登记为 allowed-tool 用于 Execute 前置检查 -->

执行前必须完成五道门禁，任一失败都不得修改业务代码：

!`python -m agentharness spec-check $1`

!`python -m agentharness plan-check $1 $2`

!`python -m agentharness permission-check`

!`python -m agentharness assert-plan-binding $1 $2`

!`python -m agentharness check-plan-mode $1`

门禁通过后，读取 `$2` 中的 Plan JSON，并严格按以下规则执行：

1. 如果提供 `$3`，只执行该 `todo-id`；否则只执行第一个 `status=pending` 且所有 `dependencies` 已 `completed` 的 todo。
2. 不得跳过依赖，不得把完整 Plan 简化成临时 todo，不得新增超出 SPEC/Plan 的工作项。
3. 执行前把选中 todo 标记为 `in_progress`；执行完成后按验证结果标记为 `completed` 或保留 `pending` 并说明阻塞原因。
4. 每次只允许一个 todo 为 `in_progress`。
5. 只改该 todo 的 `content` / `spec_refs` 所覆盖范围内文件；发现 SPEC 或 Plan 错误时停止实现，回到 `/harness:spec` 或 `/harness:plan` 修订。
6. 完成 todo 后必须输出：changed files、执行了哪些 verification、证据路径或命令输出、未完成项、风险。
7. 不得交付；全部实现完成后仍必须运行 `/harness:eval <spec-path>`。

> **长任务建议直接用 `/harness:run`**：本命令一次只推一个 todo，长任务请用 `/harness:run <spec-path>` 自动连推。

