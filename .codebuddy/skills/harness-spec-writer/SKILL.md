---
name: harness-spec-writer
description: |
  生成或修订高质量、无歧义、可执行、可验证的 SPEC 文档。
  当用户要求新增功能、修复 Bug、重构、升级框架等需要先写 SPEC 的场景时主动加载。
  默认只读，不直接改业务代码。
allowed-tools:
  - bash
  - read
  - edit
---

# Harness SPEC Writer Skill

> SDK core skill。`harness:init` 默认安装；映射到 `agents/spec-writer.md`。

## 触发场景

- 用户提出新功能或重构需求
- 用户要求"写 SPEC"、"先 SPEC 后实现"
- 现有 SPEC 不完整需要修订

## 必备章节

1. 背景与问题
2. 目标与非目标
3. 知识复用检查（先 `python3 -m agentharness kb-search <keyword>`，避免重复造轮子）
4. 现状分析与代码锚点
5. 方案设计
6. Plan 输入约束
7. 可编号验收标准（带 ACC-ID + T1/Tn）
8. Eval 验证方案
9. 风险与回滚
10. 待确认问题（苏格拉底式）

## 苏格拉底式自检

每个待确认点必须给出 `agent_answer` + `confidence` + `evidence`。
仅 `confidence=低` 才作为待确认抛给用户。

## 验收 ID 规约

```
ACC-<MODULE>-<NNN>
```

- MODULE 大写 + 数字
- NNN 三位连续编号
- SPEC 行首：`**[ACC-XXX-NNN]** Tn：标题`
- 测试代码：tag 或注释 `@ACC-XXX-NNN`

## 检查门禁

```bash
python3 -m agentharness spec-check <spec-path>
```

或 IDE 命令：

```
/harness:spec
```

返回 `status: pass` 才能进入 Plan 阶段。

## 参考

- `agents/spec-writer.md`
- `rules/spec-first-development.mdc`
- `rules/acceptance-id-convention.mdc`
- `templates/spec-template.md`
