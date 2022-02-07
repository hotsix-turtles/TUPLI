module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port:3000
  },
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "@/scss/variables.scss";`,
      },
    },
  },
}
