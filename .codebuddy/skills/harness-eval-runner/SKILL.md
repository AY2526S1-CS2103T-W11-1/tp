---
name: harness-eval-runner
description: |
  解释 Eval 脚本结果、定位 evidence 路径，并在脚本规则无法覆盖时执行轻量 LLM judge。
  默认只读，不修改代码。
allowed-tools:
  - bash
  - read
---

# Harness Eval Runner Skill

> SDK core skill。`harness:init` 默认安装；映射到 `agents/eval-runner.md`。

## 触发场景

- Eval 失败需要解释根因
- 多 suite 结果聚合后需要给出"是否可发布"的判断
- 模糊规则场景（无法用脚本断言时）需要 LLM judge

## 工作流

1. 读取 `aggregated-result.json` / 各 suite JSON
2. 按 `rules/verification-evidence-standard.mdc` 判定证据有效性
3. 若需 LLM judge，仅在「视觉差异 / 文档语义 / 自然语言对齐」类场景启用，**不**用于性能或 schema 类硬指标
4. 输出 `interpret-eval.json` + 简明 markdown 报告

## 边界

- ❌ 不修改测试代码 / baseline / gate
- ❌ 不放宽断言、不删失败用例
- ✅ 可建议 repair 方向（写到 incidents.md）
- ✅ 可识别 evidence 缺失（标 missing 而非降级判定）

## 参考

- `agents/eval-runner.md`
- `rules/eval-gate.mdc`
- `rules/verification-evidence-standard.mdc`
