import axios from 'axios'
import router from '@/router/index.js'


const board = {
  namespaced: true,
  state: {
    saveFormData: '',
    isSaved: false,
    addedPlaylist: { 'id': 0 },
    addedPlayroom: { 'id': 0 },
    playlistOrPlayroom: '',
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
      state.addedPlaylist = playlist
    },
    SELECT_PLAYROOM: function (state, playroom) {
      state.addedPlayroom = playroom
    },
    SELECT_PLAYLIST_OR_PLAYROOM: function (state, radioVal) {
      state.playlistOrPlayroom = radioVal
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
    selectPlayroom: function ({ commit }, playroom) {
      commit('SELECT_PLAYROOM', playroom)
    },
    resetBoardPlaylistAddState: function ({ commit }) {
      commit('RESET_FORM_DATA',);
    },
    selectPlaylistOrPlayroom: function ({ commit }, radioVal) {
      commit('SELECT_PLAYLIST_OR_PLAYROOM', radioVal)
    }
  },
  modules: {
  }
}
export default board
