// .eslintrc.js (ctrl+s -> 자동으로 코드 fix)

module.exports = {
  parserOptions: {
    parser: 'babel-eslint',
  },
  extends: [
    'plugin:vue/recommended'
  ],
  plugins: [
    'vuetify'
  ],
  rules: {
    'vuetify/no-deprecated-classes': 'error',
    'vuetify/no-legacy-grid': 'error',
    'indent': ["error", 2]
  },
}

