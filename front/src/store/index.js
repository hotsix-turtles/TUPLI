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

      if (res.to_user != null) { // 서버 오류시 프로필의 following.length 망가지는 것 방지
        state.following = res.to_user
      }
      if(res.from_user != null) { // 서버 오류시 프로필의 followers.length 망가지는 것 방지
        state.followers = res.from_user
      }
      state.taste = res.taste
    },
    // 유저 설정만 갱신
    GET_USER_SETTING(state, res) {
      state.alarmSetting = res.alarmSetting,
      state.alarmOnRealtime = res.alarmOnRealtime,
      state.alarmOnInvite = res.alarmOnInvite,
      state.inviteDomain = res.inviteDomain,
      state.alarmOnPlayroomMake = res.alarmOnPlayroomMake,
      state.alarmOnBadge = res.alarmOnBadge
    },
    // 프로필 변경
    UPDATE_PROFILE(state, res) {
      // 임시일지 최종일지 확인 필요
      state.nickname = decodeURIComponent(res.nickname)
      state.introduction = decodeURIComponent(res.introduction)
      // OAUTH 고려
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
          console.log('로그인', res)
          commit('TOKEN', res.data.token)
          dispatch('getUserInfo', res.data.token)
          dispatch('getSetting', res.data.token)
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
          dispatch('getSetting', res.data.token)
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
      // console.log('자동로그아웃 chk', localStorage.getItem('jwt'))
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
          commit('GET_USER_INFO', res.data)
        })
        .catch(err => console.log(err.response.data))
    },
    // 사용자 설정 얻기
    getSetting({commit}, token)  {
      axios({
        method: 'GET',
        url: SERVER.URL + '/account/setting',
        headers: {Authorization: token}
      })
        .then((res) => {
          // 설정 정보 갱신
          commit('GET_USER_SETTING', res.data)
        })
        .catch((err) => {
          console.log(err.response.data)
        })
    },
    // 실시간 알람 가져오기 (로그인 등 이후에 호출할 것!)
    getRealtimeAlarm({state, commit}) {
      firebase
        .database()
        .ref('tupli/realtime')  // 기초 버전 : 전부 다 받는 버전
        .limitToLast(20)
        .on('value', (snap) => {
          let res = snap.val()
          const tmp = {}
          tmp.from = res.from
          tmp.fromId = res.fromId
          tmp.img = res.image
          tmp.to = res.to
          tmp.toId = res.toId
          tmp.type = res.type
          tmp.isRead = false
          commit('SET_REALTIME_ALARM', tmp);
        });
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
    mainContent: mainContent,
  },
})
