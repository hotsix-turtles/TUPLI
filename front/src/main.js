import Vue from 'vue'
import App from './App.vue'
import VueMeta from 'vue-meta'
import store from './store'
import router from './router'
import vuetify from './plugins/vuetify'
import firebase from 'firebase/compat/app';
import {firebaseConfig} from './config/firebase'

Vue.config.productionTip = false

firebase.initializeApp(firebaseConfig)

Vue.use(VueMeta)

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
