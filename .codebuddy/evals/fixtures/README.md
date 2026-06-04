# SDK Fixture 目录

> 自闭环说明：本目录提供 SDK 内置最小可运行 fixture，确保 `python3 -m agentharness eval tests`
> 与检查类脚本在**完全无消费者项目**的
> 干净环境（如 `/tmp` 全新目录）下也能跑通。

## 结构

```
evals/fixtures/
├── README.md                                          ← 本文件
├── specs/
│   ├── minimal.fixture.md                             ← 最小通用 SPEC（init dry-run 用）
│   ├── plan-mode-guard.fixture.md                     ← 含三处停机话术锚点
│   ├── iteration-flow.fixture.md                      ← 主 SPEC：iteration 流程模型 + 15+ ACC-ID
│   ├── bugfix-template.fixture.md                     ← Bug 修复单模板（含「复现步骤」「regression case」）
│   └── inline-review-annotation.fixture.md            ← review-annotation export 测试占位
├── specs/iteration-flow.fixture/                     ← 过程产物子目录（与 SPEC 同名 stem）
│   ├── README.md
│   ├── iteration-flow.fixture.plan.json
│   ├── iteration-flow.fixture.flow.json
│   ├── iteration-flow.fixture.md.review-notes.md
│   ├── iteration-flow.fixture.freeze-review.md
│   └── iteration-flow.fixture.plan.json.run-id
└── docs-reorg.map.example.txt                        ← 通用归类示例（无项目专属字面量）
```

## 使用

### 在消费者项目内运行

eval test 默认使用消费者项目实际 SPEC（通过 `harness-project/harness.project.yaml` 解析）。
如需切换到 fixture，设置环境变量：

```bash
export HARNESS_TEST_FIXTURES_ROOT=".codebuddy/harness-sdk/evals/fixtures"
python3 -m agentharness eval tests test-harness-flow-model
```

### 在 SDK 自身冒烟（/tmp 全新目录）

`python3 -m agentharness sdk-fixture-smoke` 默认会自动指向本目录的 fixture，无需手工配置。

## ACC-ID 标注

本目录所有 fixture SPEC 使用 `ACC-HARNESSFLOW-001..015` 与 `ACC-HARNSDKFIX-001..` 命名空间，
仅用于满足检查脚本的 grep 锚点，不进入消费者项目的真实 traceability matrix。

## 维护契约

1. fixture 内**严禁**出现 `skumanager` / `AssetManagerWeb` / `AgentHarness升级` 等消费者项目专属字面量
2. fixture 修改时必须同步检查 `python3 -m agentharness eval tests` 的 assert 是否仍能通过
3. fixture 不得引入运行态产物（如 `harness-runs/`、`*.run-id` 之外的 evidence）
