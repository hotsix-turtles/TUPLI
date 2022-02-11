import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import account from './modules/account.js'
import playroom from './modules/playroom.js'
import router from '../router/index.js'
import video from './modules/video.js'
import playlist from './modules/playlist.js'

import axios from 'axios'
import SERVER from '@/api/server'
import createPersistedState from "vuex-persistedstate";
import firebase from 'firebase/compat/app';
import 'firebase/compat/database';

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
    // 실시간 알람
    realtimeAlarmList: null,
    realtimeBoolean:false  // 플레이룸 등 특정 상황에서 비활성화
  },
  getters: {
    getRealtimeAlarmList: function(state) {
      return state.realtimeAlarmList;
    }
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
      if (res.profileImage) {
        state.image = SERVER.ROUTES.image + res.profileImage
      } else {
        state.profileImage = null
      }
      state.is_vip = res.is_vip
      state.following = res.to_user
      state.followers = res.from_user
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
    // 가져온 알람 갱신
    SET_REALTIME_ALARM(state, res) {
      state.realtimeAlarmList = res;
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
    // 회원가입
    signup: function (context, credentials) {
      axios({
        method: 'POST',
        url: SERVER.URL + '/account/signup',
        data: {
          email: credentials.email,
          password: credentials.password,
          // username: credentials.username,
          nickname: credentials.nickname,
        }
      })
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
    video: video,
    playlist: playlist,
  },
})
