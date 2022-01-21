module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  css: {
    loaderOptions: {
      sass: {
        additionalData: `
          //sass 미사용
          //@import "@/assets/scss/variables.sass"
        `
      },
      scss: {
        additionalData: `
          @import "@/assets/scss/variables.scss";
        `
      }
    }
  }
}
