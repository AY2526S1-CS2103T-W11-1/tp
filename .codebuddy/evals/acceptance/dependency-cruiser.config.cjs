/**
 * dependency-cruiser 配置 — 前端架构契约（Fitness Function）
 *
 * @ACC-EXAMPLE-ARCH-FE-001  views/ 不得直接 import axios（必须经 api/ 或 services/ 封装）
 * @ACC-EXAMPLE-ARCH-FE-002  禁止循环依赖
 * @ACC-EXAMPLE-ARCH-FE-003  utils/internal/* 不得被 views/ 直接引用
 *
 * 用法：cd frontend && npx depcruise --config <path-to-this> src
 */
module.exports = {
  forbidden: [
    {
      // @ACC-EXAMPLE-ARCH-FE-001
      name: 'no-axios-in-views',
      severity: 'error',
      comment: 'Views must not import axios directly; use api/ or services/ wrappers.',
      from: { path: '^src/views' },
      to: { path: 'node_modules/axios' }
    },
    {
      // @ACC-EXAMPLE-ARCH-FE-002
      name: 'no-circular',
      severity: 'error',
      comment: 'Circular dependencies are forbidden.',
      from: {},
      to: { circular: true }
    },
    {
      // @ACC-EXAMPLE-ARCH-FE-003
      name: 'no-views-to-utils-internal',
      severity: 'error',
      comment: 'Views must not import from utils/internal/* (private helpers).',
      from: { path: '^src/views' },
      to: { path: '^src/utils/internal' }
    },
    {
      name: 'no-orphans',
      severity: 'warn',
      comment: 'Orphan modules waste maintenance budget; consider removing.',
      from: {
        orphan: true,
        pathNot: [
          '(^|/)\\.[^/]+\\.(js|cjs|mjs|ts)$',
          '\\.d\\.ts$',
          '(^|/)tsconfig\\.json$',
          '(^|/)vite-env\\.d\\.ts$',
          '(^|/)main\\.ts$'
        ]
      },
      to: {}
    }
  ],
  options: {
    doNotFollow: { path: 'node_modules' },
    tsConfig: { fileName: 'tsconfig.json' },
    enhancedResolveOptions: {
      exportsFields: ['exports'],
      conditionNames: ['import', 'require', 'node', 'default']
    },
    reporterOptions: {
      text: { highlightFocused: true },
      json: {}
    }
  }
};
