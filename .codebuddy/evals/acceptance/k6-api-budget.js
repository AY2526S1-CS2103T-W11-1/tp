// k6 API 性能预算（acceptance_perf suite）
//
// @ACC-EXAMPLE-PERF-API-001  GET /api/health p95 < 500ms
// @ACC-EXAMPLE-PERF-API-002  GET /api/assets?page=1&size=50 p95 < 1500ms（对齐"超 1s 才优化"+ 50% 缓冲）
//
// 用法：k6 run k6-api-budget.js
// 环境变量：API_BASE（默认 http://127.0.0.1:5100）
//
// 退出码由 k6 控制：thresholds 全过 = 0；任一未过 = 99

import http from 'k6/http';
import { check, group } from 'k6';

const API_BASE = __ENV.API_BASE || 'http://127.0.0.1:5100';

export const options = {
  scenarios: {
    smoke: {
      executor: 'constant-vus',
      vus: 2,
      duration: '15s'
    }
  },
  thresholds: {
    // @ACC-EXAMPLE-PERF-API-001
    'http_req_duration{endpoint:health}': ['p(95)<500'],
    // @ACC-EXAMPLE-PERF-API-002
    'http_req_duration{endpoint:assets_list}': ['p(95)<1500'],
    'http_req_failed': ['rate<0.01']
  },
  summaryTrendStats: ['avg', 'p(50)', 'p(95)', 'p(99)', 'max']
};

export default function () {
  group('health', function () {
    const r = http.get(`${API_BASE}/api/health`, { tags: { endpoint: 'health' } });
    check(r, { 'health 200': (res) => res.status === 200 });
  });

  group('assets_list', function () {
    const r = http.get(`${API_BASE}/api/assets?page=1&size=50`, { tags: { endpoint: 'assets_list' } });
    check(r, { 'assets_list 200': (res) => res.status === 200 });
  });
}

export function handleSummary(data) {
  // 输出到 stdout（k6 默认） + summary.json（让 shell 抓取）
  return {
    stdout: textSummary(data),
    'summary.json': JSON.stringify(data, null, 2)
  };
}

function textSummary(data) {
  const lines = ['k6 summary'];
  for (const [name, metric] of Object.entries(data.metrics || {})) {
    if (metric.values && metric.values['p(95)'] !== undefined) {
      lines.push(`  ${name}: p95=${metric.values['p(95)'].toFixed(1)}ms p99=${(metric.values['p(99)']||0).toFixed(1)}ms`);
    }
  }
  return lines.join('\n') + '\n';
}
