import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import account from './modules/account.js'
import alert from './modules/alert.js'
import playroom from './modules/playroom.js'
import router from '../router/index.js'
import video from './modules/video.js'
import playlist from './modules/playlist.js'
import common from './modules/common.js'
import board from './modules/board.js'
import mainContent from './modules/mainContent.js'

import axios from 'axios'
import SERVER from '@/api/server'
import createPersistedState from "vuex-persistedstate";
import axiosConnector from '@/utils/axios-connector.js'

export default new Vuex.Store({
  // TODO: createPersistedState 사용시 사용 모듈 한정 필요 (playroom, playlist등엔 사용 x)

  // 새로고침, 외부진입시에도 state 정보 온존
  plugins: [createPersistedState()],
  //plugins: [createPersistedState({storage: window.sessionStorage})], // 창 종료시 state 초기화 하는 타입

  // data
  state: {
    authToken: null,
    isLogin: false,
    // userInfo: null
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
      state.following = null
      state.followers = null
      state.taste = null
    },
    // 유저 정보 갱신
    GET_USER_INFO(state, res) {
      state.userId = res.userSeq
      state.email = res.email
      state.nickname = res.nickname
      state.introduction = res.introduction
      if (res.profileImage) {
        state.image = SERVER.ROUTES.image + res.profileImage
      } else {
        state.profileImage = null
      }
      state.is_vip = res.is_vip
      state.following = res.to_user
      state.followers = res.from_user
      state.taste = res.taste
    },
    // 프로필 변경
    UPDATE_PROFILE(state, res) {
      console.log('프로필 변경', res)
      state.nickname = res.nickname
      state.introduction = res.introduction
      if (res.image) {
        image = SERVER.ROUTES.image + res.image
      } else {
        image = state.image
      }
    },

  },
  actions: {
    // 로그인
    login: function ({ commit, dispatch }, credentials) {
      axios({
        method: 'POST',
        url: SERVER.URL + '/account/login',
        data: {
          email: credentials.email,
          password: credentials.password,
        }
      })
        .then((res) => {
          commit('TOKEN', res.data.token)
          dispatch('getUserInfo', res.data.token)
          router.push({ name: 'Home' })
        })
        .catch((err) => {
          console.log(err.response.data)
        })
    },
    // 로그인 (회원가입, 자동로그인 등을 위한 라우터 이동 없는 버전)
    loginHere: function ({ commit, dispatch }, credentials) {
      axios({
        method: 'POST',
        url: SERVER.URL + '/account/login',
        data: {
          email: credentials.email,
          password: credentials.password,
        }
      })
        .then((res) => {
          commit('TOKEN', res.data.token)
          dispatch('getUserInfo', res.data.token)
        })
        .catch((err) => {
          console.log(err.response.data)
        })
    },
    // 로그아웃
    logout: function ({ commit }) {
      commit('DELETE_TOKEN')
      // router.push({ name: 'Login' })
    },
    // jwt 토큰 유효 여부 + 자동 로그아웃
    checkLogin({commit}, state) {
      console.log('자동로그아웃 chk', localStorage.getItem('jwt'))
      if (localStorage.getItem('jwt') === null || localStorage.getItem('jwt') === undefined) {
        // 토큰 없음
        commit('DELETE_TOKEN')
      }
      else {
        axios({
          method: 'GET',
          url: SERVER.URL + '/account/tokenvalidate',
          headers: {Authorization: localStorage.getItem('jwt')}
        })
          .then(
            // 딱히 하는거 없음
            console.log('토큰 유효함')
          )
          .catch(() => {
            // 토큰 유효기간 종료 >> 일단 자동 로그 아웃 이거 명시 해야되나...
            commit('DELETE_TOKEN')
            window.location.reload();
          })
      }
      // commit('LOGIN')
    },
    // 회원가입
    signup: function (context, credentials) {
      axiosConnector.post('/account/signup', { email: credentials.email, password: credentials.password, nickname: credentials.nickname })
      // axios({
      //   method: 'POST',
      //   url: SERVER.URL + '/account/signup',
      //   data: {
      //     email: credentials.email,
      //     password: credentials.password,
      //     // username: credentials.username,
      //     nickname: credentials.nickname,
      //   }
      // })
        .then((res) => {
          // 회원가입시 자동 로그인까지 하고 signup 3으로 보내기 (강민구)
          this.dispatch('loginHere', credentials)
          router.push( { name: 'Signup3' })
        })
        .catch((err) => {
          console.log('signup fail')
          console.log(err.response.data)
        })
    },
    // 사용자 정보 얻기
    getUserInfo({commit}, token) {
      axios({
        method: 'GET',
        url: SERVER.URL + '/account/userInfo',
        headers: {Authorization: token}
      })
        .then(res => {
          console.log(res.data)
          commit('GET_USER_INFO', res.data)
        })
        .catch(err => console.log(err.response.data))
    },
  },
  modules: {
    playroom: playroom,
    account: account,
    alert: alert,
    video: video,
    playlist: playlist,
    common: common,
    board: board,
<<<<<<< HEAD
=======
    mainContent: mainContent,
>>>>>>> 7ada2335ac142236e33d00a4898807f60852f91f
  },
})
