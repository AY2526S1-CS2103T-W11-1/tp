// Acceptance test stub — Playwright
// 用法：
//   pnpm exec playwright test path/to/this.spec.ts
//
// 必须挂 ACC-ID tag，由 acceptance_traceability suite 自动追溯。

import { test, expect } from '@playwright/test';

test.describe('<feature> acceptance', { tag: '@ACC-<MODULE>-001' }, () => {
  test.beforeEach(async ({ page }) => {
    // 公共前置：登录、导航等
  });

  test(
    'T1：<验收点描述>',
    { tag: '@ACC-<MODULE>-001' },
    async ({ page }) => {
      await page.goto('${FRONTEND_URL}/<path>');
      await expect(page.getByRole('heading', { name: '<title>' })).toBeVisible();
      // 关键交互
      await page.getByRole('button', { name: '<action>' }).click();
      // 关键断言
      await expect(page.getByText('<expected>')).toBeVisible();
    }
  );

  test(
    'T2：<另一个验收点>',
    { tag: '@ACC-<MODULE>-002' },
    async ({ request }) => {
      const res = await request.get('${API_BASE}/<endpoint>');
      expect(res.status()).toBe(200);
      const body = await res.json();
      expect(body).toMatchObject({ /* schema */ });
    }
  );
});
