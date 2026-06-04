# Staging Verified — <feature>

- **Run ID**：<harness-runs/<id>>
- **日期**：<YYYY-MM-DD>
- **Staging URL**：<https://staging.example.com>
- **High-risk**：yes / no
- **Human ack**：required / not-required（高风险必须 yes）

## 1. 部署摘要

- Commit：<sha>
- Diff 范围：<files>
- 部署命令：<command>
- 部署日志：<path>

## 2. 冒烟结果

| 项 | 结果 | evidence |
|---|---|---|
| 健康检查 | pass / fail | <path> |
| 关键 API | pass / fail | <path> |
| 前端首屏 | pass / fail | <path> |

## 3. 性能 / 安全

- p95：<ms>
- 错误率：<%>
- 安全扫描：<path>

## 4. 结论

- staging_verify_passed = true / false
- release_allowed_after_ack = true / false
