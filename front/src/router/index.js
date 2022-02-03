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
import VideoWatch from '../views/video/VideoWatch.vue'
import video from '@/store/index.js'

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
    component: VideoSearch,
    // beforeEnter: (to, from, next) => {
    //   if (from.path != '/video/watch') {
    //     console.log('video.dispatch(resetVideoSearchState)')
    //     video.dispatch('resetVideoSearchState')
    //   }
    //   next()
    // }
  },
  {
    path: '/video/watch',
    name: 'VideoWatch',
    component: VideoWatch
  },
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
