---
status: draft
owner: <author>
created: <YYYY-MM-DD>
updated: <YYYY-MM-DD>
acceptance_module: <MODULE>
---

# <功能名> SPEC

## 1. 背景与问题

<现状、痛点、触发动机>

## 2. 目标与非目标

- 目标：
  - <目标 1>
- 非目标：
  - <显式排除的需求>

## 3. 知识复用检查

- 已复用：<现有 SPEC / Rules / Skills / Scripts>
- 不复用原因：<列出未复用项与说明>

## 4. 现状分析与代码锚点

- 关键文件：`<path>:<line-range>`
- 核心数据结构：<结构 / schema>
- 现有调用链：<sequence>

## 5. 方案设计

### 5.1 高层设计

<架构图 / 流程图（可文本表示）>

### 5.2 详细方案

<逐项说明，含 API 契约、数据结构、状态机、并发模型>

### 5.3 失败 & 回滚

<故障场景、降级策略、回滚步骤>

## 6. Plan 输入约束

- 文件改动范围：<files>
- 不变量：<不能破坏的约束>
- 必须并行的任务：<list>

## 7. 可编号验收标准

- **[ACC-<MODULE>-001]** T1：<验收点>
- **[ACC-<MODULE>-002]** T2：<验收点>
- **[ACC-<MODULE>-003]** T3：<验收点>

## 8. Eval 验证方案

| 验收 ID | suite | 验证方式 | evidence |
|---|---|---|---|
| ACC-<MODULE>-001 | unit | <command> | <path> |
| ACC-<MODULE>-002 | api | <command> | <path> |

## 9. 风险与回滚

- 风险：<列出>
- 回滚：<步骤>

## 10. 待确认问题

- [待确认] <问题 1>

## 11. 变更记录

| 日期 | 变更摘要 | 作者 |
|---|---|---|
| <YYYY-MM-DD> | 初稿 | <author> |
