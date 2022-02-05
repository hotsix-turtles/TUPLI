import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import account from './modules/account.js'
import playroom from './modules/playroom.js'
import router from '../router/index.js'
import video from './modules/video.js'
import playlist from './modules/playlist.js'

import axiosConnector from '../utils/axios-connector.js'
import createPersistedState from "vuex-persistedstate";

export default new Vuex.Store({
  // 새로고침, 외부진입시에도 state 정보 온존 
  plugins: [createPersistedState()],
  //plugins: [createPersistedState({storage: window.sessionStorage})], // 창 종료시 state 초기화 하는 타입
  // data
  state: {
    authToken: localStorage.getItem('jwt'),
    isLogin: false,
    userInfo: null
  },
  modules: {
    playroom: playroom,
    account: account,
    video: video,
    playlist: playlist,
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
      axiosConnector.post('/account/login', { email: credentials.email, password: credentials.password })
      // axios({
      //   method: 'post',
      //   url: 'https://i6a102.p.ssafy.io/api/v1' + '/account/login',
      //   data: {
      //     email: credentials.email,
      //     password: credentials.password,
      //   }
      // })
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
    signup: function (context, credentials) {
      // console.log(context, credentials)
      console.log(credentials)
      axiosConnector.post('/account/signup', {
        email: credentials.email,
        password: credentials.password,
        username: credentials.username,
        nickname: credentials.nickname,
      })
      // axios({
      //   method: 'post',
      //   url: 'https://i6a102.p.ssafy.io/api/v1' + '/account/signup',
      //   data: {
      //     email: credentials.email,
      //     password: credentials.password,
      //     username: credentials.nickname,
      //   }
      // })
        .then((res) => {
          console.log(res)
          console.log('signup success')
          router.push( { name: 'Login' })
        })
        .catch((err) => {
          console.log('signup fail')
          console.log(err)
        })
    },

    // 프로필
    // getProfile: function ({ commit }) {
    //   axios({
    //     method: 'get',
    //     url: 'i6a102.p.ssafy.io/api/v1' + '/profile',
    //     headers: state.authToken
    //   })
    //     .then((res) => {
    //       commit('PROFILE', res.data.userInfo)
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // }
  },


})
