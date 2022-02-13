import axios from 'axios'
import router from '@/router/index.js'


const board = {
  namespaced: true,
  state: {
    saveFormData: '',
    isSaved: false,
    selectedPlaylist: null,
  },
  mutations: {
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      state.isSaved = false
      state.selectedPlaylist = null
      console.log('RESET_FORM_DATA (board)', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      state.isSaved = true
    },
    SELECT_PLAYLIST: function (state, playlist) {
      state.selectedPlaylist = playlist
    }
  },
  actions: {
    saveFormData: function ({ commit }, formData) {
      // console.log('saveFormData (board)', formData)
      commit('SAVE_FORM_DATA', formData)
    },
    selectPlaylist: function ({ commit }, playlist) {
      commit('SELECT_PLAYLIST', playlist)
    },
    resetBoardPlaylistAddState: function ({ commit }) {
      commit('RESET_FORM_DATA',);
    }
  },
  modules: {
  }
}
export default board
