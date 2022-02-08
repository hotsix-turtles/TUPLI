import Vue from 'vue'
import App from './App.vue'
import VueMeta from 'vue-meta'
import store from './store'
import router from './router'
import vuetify from './plugins/vuetify'
import firebase from 'firebase/compat/app';
// import {firebaseConfig} from './config/firebase'

Vue.config.productionTip = false

var firebaseConfig = {
  apiKey: process.env.FIREBASE_APIKEY,
  authDomain: process.env.FIREBASE_AUTHDOMAIN,
  databaseURL: process.env.FIREBASE_DATABASEURL,
  projectId: process.env.FIREBASE_PROJECTID,
  storageBucket: process.env.FIREBASE_STORAGEBUCKET,
  messagingSenderId: process.env.FIREBASE_MESSAGING_SENDERID,
  appId: process.env.FIREBASE_APPID,
  measurementId: process.env.FIREBASE_MEASUREMENTID,
};

firebase.initializeApp(firebaseConfig)

Vue.use(VueMeta)

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
