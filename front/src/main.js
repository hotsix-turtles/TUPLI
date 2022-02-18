import Vue from 'vue'
import App from './App.vue'
import VueMeta from 'vue-meta'
import store from './store'
import router from './router'
import vuetify from './plugins/vuetify'
import firebase from 'firebase/compat/app';
// import {firebaseConfig} from './config/firebase'
import AlertDialog from "@/components/common/AlertDialog.vue"

Vue.config.productionTip = false

Vue.component('alert-dialog', AlertDialog);

Kakao.init(process.env.VUE_APP_KAKAO_API_KEY)

var firebaseConfig = {
  apiKey: process.env.VUE_APP_FIREBASE_APIKEY,
  authDomain: process.env.VUE_APP_FIREBASE_AUTHDOMAIN,
  databaseURL: process.env.VUE_APP_FIREBASE_DATABASEURL,
  projectId: process.env.VUE_APP_FIREBASE_PROJECTID,
  storageBucket: process.env.VUE_APP_FIREBASE_STORAGEBUCKET,
  messagingSenderId: process.env.VUE_APP_FIREBASE_MESSAGING_SENDERID,
  appId: process.env.VUE_APP_FIREBASE_APPID,
  measurementId: process.env.VUE_APP_FIREBASE_MEASUREMENTID,
};

firebase.initializeApp(firebaseConfig)

Vue.use(VueMeta)

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
