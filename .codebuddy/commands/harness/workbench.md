# /harness:workbench

刷新并打开 Agent Harness 项目级 HTML 工作台。

## 用途

- 全量扫描 `.codebuddy/harness-runs/` 与 `${PROJECT_ROOT}/Docs/**/*.plan.json`。
- 重新生成 `.codebuddy/harness-workbench/project-workbench.snapshot.json`。
- 重新生成 `.codebuddy/harness-workbench/project-workbench.html`。
- 若存在 P0/P1 Review Queue 项，优先打开 `preview.top_review_url` 定位到 `#review-<id>`。

## 执行步骤

```bash
python3 .codebuddy/scripts/harness/render-project-workbench.py
python3 .codebuddy/scripts/harness/serve-workbench.py --port 8787
```

随后使用 CodeBuddy 网页预览打开（服务根目录已设为 `.codebuddy/harness-workbench/`）：

```text
http://127.0.0.1:8787/project-workbench.html
```

或打开 snapshot 中的 `preview.top_review_url`。

## 边界

- `/harness:workbench` 只刷新派生视图，不修改 Plan、ack、release 或 Eval gate。
- HTML 工作台不作为 gate 输入；权威状态仍来自 JSON / JSONL / Markdown evidence。
- Review Response answer 文件只能作为用户输入证据和 Agent 上下文，不得替代 SPEC 修订、Plan 重建、high-risk ack 或 release approve。
- 刷新失败默认只作为 warning，不改变原 harness / eval 结果。
