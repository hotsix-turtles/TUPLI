import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import Category from '@/views/common/Category'
import Search from '@/views/common/Search'
import Login from '../views/accounts/Login.vue'
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'
import VideoSearch from '../views/video/VideoSearch.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  // 플레이리스트
  {
    path: '/playlist/create',
    name: 'PlaylistForm',
    component: PlaylistForm
  },
  {
    path: '/playlist/create/video',
    name: 'PlaylistFormVideo',
    component: PlaylistFormVideo
  },
  // 둘러보기
  {
    path: '/category',
    name: 'Category',
    component: Category
  },
  // 검색
  {
    path: '/search',
    name: 'Search',
    component: Search
  },
  // 동영상
  {
    path: '/video/search',
    name: 'VideoSearch',
    component: VideoSearch
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
    name: 'PlayroomForm',
    component: PlayroomForm
  },
  {
    path: '/playroom/:id',
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
