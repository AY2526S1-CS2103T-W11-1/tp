---
status: draft
acceptance_module: HARNSDKFIX
flow:
  type: iteration
  priority: P2
---

# Minimal Fixture SPEC

最小通用 SPEC，仅供 SDK init dry-run / smoke 测试使用，不含任何项目专属内容。

## 1. 背景
SDK 自闭环冒烟测试场景。

## 2. 目标与非目标
- 目标：验证 init / scan-only / dir-check 在无消费者项目时仍能成功。
- 非目标：不验证生产能力。

## 7. 可编号验收标准
- **[ACC-HARNSDKFIX-001]** T1：harness-init dry-run 无报错
- **[ACC-HARNSDKFIX-002]** T2：sanitizer scan-only 0 命中
- **[ACC-HARNSDKFIX-003]** T3：check-sdk-dir 通过
