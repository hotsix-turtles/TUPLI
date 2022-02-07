import Vue from 'vue'
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
    addedPlaylistVideoIds: [],
    // 아래 두줄은 추후 account.js로 옮길듯
    likedPlaylists: [],
    myPlaylists: [ { playlistId: 1, title: '좋아요한 플레이리스트 1', thumbnail: '', videos: [{ videoId: 'a2dxf-fvfla', title: '냠냠', thumbnail: '' }] }, { playlistId: 2, title: '좋아요한 플레이리스트 2', thumbnail: '' }, { playlistId: 3, title: '좋아요한 플레이리스트 3', thumbnail: '' }, ],
    savedPlaylists: [ { playlistId: 4, title: '저장한 플레이리스트 1', thumbnail: '', videos: [{ videoId: 'a2dxf-fvflb', title: '냠냠', thumbnail: '' }] }, { playlistId: 5, title: '저장한 플레이리스트 2', thumbnail: '' }, { playlistId: 6, title: '저장한 플레이리스트 3', thumbnail: '' }, ]
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
      state.addedPlaylists = []
      state.selectedPlaylists.map(selectedPlaylist => {
        if (selectedPlaylist.videos)
          selectedPlaylist.videos
            .filter(video => !state.addedPlaylistVideoIds.find(addedPlaylistVideoId => addedPlaylistVideoId == video.videoId))
            .map(video => state.addedPlaylistVideoIds.push(video.videoId))
      });
      state.selectedPlaylists.map(selectedPlaylist => state.addedPlaylists.push(selectedPlaylist))
      state.selectedPlaylists = []
      console.log('state.addedPlaylists', state.addedPlaylists)
    },
    REVOKE_PLAYLISTS: function (state) {
      state.selectedPlaylists = []
      state.addedPlaylists.map(addedPlaylist => state.selectedPlaylists.push(addedPlaylist))
      state.addedPlaylists = []
      state.addedPlaylistVideoIds = []

      console.log('state.revokePlaylists', state.selectedPlaylists)
    },
    SELECT_PLAYLIST: function (state, toAddPlaylist) {
      state.selectedPlaylists.push(toAddPlaylist)
      console.log('select_playlist', state.selectedPlaylists)
    },
    DESELECT_PLAYLIST: function (state, toRemovePlaylist) {
      const idx = state.selectedPlaylists.findIndex(selectedPlaylist => selectedPlaylist.playlistId == toRemovePlaylist.playlistId)
      state.selectedPlaylists.splice(idx, 1)
      console.log('deselect_playlist', state.selectedPlaylists)
    },
    WATCHING_PLAYLIST: function (state, watchingPlaylist) {
      state.watchingPlaylist = watchingPlaylist
      //router.push({ name: 'VideoPlaylist' })
    },
    SELECT_PLAYLIST_VIDEO: function (state, videoId) {
      const idx = state.addedPlaylistVideoIds.findIndex(addedPlaylistVideoId => addedPlaylistVideoId == videoId)
      if (idx != -1) return;
      state.addedPlaylistVideoIds.push(videoId)

      console.log('select_playlist_video', videoId)
    },
    SELECT_ALL_PLAYLIST_VIDEO: function (state) {
      state.addedPlaylists.map(addedPlaylist => {
        if (addedPlaylist.videos)
          addedPlaylist.videos.map(video => {
            const idx = state.addedPlaylistVideoIds.findIndex(addedPlaylistVideoId => addedPlaylistVideoId == video.videoId)
            if (idx != -1) return;
            state.addedPlaylistVideoIds.push(video.videoId)
          });
      });

      console.log('select_all_playlist_video')
    },
    DESELECT_PLAYLIST_VIDEO: function (state, videoId) {
      const idx = state.addedPlaylistVideoIds.findIndex(addedPlaylistVideoId => addedPlaylistVideoId == videoId)
      state.addedPlaylistVideoIds.splice(idx, 1)

      console.log('deselect_playlist_video', videoId)
    },
    DESELECT_ALL_PLAYLIST_VIDEO: function (state) {
      state.addedPlaylists.map(addedPlaylist => {
        if (addedPlaylist.videos)
          addedPlaylist.videos.map(video => {
            const idx = state.addedPlaylistVideoIds.findIndex(addedPlaylistVideoId => addedPlaylistVideoId == video.videoId)
            state.addedPlaylistVideoIds.splice(idx, 1)
          });
      });

      console.log('deselect_all_playlist_video')
    },
    RESET_ADDED_PLAYLISTS: function (state) {
      state.selectedPlaylists = []
      state.addedPlaylists = []
      state.addedPlaylistVideoIds = []
    },
    SET_LIKED_PLAYLISTS: function (state, value) {
      state.likedPlaylists = value ? value : state.likedPlaylists;
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
    },
    // 추가한 플레이리스트들을 선택한 플레이리스트 리스트로 되돌림
    revokePlaylists: function ({ commit }) {
      commit('REVOKE_PLAYLISTS')
    },
    // 선택한 플레이리스트 리스트에 추가
    selectPlaylist: function ({ commit }, playlist) {
      commit('SELECT_PLAYLIST', playlist)
    },
    // 선택한 플레이리스트 리스트에서 제거
    deselectPlaylist: function ({ commit }, playlist) {
      commit('DESELECT_PLAYLIST', playlist)
    },
    // 플레이리스트 보기 선택한 영상
    watchingPlaylist: function ({ commit }, watchingPlaylist) {
      commit('WATCHING_PLAYLIST', watchingPlaylist)
    },
    selectPlaylistVideo: function ({ commit }, videoId) {
      commit('SELECT_PLAYLIST_VIDEO', videoId)
    },
    deselectPlaylistVideo: function ({ commit }, videoId) {
      commit('DESELECT_PLAYLIST_VIDEO', videoId)
    },
    selectAllPlaylistVideo: function ({ commit }) {
      commit('SELECT_ALL_PLAYLIST_VIDEO')
    },
    deselectAllPlaylistVideo: function ({ commit }) {
      commit('DESELECT_ALL_PLAYLIST_VIDEO')
    },
    resetAddedPlaylists: function ({ commit }) {
      commit('RESET_ADDED_PLAYLISTS')
    },
    setLikedPlaylist: function ({ commit }, { data }) {
      commit('SET_LIKED_PLAYLISTS', data);
    }
  },
  modules: {
  },
  getters: {
    numberOfAddedPlaylists: state => state.addedPlaylists.length,
    numberOfAddedPlaylistVideos: state => state.addedPlaylists.reduce((acc, cur) => acc + (cur.videos ? cur.videos.length : 0), 0),
    numberOfAddedPlaylistSelectedVideos: state => state.addedPlaylistVideoIds.length,
  }
}
export default playlist
