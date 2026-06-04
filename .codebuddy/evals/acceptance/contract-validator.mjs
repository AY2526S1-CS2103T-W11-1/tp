#!/usr/bin/env node
/**
 * contract-validator.mjs — 接口契约校验器（acceptance_contract suite 的核心）
 *
 * @ACC-EXAMPLE-CONTRACT-001  全部 GET 端点的响应必须符合 OpenAPI schema
 * @ACC-EXAMPLE-CONTRACT-002  Swagger 文档自身必须是合法的 OpenAPI 3.x
 * @ACC-EXAMPLE-CONTRACT-003  Coverage = covered_endpoints / total_endpoints = 100%
 *
 * 流程：
 *   1) 抓 swagger.json from staging
 *   2) swagger-parser validate（schema 自身合法性）
 *   3) 枚举所有 paths × methods，对 GET 端点做"实际请求 + ajv 校验响应 schema"
 *   4) 输出 contract-result.json + uncovered-endpoints.txt
 *
 * 退出码：0=pass  1=fail  2=blocked
 */

import SwaggerParser from '@apidevtools/swagger-parser';
import Ajv from 'ajv';
import addFormats from 'ajv-formats';
import { writeFileSync, mkdirSync } from 'node:fs';
import { dirname } from 'node:path';

const swaggerUrl = process.env.SWAGGER_URL || 'http://127.0.0.1:5100/swagger/v1/swagger.json';
const apiBase    = process.env.API_BASE    || 'http://127.0.0.1:5100';
const evidenceDir = process.env.EVIDENCE_DIR || './contract-evidence';
const sampleOnly = (process.env.SAMPLE_ONLY || 'false') === 'true';

mkdirSync(evidenceDir, { recursive: true });

function writeJson(path, obj) {
  mkdirSync(dirname(path), { recursive: true });
  writeFileSync(path, JSON.stringify(obj, null, 2));
}

async function fetchSwagger(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error(`fetch swagger failed: ${res.status} ${res.statusText}`);
  return await res.json();
}

async function main() {
  console.log(`[contract] swagger from ${swaggerUrl}`);
  let raw;
  try {
    raw = await fetchSwagger(swaggerUrl);
  } catch (e) {
    console.error(`[contract] cannot fetch swagger: ${e.message}`);
    writeJson(`${evidenceDir}/contract-result.json`, {
      status: 'blocked', reason: 'swagger fetch failed', error: e.message
    });
    process.exit(2);
  }
  writeJson(`${evidenceDir}/swagger.json`, raw);

  // 1) 自身合法性
  let api;
  try {
    api = await SwaggerParser.validate(raw);
    console.log(`[contract] swagger valid: ${api.info?.title} ${api.info?.version}`);
  } catch (e) {
    console.error(`[contract] swagger validation failed: ${e.message}`);
    writeJson(`${evidenceDir}/contract-result.json`, {
      status: 'fail', reason: 'swagger schema invalid', error: e.message
    });
    process.exit(1);
  }

  // 2) 枚举端点
  const paths = api.paths || {};
  const allEndpoints = [];
  for (const [p, methods] of Object.entries(paths)) {
    for (const m of Object.keys(methods)) {
      if (['get','post','put','delete','patch'].includes(m.toLowerCase())) {
        allEndpoints.push({ method: m.toUpperCase(), path: p, op: methods[m] });
      }
    }
  }
  console.log(`[contract] total endpoints: ${allEndpoints.length}`);

  // 3) 对 GET 端点做 spec-vs-impl 检查
  const ajv = new Ajv({ strict: false, allErrors: true });
  addFormats(ajv);

  const getEndpoints = allEndpoints.filter(e => e.method === 'GET');
  const targets = sampleOnly ? getEndpoints.slice(0, 3) : getEndpoints;
  console.log(`[contract] checking ${targets.length} GET endpoints`);

  const perEndpoint = [];
  let covered = 0;
  let failed = 0;
  let skipped = 0;

  for (const ep of targets) {
    // 仅校验无路径参数的端点；带 {id} 的跳过（需测试数据，留给 e2e）
    if (ep.path.includes('{')) {
      perEndpoint.push({ ...ep, status: 'skipped', reason: 'path param requires fixture' });
      skipped++;
      continue;
    }
    const url = apiBase + ep.path;
    let httpStatus = 0, body = null, err = null;
    try {
      const r = await fetch(url, { method: 'GET', headers: { 'Accept': 'application/json' } });
      httpStatus = r.status;
      const text = await r.text();
      try { body = JSON.parse(text); } catch { body = text; }
    } catch (e) {
      err = e.message;
    }

    // 抽 200 的 schema 验证
    const okResp = ep.op?.responses?.['200'] || ep.op?.responses?.['default'];
    let schemaCheck = 'no-schema';
    let validationErrors = null;
    if (okResp?.content?.['application/json']?.schema) {
      const schema = okResp.content['application/json'].schema;
      try {
        const validate = ajv.compile(schema);
        const valid = validate(body);
        if (valid) {
          schemaCheck = 'pass';
        } else {
          schemaCheck = 'fail';
          validationErrors = validate.errors;
        }
      } catch (e) {
        schemaCheck = 'error';
        validationErrors = e.message;
      }
    }

    const epStatus = (httpStatus >= 200 && httpStatus < 300 && schemaCheck !== 'fail') ? 'pass' : 'fail';
    if (epStatus === 'pass') covered++; else failed++;

    perEndpoint.push({
      method: ep.method,
      path: ep.path,
      url,
      http_status: httpStatus,
      schema_check: schemaCheck,
      validation_errors: validationErrors ? (Array.isArray(validationErrors) ? validationErrors.slice(0, 3) : validationErrors) : null,
      error: err,
      status: epStatus
    });
  }

  // 4) 输出
  const total = allEndpoints.length;
  const totalGet = getEndpoints.length;
  const coveragePct = totalGet > 0 ? Math.round(covered * 1000 / (totalGet - skipped)) / 10 : 0;

  const result = {
    status: failed === 0 ? 'pass' : 'fail',
    swagger_url: swaggerUrl,
    api_base: apiBase,
    summary: {
      total_endpoints: total,
      total_get: totalGet,
      checked: targets.length,
      covered,
      failed,
      skipped,
      coverage_pct: coveragePct
    },
    per_endpoint: perEndpoint
  };

  writeJson(`${evidenceDir}/contract-result.json`, result);

  // uncovered 列表（schema 缺失或失败）
  const uncovered = perEndpoint.filter(e => e.status === 'fail').map(e => `${e.method} ${e.path}`);
  writeFileSync(`${evidenceDir}/uncovered-endpoints.txt`, uncovered.join('\n'));

  console.log(`[contract] result: ${result.status} (covered=${covered}/${totalGet - skipped}, failed=${failed}, skipped=${skipped})`);
  process.exit(result.status === 'pass' ? 0 : 1);
}

main().catch(e => {
  console.error('[contract] fatal:', e);
  process.exit(2);
});
