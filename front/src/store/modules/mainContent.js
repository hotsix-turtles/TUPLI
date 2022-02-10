import Vue from 'vue'
import axiosConnector from '@/utils/axios-connector'

const mainContent = {
  namespaced: true,
  state: {
    // 플레이룸
    playroomId: '',

    // 플레이리스트

    // 좋아요
    isLikedMain: false,
  },
  mutations: {

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


  },
  actions: {
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

  },
  getters: {

  }

};

export default mainContent;
