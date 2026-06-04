# Workbench Vendor 资源

本目录存放 artifact-viewer 渲染依赖的第三方 JS 库。所有资源**已下载到本地**，由 `serve-workbench.py` 作为静态文件提供，不通过外部 CDN 加载（满足 `.codebuddy/rules/html-output-policy.mdc §3` "禁止外部 CDN/远程 JS"）。

## 资源清单

| 文件 | 版本 | 用途 | 上游 | License | sha256 |
|---|---|---|---|---|---|
| `marked.min.js` | 12.0.2 | Markdown 解析器（CommonMark + GFM 表格） | https://github.com/markedjs/marked | MIT | `15fabce5b65898b32b03f5ed25e9f891a729ad4c0d6d877110a7744aa847a894` |
| `purify.min.js` | 3.4.2 | HTML sanitizer（防 XSS） | https://github.com/cure53/DOMPurify | Apache-2.0 / MPL-2.0 | `ef9a98b5b21aac33c73e316ef21f5cf06f68eff003a40ac953022129112cff3c` |
| `mermaid.min.js` | 11.14.0 | Mermaid 图表渲染（flowchart/sequence/class 等） | https://github.com/mermaid-js/mermaid | MIT | `217b66ef4279c33c141b4afe22effad10a91c02558dc70917be2c0981e78ed87` |

## 升级流程

```bash
cd "$WORKSPACE_ROOT"
curl -sSL -o .codebuddy/harness-workbench/vendor/marked.min.js https://cdn.jsdelivr.net/npm/marked@<NEW>/marked.min.js
curl -sSL -o .codebuddy/harness-workbench/vendor/purify.min.js https://cdn.jsdelivr.net/npm/dompurify@<NEW>/dist/purify.min.js
curl -sSL -o .codebuddy/harness-workbench/vendor/mermaid.min.js https://cdn.jsdelivr.net/npm/mermaid@<NEW>/dist/mermaid.min.js
sha256sum .codebuddy/harness-workbench/vendor/*.js   # 更新本表
```

## 调用约定

artifact-viewer.html 内：

```html
<script src="/.codebuddy/harness-workbench/vendor/marked.min.js"></script>
<script src="/.codebuddy/harness-workbench/vendor/purify.min.js"></script>
<script src="/.codebuddy/harness-workbench/vendor/mermaid.min.js"></script>
<script>
  const dirty = marked.parse(text, { gfm: true, breaks: false });
  const clean = DOMPurify.sanitize(dirty, { USE_PROFILES: { html: true } });
  contentEl.innerHTML = clean;

  const mermaidLib = window.mermaid || window.__esbuild_esm_mermaid_nm?.mermaid;
  const mermaid = mermaidLib?.default || mermaidLib;
  mermaid.initialize({ startOnLoad: false, securityLevel: 'strict' });
  // 内容插入 DOM 后，将 pre > code.language-mermaid 渲染为 SVG。
</script>
```

## 安全约束

- DOMPurify 默认配置已禁 `<script>` / `<iframe>` / `on*` 事件 / `javascript:` 协议
- marked 仅用于 `.md`/`.markdown` 文件，且渲染输出**必须**先经 DOMPurify 清洗才挂到 DOM
- Mermaid 必须使用 `startOnLoad: false` 手动渲染动态内容，且 `securityLevel` 保持 `strict`
- Mermaid 优先配置 `htmlLabels: false`，避免节点文字依赖 `<foreignObject>`；若上游仍生成 `<foreignObject>`，必须经 DOMPurify 的 HTML+SVG profile 清洗后挂载
- Mermaid 生成的 SVG 挂载前必须再次经 DOMPurify HTML+SVG profile 清洗，避免 SVG-only profile 误删节点文字
- 任何 SPEC 引用此目录文件必须明确"本地静态资源、无网络回连"
