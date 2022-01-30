import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import playroom from './modules/playroom.js'
import video from './modules/video.js'
import playlist from './modules/playlist.js'

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    playroom: playroom,
    video: video,
    playlist: playlist,
  }
})
