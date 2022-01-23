import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/playroom',
    name: 'PlayroomDetail',
    component: PlayroomDetail
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
