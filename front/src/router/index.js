import Vue from 'vue'
import VueMeta from 'vue-meta'
import VueRouter from 'vue-router'
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import Category from '@/views/common/Category'
import Search from '@/views/common/Search'
import Login from '../views/accounts/Login.vue'
<<<<<<< HEAD
import Signup from '../views/accounts/Signup1.vue'
import Signup2 from '../views/accounts/Signup2.vue'
import Signup3 from '../views/accounts/Signup3.vue'
import ChangePassword from '../views/profile/ChangePassword.vue'
import History from '../views/profile/History.vue'
import Like from '../views/profile/Like.vue'
import Notice from '../views/profile/Notice.vue'
import Payment from '../views/profile/Payment.vue'
import Profile from '../views/profile/Profile.vue'
import Save from '../views/profile/Save.vue'
import Setting from '../views/profile/Setting.vue'
import Terms from '../views/profile/Terms.vue'
=======
import PlayroomForm from '../views/playroom/PlayroomForm.vue'
>>>>>>> d45949f7055bdaa3cdd84c56dc1b1eb8aa25f7cd
import PlayroomDetail from '../views/playroom/PlayroomDetail.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const routes = [
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

  //accounts
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
    path: '/playroom/create',
    name: 'PlayroomForm',
    component: PlayroomForm
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
