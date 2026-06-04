---
name: harness-workbench
description: |
  AgentHarness 项目级 HTML 工作台说明，覆盖 Project Workbench、Artifact Viewer、
  Review Queue 与受控 review response 边界。HTML 仅作为派生人类审查视图，
  不得作为 gate 输入或配置权威源。
allowed-tools:
  - bash
  - read
---

# Harness Workbench Skill

> SDK core skill。`harness:init` 默认安装。

## 触发场景

- 用户要求查看任务进度、Review Queue、Eval/Traceability/Release evidence
- 用户要求刷新或重新生成 workbench HTML
- 出现 `/harness:workbench` 命令

## 标准操作

```bash
# 刷新派生快照与 HTML
python3 -m agentharness workbench render

# 启动本地服务器（含 review response API）
python3 -m agentharness workbench serve --port 8787
```

## 边界（强制）

| 用途 | 是否允许 |
|---|---|
| 任务总览 / Review Queue 展示 | ✅ |
| 受控 review response 写入 | ✅ |
| 解析 HTML 作为 gate 输入 | ❌ |
| HTML 取代 traceability-matrix.json | ❌ |
| HTML 内嵌 high-risk ack 写入 | ❌ |

## 必备权威源

- `*.plan.json`（Plan 权威）
- `run-state.json`（执行进度）
- `eval-result.json`（Eval 权威）
- `traceability-matrix.md/json`（追溯权威）
- `release-summary.md`（发布权威）

HTML 由这些权威源派生，可重建。

## 参考

- `rules/html-output-policy.mdc`
- `scripts/harness/render-project-workbench.py`
- `scripts/harness/serve-workbench.py`
