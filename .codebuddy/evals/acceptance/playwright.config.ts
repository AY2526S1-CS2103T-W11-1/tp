/**
 * Playwright 配置 — acceptance_ui 和 acceptance_e2e suite 共用
 *
 * @ACC-EXAMPLE-UI-CONFIG-001  baseURL 指向 staging 前端 8080
 * @ACC-EXAMPLE-UI-CONFIG-002  仅 chromium，节省磁盘与跑时
 * @ACC-EXAMPLE-UI-CONFIG-003  trace/video 仅失败时保留，避免 evidence 体量爆炸
 *
 * 用法：
 *   cd ${PROJECT_ROOT}/frontend
 *   npx playwright test --config ../../../../../.codebuddy/evals/acceptance/playwright.config.ts
 */
import { defineConfig, devices } from '@playwright/test';

const STAGING_BASE = process.env.PLAYWRIGHT_BASE_URL || 'http://127.0.0.1:8080';
const EVIDENCE_DIR = process.env.PLAYWRIGHT_EVIDENCE_DIR || './playwright-evidence';

export default defineConfig({
  testDir: '../../../${PROJECT_ROOT}/frontend/tests/acceptance',
  testMatch: ['**/*.spec.ts', '**/*.spec.js'],
  fullyParallel: false,                  // suite 内串行（避免共享 staging 环境互相干扰）
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  reporter: [
    ['list'],
    ['json', { outputFile: `${EVIDENCE_DIR}/playwright-report.json` }],
    ['html',  { outputFolder: `${EVIDENCE_DIR}/playwright-html`, open: 'never' }]
  ],
  outputDir: `${EVIDENCE_DIR}/test-results`,

  // baseline 落 .codebuddy/evals/baselines/acceptance-ui/
  snapshotPathTemplate: '{testDir}/../../../../../../.codebuddy/evals/baselines/acceptance-ui/{testFilePath}/{arg}{ext}',

  use: {
    baseURL: STAGING_BASE,
    trace: 'retain-on-failure',
    video: 'retain-on-failure',
    screenshot: 'only-on-failure',
    actionTimeout: 10_000,
    navigationTimeout: 20_000
  },

  expect: {
    toHaveScreenshot: {
      maxDiffPixelRatio: 0.02,
      animations: 'disabled',
      caret: 'hide'
    }
  },

  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] }
    }
  ]
});
