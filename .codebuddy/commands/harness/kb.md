---
description: 检索、检查或维护项目知识库入口
argument-hint: "check|index|init-code|update-code|code <keyword>|<keyword>"
allowed-tools: Bash(python -m agentharness kb-search:*), Bash(python -m agentharness kb-check:*), Bash(python -m agentharness kb-index:*), Bash(python -m agentharness kb-code-init:*), Bash(python -m agentharness kb-code-update:*), Bash(python -m agentharness kb-code-search:*)
---

根据 `$ARGUMENTS` 执行知识库操作：

- `check`：运行 `!python -m agentharness kb-check`
- `index`：运行 `!python -m agentharness kb-index`
- `init-code`：运行 `!python -m agentharness kb-code-init`
- `update-code`：运行 `!python -m agentharness kb-code-update`
- `code <keyword>`：运行 `!python -m agentharness kb-code-search <keyword>`
- 其他输入：运行 `!python -m agentharness kb-search $ARGUMENTS`

输出脚本 JSON 结果，并说明可复用的 Docs / Code Knowledge / Rules / Commands / Scripts / Evals / MCP 入口。
