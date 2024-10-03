const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    allowedHosts: 'all',
    proxy: {
      '/api': {
        target: 'http://211.188.52.53:39880',
        changeOrigin: true,
        // pathRewrite: { '^/api': '' }, // 경로 재작성
      },
    },
  },
});
