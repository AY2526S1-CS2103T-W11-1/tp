// dep-cruiser config — 架构依赖检查
// 用法：
//   pnpm exec depcruise --config path/to/this.js src

module.exports = {
  forbidden: [
    {
      name: 'no-circular',
      severity: 'error',
      comment: '禁止循环依赖',
      from: {},
      to: { circular: true },
    },
    {
      name: 'no-orphans',
      severity: 'warn',
      from: { orphan: true, pathNot: '\\.(test|spec)\\.[jt]sx?$' },
      to: {},
    },
    {
      name: 'controllers-not-call-repositories',
      severity: 'error',
      comment: 'Controllers 不允许直接调用 Repositories（应经 Service 层）',
      from: { path: '(^|/)controllers/' },
      to:   { path: '(^|/)repositories/' },
    },
  ],
  options: {
    doNotFollow: { path: 'node_modules' },
    tsConfig: { fileName: 'tsconfig.json' },
    enhancedResolveOptions: { exportsFields: ['exports'], conditionNames: ['import', 'require', 'node'] },
    reporterOptions: {
      dot: { collapsePattern: 'node_modules/[^/]+' },
      archi: { collapsePattern: '^(packages|src|lib|app|test|node_modules)/[^/]+' },
    },
  },
};
