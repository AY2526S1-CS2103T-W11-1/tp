# Code Review — <SPEC/Plan name>

- **Reviewer**：<reviewer>
- **日期**：<YYYY-MM-DD>
- **范围**：<files / commits>

## 1. SPEC 对齐

- [ ] 实现是否完全覆盖 SPEC 验收项？
- [ ] 是否扩范围？是否经审批？

## 2. 设计

- 模块边界：<>
- 接口契约：<>
- 错误处理：<>

## 3. 实现细节

| 类别 | 检查项 | 结果 |
|---|---|---|
| 命名 | 一致性 / 语义 | OK / Issue |
| 类型 | 边界 / null / 异常 | OK / Issue |
| 并发 | 锁 / 顺序 / 死锁 | OK / Issue |
| 性能 | N+1 / 内存 / 复杂度 | OK / Issue |
| 安全 | 注入 / 越权 / 敏感泄露 | OK / Issue |
| 测试 | 单元 / 边界 / 回归 | OK / Issue |

## 4. 阻塞问题

- [ ] <issue 1>

## 5. 非阻塞建议

- <suggestion>

## 6. 结论

- approve / request-changes / blocked
