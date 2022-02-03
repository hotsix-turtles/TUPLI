import axios from 'axios'


const playlist = {
  namespaced: true,
  state: {
    savedFormData: '',
  },
  mutations: {
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      console.log('RESET_FORM_DATA', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
    }
  },
  actions: {
    createPlaylist: function ({ commit }, formData) {
      console.log('createPlaylist', formData)
      axios({
        url: `localhost:8080/playlist`,
        method: 'post',
        data: formData,
        // headers
      })
        .then((res) => {
          console.log(res)
        })
        .catch((err) => {
          console.log(err)
        })
      commit('RESET_FORM_DATA')
    },
    saveFormData: function ({ commit }, formData) {
      console.log('saveFormData', formData)
      commit('SAVE_FORM_DATA', formData)
    }
  },
  modules: {
  }
}
export default playlist
