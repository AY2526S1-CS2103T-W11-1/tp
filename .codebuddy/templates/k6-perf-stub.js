// k6 perf stub — @ACC-<MODULE>-PERF-001
// 用法：
//   k6 run path/to/this.js
//
// 阈值：p95 < 1500ms（按 SPEC 性能预算调整）

import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  thresholds: {
    http_req_duration: ['p(95)<1500'],
    http_req_failed: ['rate<0.01'],
  },
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m',  target: 20 },
    { duration: '30s', target: 0 },
  ],
};

const BASE = __ENV.API_BASE || 'http://localhost:${BACKEND_PORT}';

export default function () {
  const res = http.get(`${BASE}/<endpoint>`);
  check(res, {
    'status is 200': (r) => r.status === 200,
    'has body': (r) => r.body && r.body.length > 0,
  });
  sleep(1);
}
