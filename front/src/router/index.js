import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import PlaylistDetail from '@/views/playlist/PlaylistDetail'
import Category from '@/views/common/Category'
import Search from '@/views/common/Search'
import Login from '../views/accounts/Login.vue'
import Signup from '../views/accounts/Signup1.vue'
import Signup2 from '../views/accounts/Signup2.vue'
import Signup3 from '../views/accounts/Signup3.vue'
import ChangePassword from '../views/profile/ChangePassword.vue'
import EditProfile from '../views/profile/EditProfile.vue'
import History from '../views/profile/History.vue'
import Like from '../views/profile/Like.vue'
import Notice from '../views/profile/Notice.vue'
import Payment from '../views/profile/Payment.vue'
import Profile from '../views/profile/Profile.vue'
import Save from '../views/profile/Save.vue'
import Setting from '../views/profile/Setting.vue'
import Terms from '../views/profile/Terms.vue'
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
import PlayroomFormPlaylist from '../views/playroom/PlayroomFormPlaylist.vue'
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'
import VideoSearch from '../views/video/VideoSearch.vue'
import VideoWatch from '../views/video/VideoWatch.vue'
import video from '@/store/index.js'
import AuthHandler from '../views/handler/AuthHandler.vue'
import KakaoPaySuccess from '../views/handler/kakaoPay/KakaoPaySuccess.vue'
import KakaoPayCancel from '../views/handler/kakaoPay/KakaoPayCancel.vue'
import KakaoPayFail from '../views/handler/kakaoPay/KakaoPayFail.vue'


Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  // 구글 소셜로그인
  {
    path: '/oauth/redirect',
    name: 'AuthHandler',
    component: AuthHandler
  },

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
  {
    path: '/playlist/:playlistId', // router.push({ name: 'user', params: { userId: '123' } })
    name: 'PlaylistDetail',
    component: PlaylistDetail
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
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/signup2',
    name: 'Signup2',
    component: Signup2
  },
  {
    path: '/signup3',
    name: 'Signup3',
    component: Signup3
  },

  //profile
  {
    path: '/changepassword',
    name: 'ChangePassword',
    component: ChangePassword
  },
  {
    path: '/editprofile',
    name: 'EditProfile',
    component: EditProfile
  },
  {
    path: '/history',
    name: 'History',
    component: History
  },
  {
    path: '/like',
    name: 'Like',
    component: Like
  },
  {
    path: '/notice',
    name: 'Notice',
    component: Notice
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
    path: '/save',
    name: 'Save',
    component: Save
  },
  {
    path: '/setting',
    name: 'Setting',
    component: Setting
  },
  {
    path: '/terms',
    name: 'Terms',
    component: Terms
  },

  //handler
  //카카오
  {
    path: '/kakaoPay/success',
    name: 'KakaoPaySuccess',
    component: KakaoPaySuccess
  },
  {
    path: '/kakaoPay/cancel',
    name: 'KakaoPayCancel',
    component: KakaoPayCancel
  },
  {
    path: '/kakaoPay/fail',
    name: 'KakaoPayFail',
    component: KakaoPayFail
  },



  //playroom
  {
    path: '/playroom',
    name: 'PlayroomForm',
    component: PlayroomForm
  },
  {
    path: '/playroom/create/playlist',
    name: 'PlayroomFormPlaylist',
    component: PlayroomFormPlaylist
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
  routes,
  scrollBehavior() {
    return { x: 0, y: 0 }
  },

})

export default router

