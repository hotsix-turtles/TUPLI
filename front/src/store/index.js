import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import account from './modules/account.js'
import playroom from './modules/playroom.js'
import router from '../router/index.js'
import video from './modules/video.js'
import playlist from './modules/playlist.js'

import axios from 'axios'
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
  // 모듈에서 쓸 JWT
  getters: {
    config: function (state) {
      return {
        Authorization: `JWT ${state.authToken}`
      }
    },
  },
  mutations: {
    // 로그인
    TOKEN: function (state, token) {
      localStorage.setItem('jwt', token)
      state.isLogin = true
      state.authToken = token
    },
    // 로그아웃
    DELETE_TOKEN: function (state) {
      state.isLogin = false
      localStorage.removeItem('jwt')
      state.authToken = null
      state.userId = null
      state.email = null
      state.nickname = null
      state.introduction = null
      state.image = null
      state.is_vip = null
    },
    // 유저 정보 갱신
    GET_USER_INFO(state, res) {
      state.userId = res.userSeq
      state.email = res.email
      state.nickname = res.nickname
      state.introduction = res.introduction
      if (res.profileImageUrl) {
        state.image = 'https://storage.cloud.google.com/tupli_profile' + res.profileImageUrl
      } else {
        state.profileImageUrl = null
      }
      state.is_vip = res.is_vip
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
        .then((res) => {
          commit('TOKEN', res.data.token)
          router.push({ name: 'Home' })
          // router.push({ name: 'Profile' })  // 자동 로그인, 가입후 바로 로그인 쓰일 수 있으니 store에서 해주지 말아주시고 login view라던가 필요한 상황에서 해주세요(강민구)
          dispatch('getUserInfo')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 로그아웃
    logout: function ({ commit }) {
      commit('DELETE_TOKEN')
      // router.push({ name: 'Login' })
    },
    // 회원가입
    signup: function (context, credentials) {
      // const formData = new FormData()
      // formData.append('email', credentials.email)
      // formData.append('password', credentials.password)
      // formData.append('passwordCheck', credentials.passwordCheck)
      // formData.append('nickname', credentials.nickname)
      // formData.append('username', credentials.username)
      // console.log('토큰있는상태로 이거 하면 또 토큰 보내서 사고 터지는거 아님?', formData) (강민구<< 로그아웃 버튼 만들고 다시 실험해주세요)
      // axiosConnector.post('/account/signup', credentials)
      axios({
        method: 'post',
        url: 'https://i6a102.p.ssafy.io/api/v1' + '/account/signup',
        data: {
          email: credentials.email,
          password: credentials.password,
          username: credentials.username,
        }
      })
        .then((res) => {
          // 회원가입시 자동 로그인까지 하고 signup 3으로 보내기 (강민구)
          this.dispatch('login', credentials)
          router.push( { name: 'Signup3' })
        })
        .catch((err) => {
          console.log('signup fail')
          console.log(err)
        })
    },
    // 사용자 정보 얻기
    getUserInfo({commit}) {
      axiosConnector.get('/account/userInfo')
        .then(res => {
          commit('GET_USER_INFO', res.data)
        })
        .catch(err => console.log(err))
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
  modules: {
    playroom: playroom,
    account: account,
    video: video,
    playlist: playlist,
  },
})

