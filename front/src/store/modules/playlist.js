import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector'


const playlist = {
  namespaced: true,
  state: {
    savedFormData: '',
    playlistDetail: '',
    selectedPlaylists: [],
    addedPlaylists: [],
    myPlaylists: [ { playlistId: 1, title: '좋아요한 플레이리스트 1', thumbnail: '', videos: [{ videoId: 'a2dxf-fvfla', title: '냠냠', thumbnail: '' }] }, { playlistId: 2, title: '좋아요한 플레이리스트 2', thumbnail: '' }, { playlistId: 3, title: '좋아요한 플레이리스트 3', thumbnail: '' }, ],
    savedPlaylists: [ { playlistId: 1, title: '저장한 플레이리스트 1', thumbnail: '' }, { playlistId: 2, title: '저장한 플레이리스트 2', thumbnail: '' }, { playlistId: 3, title: '저장한 플레이리스트 3', thumbnail: '' }, ]
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
    },
    ADD_PLAYLISTS: function (state) {
      state.selectedPlaylists
        .filter(selectedPlaylist => state.addedPlaylists.findIndex(addedPlaylist => addedPlaylist.playlistId == selectedPlaylist.playlistId) == -1)
        .map(v => state.addedPlaylists.push(v))

      state.selectedPlaylists = []
      console.log('state.addedPlaylists', state.addedPlaylists)
    },
    REVOKE_PLAYLISTS: function (state) {
      state.selectedPlaylists = []
      state.addedPlaylists.map(v => state.selectedPlaylists.push(v))

      console.log('state.revokePlaylists', state.selectedPlaylists)
    },
    ADD_SELECTED_PLAYLIST: function (state, toAddPlaylist) {
      state.selectedPlaylists.push(toAddPlaylist)
      console.log('add_selected_playlist', state.selectedPlaylists)
    },
    REMOVE_SELECTED_PLAYLIST: function (state, toRemovePlaylist) {
      const idx = state.selectedPlaylists.findIndex(selectedPlaylist => selectedPlaylist.playlistId === toRemovePlaylist.playlistId)
      state.selectedPlaylists.splice(idx, 1)
      console.log('remove_selected_playlist', state.selectedPlaylists)
    },
    WATCHING_PLAYLIST: function (state, watchingPlaylist) {
      state.watchingPlaylist = watchingPlaylist
      //router.push({ name: 'VideoPlaylist' })
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
    },
    // 플레이리스트 리스트에 추가
    addPlaylists: function ({ commit }) {
      commit('ADD_PLAYLISTS')
      commit('video/ADD_VIDEOS', null, { root: true })
    },
    // 추가한 플레이리스트들을 선택한 플레이리스트 리스트로 되돌림
    revokePlaylists: function ({ commit }) {
      commit('REVOKE_PLAYLISTS')
    },
    // 선택한 플레이리스트 리스트에 추가
    addSelectedPlaylist: function ({ commit }, playlist) {
      commit('ADD_SELECTED_PLAYLIST', playlist)
      commit('video/ADD_SELECTED_VIDEOS_FROM_PLAYLIST', playlist, { root: true })
    },
    // 선택한 플레이리스트 리스트에서 제거
    removeSelectedPlaylist: function ({ commit }, playlist) {
      commit('REMOVE_SELECTED_PLAYLIST', playlist.playlistId)
      commit('video/REMOVE_SELECTED_VIDEOS_FROM_PLAYLIST', playlist, { root: true })
    },
    // 플레이리스트 보기 선택한 영상
    watchingPlaylist: function ({ commit }, watchingPlaylist) {
      commit('WATCHING_PLAYLIST', watchingPlaylist)
    },
  },
  modules: {
  }
}
export default playlist
