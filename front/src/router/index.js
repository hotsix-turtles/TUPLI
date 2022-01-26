import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/accounts/Login.vue'
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // },
  {
    path: '/login',
    name: 'Login',
    component: Login
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
