---
description: 基于需求生成或修订 SPEC
argument-hint: "<需求描述或 SPEC 路径>"
allowed-tools: Read, Glob, Grep, Bash(python -m agentharness kb-search:*), Bash(python -m agentharness spec-check:*), Bash(.codebuddy/scripts/harness/render-project-workbench.py:*)
---

先运行知识复用检查：

!`python -m agentharness kb-search $ARGUMENTS`

然后按 `spec-writer` 标准生成或修订 SPEC。

## 标准 SPEC 生成步骤

1. **知识复用检查**：先执行 `python -m agentharness kb-search`，复用已有 Docs / Rules / Scripts / Evals。
2. **苏格拉底式自检**：围绕目标、范围、方案取舍、风险、验收、展示、发布回滚生成审核问题。
3. **先自答再提问**：能自答的问题放入 `### Agent 已自答的问题` 表格（5 列：ID/问题/Agent 答案/confidence/evidence，ID 格式 `RQ-<MODULE>-AUTO-NNN`）；不能自答的放入 `### 需要你审核的点` 表格（12 列：ID/优先级/背景/问题/选项A/选项A优点/选项A缺点/选项B/选项B优点/选项B缺点/推荐/不回答的影响，ID 格式 `RQ-<MODULE>-NNN`）。**禁止自由 Markdown 格式**——工作台只解析表格行，ID 不以 `RQ-` 开头的行会被跳过。
4. **补足提问信息量**：每个用户审核问题必须填写"背景"和"不回答的影响"列，禁止出现未解释的缩写、编号或内部代号。
5. **展示选项优缺点**：每个审核问题必须提供选项 A（推荐）和选项 B（替代），各填优点和缺点列。
6. **生成"需要你审核的点"**：把 P0/P1/P2 审核问题写入 SPEC §10 的双表格（Agent 自答表 + 用户审核表），不把问题散落在对话里。
7. **父级 heading 防误匹配**：§10 父级 heading 严禁包含 `"需要你审核的点"` 或 `"Agent 已自答的问题"`，应使用中性标题如 `## 10. 审核问题与待确认决策`，否则工作台会把两个子表格全部抓进同一区域。
7. **HTML 控件化审核**：刷新 Artifact Viewer / Workbench，用单选、填空、多选、长文本等控件收集用户回答；回答写入 `.codebuddy/harness-workbench/review-responses/`，不得替代 SPEC 修订或 Plan 重建。
8. **刷新审核页**：SPEC 生成或修订后运行 `python3 .codebuddy/scripts/harness/render-project-workbench.py`，让用户看到图表化和控件化审核视图。

输出必须包含：

- 背景与问题
- 目标与非目标
- 知识复用检查
- 现状分析
- 方案设计
- Plan 输入约束
- 验收标准
- Eval 验证方案
- 风险与回滚
- 需要你审核的点 / 待确认问题
- 参考与代码锚点
