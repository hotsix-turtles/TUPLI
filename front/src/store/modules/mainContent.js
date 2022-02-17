import Vue from 'vue'
import axiosConnector from '@/utils/axios-connector'

const mainContent = {
  namespaced: true,
  state: {
    // 메인 컨텐츠
    mainContents: [],

    // 좋아요
    isLikedMain: false,

    // 플레이룸 재생 여부
    mainPlayrooms: [],

  },
  mutations: {
    // 메인 컨텐츠 얻기
    GET_MAIN_CONTENT: function (state, mainContents) {
      // console.log('메인 컨텐츠 얻기', state.mainContents)
      state.mainContents = mainContents
    },

    // *** 좋아요 ***
    // 메인 컨텐츠 좋아요 여부 조회 GET
    IS_LIKED_MAIN: function (state, isLikedMain) {
      console.log('LIKED_main', isLikedMain)
      if (isLikedMain === "ok") {
        state.isLikedMain = true
      } else {
        state.isLikedMain = false
      }
    },
    // 메인 컨텐츠 좋아요 / 좋아요 취소 POST
    LIKE_CONTENT: function (state) {
      state.isLikedMain = true
    },
    UNLIKE_CONTENT: function (state) {
      state.isLikedMain = false
    },

    // 재생 중 여부 파악
    GET_MAIN_PLAYROOMS: function (state, playrooms) {
      let today = new Date()
      playrooms.forEach((playroom) => {
        if (playroom.startTime <= today && playroom.endTime >= today) {
          playroom.onPlay = true
        } else {
          playroom.onPlay = false
        }
        playroom.playTime = playtimeConverter(playroom.startTime, playroom.endTime)
      })
      state.mainPlayrooms = playrooms
      console.log(state.myPlayrooms)
    },


  },
  actions: {
    getMainContent: function ({ commit }) {
      console.log('getMainContent')
      axiosConnector.get(`home/all/`,
      ).then((res) => {
        commit('GET_MAIN_CONTENT', res.data)
      })
        .catch((err) => {
          console.log(err)
        })
    },
    isLikedMain: function ({ commit }, playlistId) {
      console.log('isLiked', mainId)
      axiosConnector.get(`home/playlist/`,
      ).then((res) => {
        console.log('isLiked', res)
        commit('IS_LIKED', res.data)
      })
        .catch((err) => {
          console.log(err)
        })
    },

    // 플레이타임 계산
    getMainPlayrooms: function ({ commit }) {
      axiosConnector.get(`/home/all/`
      ).then((res) => {
        if (res.data.type === 'playroom') {
          commit('GET_MY_PLAYROOMS', res.data)
        }
      }).catch((err) => {
        console.log(err)
      })
    },

  },
  getters: {

  }

};

export default mainContent;
