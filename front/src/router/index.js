import Vue from 'vue'
import VueRouter from 'vue-router'
import PlaylistForm from '@/views/playlist/PlaylistForm'
import PlaylistFormVideo from '@/views/playlist/PlaylistFormVideo'
import Category from '@/views/Category'
import Search from '@/views/Search'

Vue.use(VueRouter)

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
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
