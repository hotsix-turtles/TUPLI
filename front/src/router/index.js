import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import Category from '@/views/common/Category'
import Search from '@/views/common/Search'
import Login from '../views/accounts/Login.vue'
<<<<<<< .merge_file_1LyCUH
<<<<<<< .merge_file_CuPzi2
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
<<<<<<< HEAD
=======
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
>>>>>>> .merge_file_TUz9MB
=======
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
>>>>>>> .merge_file_BeeeIF
=======
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'
import VideoSearch from '../views/video/VideoSearch.vue'
import VideoWatch from '../views/video/VideoWatch.vue'
import video from '@/store/index.js'
import AuthHandler from '../views/handler/AuthHandler.vue'


Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
  // 구글 소셜로그인
  {
<<<<<<< HEAD
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
    path: '/category',
    name: 'Category',
    component: Category
  },
  {
    path: '/search',
    name: 'Search',
    component: Search
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // },
=======
    path: '/oauth/redirect',
    name: 'AuthHandler',
    component: AuthHandler
  },
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5

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
<<<<<<< .merge_file_1LyCUH
<<<<<<< .merge_file_CuPzi2
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



  //playroom
  {
=======
=======
>>>>>>> .merge_file_BeeeIF
    path: '/playroom/create',
    name: 'PlayroomForm',
    component: PlayroomForm
  },
  {
<<<<<<< .merge_file_1LyCUH
>>>>>>> .merge_file_TUz9MB
=======
>>>>>>> .merge_file_BeeeIF
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
