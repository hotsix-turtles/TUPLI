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
        additionalData: `@import "@/assets/scss/variables.scss";`,
      },
    },
  },
}
