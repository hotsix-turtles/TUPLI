module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port:80
  },
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "@/assets/scss/variables.scss";`,
      },
    },
  },
}
