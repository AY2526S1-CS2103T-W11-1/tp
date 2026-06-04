module.exports = {
  ci: {
    collect: {
      url: ['http://127.0.0.1:8080/'],
      numberOfRuns: 1,
      settings: {
        chromeFlags: '--no-sandbox --headless=new --disable-gpu',
        preset: 'desktop'
      }
    },
    assert: {
      // 首版宽松，与 perf-budget.json 一致
      assertions: {
        'categories:performance':     ['warn',  { minScore: 0.6 }],
        'categories:accessibility':   ['error', { minScore: 0.8 }],
        'categories:best-practices':  ['warn',  { minScore: 0.8 }],
        'categories:seo':             ['warn',  { minScore: 0.7 }],
        'first-contentful-paint':     ['warn',  { maxNumericValue: 3000 }],
        'largest-contentful-paint':   ['warn',  { maxNumericValue: 4000 }],
        'cumulative-layout-shift':    ['warn',  { maxNumericValue: 0.1 }],
        'total-blocking-time':        ['warn',  { maxNumericValue: 600 }]
      }
    },
    upload: {
      target: 'filesystem',
      outputDir: './lhci-report'
    }
  }
};
