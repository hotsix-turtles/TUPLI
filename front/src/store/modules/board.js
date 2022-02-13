import axios from 'axios'
import router from '@/router/index.js'


const board = {
  namespaced: true,
  state: {
    saveFormData: '',
    isSaved: false,
  },
  mutations: {
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      state.isSaved = false
      console.log('RESET_FORM_DATA (board)', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      state.isSaved = true
    },
  },
  actions: {
    saveFormData: function ({ commit }, formData) {
      console.log('saveFormData (board)', formData)
      commit('SAVE_FORM_DATA', formData)
    },
  },
  modules: {
  }
}
export default board
