import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
//home
import Home from '@/views/common/Home'
import Notice from '../views/home/Notice.vue'
//account
import Login from '../views/accounts/Login.vue'
import Signup from '../views/accounts/Signup1.vue'
import Signup2 from '../views/accounts/Signup2.vue'
import Signup3 from '../views/accounts/Signup3.vue'
//profile
import EditProfile from '../views/profile/EditProfile.vue'
import Follow from '../views/profile/Follow.vue'
import PersonFollow from '../views/profile/PersonFollow.vue'
import Profile from '../views/profile/Profile.vue'
import Setting from '../views/profile/Setting.vue'
//setting
import ChangePassword from '../views/profile/setting/ChangePassword.vue'
import History from '../views/profile/setting/History.vue'
import InviteNoticeAlert from '../views/profile/setting/InviteNoticeAlert.vue'
import Like from '../views/profile/setting/Like.vue'
import Payment from '../views/profile/setting/Payment.vue'
import PrivateTerms from '../views/profile/setting/PrivateTerms.vue'
import Save from '../views/profile/setting/Save.vue'
import ServiceTerms from '../views/profile/setting/ServiceTerms.vue'
// playlist
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import PlaylistDetail from '@/views/playlist/PlaylistDetail'
import PlaylistComment from '@/views/playlist/PlaylistComment'
// category
import Category from '@/views/common/Category'
// search
import Search from '@/views/common/Search'
import SearchDetail from '@/views/common/SearchDetail'
// video
import VideoSearch from '../views/video/VideoSearch.vue'
import VideoWatch from '../views/video/VideoWatch.vue'

import PlayroomForm from '../views/playroom/PlayroomForm.vue'
import PlayroomFormPlaylist from '../views/playroom/PlayroomFormPlaylist.vue'
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'

import AuthHandler from '../views/handler/AuthHandler.vue'
import KakaoPaySuccess from '../views/handler/kakaoPay/KakaoPaySuccess.vue'
import KakaoPayCancel from '../views/handler/kakaoPay/KakaoPayCancel.vue'
import KakaoPayFail from '../views/handler/kakaoPay/KakaoPayFail.vue'
import NotFound from '../views/handler/NotFound.vue'



Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  // 홈
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/notice',
    name: 'Notice',
    component: Notice
  },

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
  {
    path: '/playlist/:playlistId/comment', // router.push({ name: 'user', params: { userId: '123' } })
    name: 'PlaylistComment',
    component: PlaylistComment
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
  {
    path: '/search/detail',
    name: 'SearchDetail',
    component: SearchDetail
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
    path: '/editprofile',
    name: 'EditProfile',
    component: EditProfile
  },
  {
    path: '/follow',
    name: 'Follow',
    component: Follow
  },
  {
    path: '/personfollow',
    name: 'PersonFollow',
    component: PersonFollow
  },

  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
    path: '/setting',
    name: 'Setting',
    component: Setting
  },

  //setting
  {
    path: '/changepassword',
    name: 'ChangePassword',
    component: ChangePassword
  },
  {
    path: '/history',
    name: 'History',
    component: History
  },
  {
    path: '/invitenotice',
    name: 'InviteNoticeAlert',
    component: InviteNoticeAlert
  },
  {
    path: '/like',
    name: 'Like',
    component: Like
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment
  },
  {
    path: '/privateterms',
    name: 'PrivateTerms',
    component: PrivateTerms
  },
  {
    path: '/save',
    name: 'Save',
    component: Save
  },
  {
    path: '/serviceterms',
    name: 'ServiceTerms',
    component: ServiceTerms
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
  // 올바르지 못한 주소 404 일괄 처리
  {
    path: "/404",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "*",
    redirect: "/404",
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

