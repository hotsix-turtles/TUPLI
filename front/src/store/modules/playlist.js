import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector'


const playlist = {
  namespaced: true,
  state: {
    savedFormData: '',
    playlistDetail: '',
  },
  mutations: {
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      console.log('RESET_FORM_DATA', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
    },
    GET_PLAYLIST_DETAIL: function (state, playlistDetail) {
      console.log('playlistDetail', playlistDetail)
      function timeConverter(UNIX_timestamp){
        var a = new Date(UNIX_timestamp * 1000);
        var year = a.getFullYear();
        var month = a.getMonth();
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        var time = year + '년 ' + month + '월 ' + date + '일 ' + hour + ':' + min ;
        return time;
      }
      playlistDetail.tags = playlistDetail.tags.split(',')
      playlistDetail.createdAt = timeConverter(playlistDetail.createdAt)
      state.playlistDetail = playlistDetail
      console.log(state.playlistDetail)
    }
  },
  actions: {
    createPlaylist: function ({ commit }, formData) {
      console.log('createPlaylist', formData)
      axiosConnector.post('/playlist',
        formData,
      )
        .then((res) => {
          console.log(res)
          router.push({ name: 'PlaylistDetail', params: { playlistId: res.data.id } }) // 나중에 디테일페이지로 주소 변경
        })
        .catch((err) => {
          console.log(err)
        })
      commit('RESET_FORM_DATA')
    },
    getPlaylistDetail: function ({ commit }, playlistId) {
      axios({
        url: `https://i6a102.p.ssafy.io/api/v1/playlist/${playlistId}`,
        method: 'get',
        // headers: {Authorization: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaW5ndTQ5NjkiLCJ1c2VyX3NlcSI6MiwidXNlcm5hbWUiOiJtaW5ndTQ5NjkiLCJpYXQiOjE2NDM5NTI5MDMsImV4cCI6MTY0NDAzOTMwM30.PwbIqBvx3Tc-2497EPu_PahPUqAtRlMjDVgsF5kJeQQ'},
        // headers,
      })
        .then((res) => {
          console.log(res)
          commit('GET_PLAYLIST_DETAIL', res.data)
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
