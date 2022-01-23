import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import playroom from './modules/playroom.js'

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    playroom: playroom
  }
})
