---
status: draft
acceptance_module: HARNESSFLOW
owner: SDK Fixture
flow:
  type: iteration
  priority: P1
  id: harness-flow-fixture-iteration
---

# Iteration Flow Fixture SPEC

> 用途：给 `python3 -m agentharness eval tests test-harness-flow-model` / `python3 -m agentharness eval tests test-iteration-plan-readiness` /
> `python3 -m agentharness eval tests test-harness-process-artifact-dir` 提供主 SPEC 锚点。
> 本 fixture 不含任何消费者项目专属字面量。

## 1. 背景
最小可运行的"迭代流程"SPEC，仅供 SDK 自闭环测试。

## 2. 非目标
- 不覆盖真实业务能力。
- 不参与消费者项目 traceability matrix。

## 3. 当前无阻塞 P0 问题
本 fixture 不存在阻塞性 P0 待澄清问题。

## 4. Eval 验证方案
- 对应 plan：`evals/fixtures/specs/iteration-flow/iteration-flow.fixture.plan.json`
- 对应 flow sidecar：`evals/fixtures/specs/iteration-flow/iteration-flow.fixture.flow.json`
- regression dataset：N/A（fixture 不参与回归）

## 5. 实现策略
仅落盘静态 fixture，无可执行行为。

## 6. 实现说明
SDK 测试脚本通过 `HARNESS_TEST_FIXTURE_SPEC` env 指向本文件。

## 7. 可编号验收标准
- **[ACC-HARNESSFLOW-001]** T1：fixture SPEC 可被 harness_flow.py 解析为 iteration 类型
- **[ACC-HARNESSFLOW-002]** T2：fixture 含 ≥15 个 ACC-ID
- **[ACC-HARNESSFLOW-003]** T3：fixture 含 plan/flow/review-notes/freeze-review/run-id 5 个过程文件
- **[ACC-HARNESSFLOW-004]** T4：process-dir flow metadata 被 sidecar 解析
- **[ACC-HARNESSFLOW-005]** T5：plan binding 默认走 process artifact 目录
- **[ACC-HARNESSFLOW-006]** T6：legacy plan/flow 路径不再共存
- **[ACC-HARNESSFLOW-007]** T7：plan-check 通过
- **[ACC-HARNESSFLOW-008]** T8：harness_flow --artifact-path plan 解析正确
- **[ACC-HARNESSFLOW-009]** T9：harness_flow --artifact-path flow 解析正确
- **[ACC-HARNESSFLOW-010]** T10：harness_flow --artifact-path review-notes 解析正确
- **[ACC-HARNESSFLOW-011]** T11：harness_flow --artifact-path freeze-review 解析正确
- **[ACC-HARNESSFLOW-012]** T12：Eval regression 覆盖过程产物目录布局
- **[ACC-HARNESSFLOW-013]** T13：harness_flow --artifact-path run-id 解析正确
- **[ACC-HARNESSFLOW-014]** T14：plan_path 含过程目录段
- **[ACC-HARNESSFLOW-015]** T15：flow_metadata_path 指向过程目录内 sidecar
