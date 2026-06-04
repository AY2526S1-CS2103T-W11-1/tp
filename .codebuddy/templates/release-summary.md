# Release Summary — <feature>

- **Run ID**：<harness-runs/<id>>
- **发布日期**：<YYYY-MM-DD>
- **版本 / Tag**：<v.x.y.z>
- **Branch / Commit**：<branch> @ <sha>

## 1. 范围

- SPEC：<path>
- Plan：<path>
- 改动文件数：<n>

## 2. Eval 结果

| suite | result | evidence |
|---|---|---|
| spec_alignment | pass | <path> |
| unit | pass | <path> |
| security | pass | <path> |
| regression | pass | <path> |
| acceptance_traceability | pass | <path> |
| staging_deploy_verify | pass | <path> |

- release_allowed = true
- coverage = 100%

## 3. 高风险与人工 ack

- High-risk：yes / no
- ack 文件：<path>

## 4. 验收覆盖

- ACC-<MODULE>-001 ... -NNN 全部 covered

## 5. 回滚预案

- 回滚命令：<>
- 数据回退：<>

## 6. 后续观察

- 监控指标：<>
- 观察期：<n hours>
