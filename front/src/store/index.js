import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

import account from './modules/account.js'
import playroom from './modules/playroom.js'
import router from '../router/index.js'

export default new Vuex.Store({
  // data
  state: {
    authToken: localStorage.getItem('jwt'),
    isLogin: false,
    userInfo: null
  },
  modules: {
    playroom: playroom,
    account: account,
  },
  mutations: {
    // 로그인
    TOKEN: function (state, token) {
      state.authToken = token
      localStorage.setItem('jwt', token)
    },
    // 로그아웃
    DELETE_TOKEN: function (state) {
      localStorage.removeItem('jwt')
      state.authToken = null
    },
    // 회원정보
    PROFILE: function (state, userInfo) {
      state.userInfo
    }

  },
  actions: {
    // 로그인
    login: function ({ commit, dispatch }, credentials) {
      axios({
        method: 'post',
        url: 'http://api.tupli.kr/v1' + '/account/login',
        data: credentials,
      })
        .then((res) => {
          commit('TOKEN', res.data.token)
          this.state.isLogin = true
          router.push({ name: 'Profile' })
          dispatch('getUserId')
          dispatch('setUserInfo')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 로그아웃
    logout: function ({ commit }) {
      commit('DELETE_TOKEN')
      this.state.isLogin = false
      router.push({ name: 'Login' })
    },
    // 회원가입
    signup: function (context, credentails) {
      axios({
        method: 'post',
        url: 'http://api.tupli.kr/v1' + '/account/signup',
        data: credentails,
      })
        .then(() => {
          console.log('signup')
          router.push( { name: 'Login' })
        })
        .catch((err) => {
          console.log(err)
        })
    },

    // 프로필
    getProfile: function ({ commit }) {
      axios({
        method: 'get',
        url: 'http://api.tupli.kr/v1' + '/profile',
        headers: state.authToken
      })
        .then((res) => {
          commit('PROFILE', res.data.userInfo)
        })
        .catch((err) => {
          console.log(err)
        })
    }
  },
})
