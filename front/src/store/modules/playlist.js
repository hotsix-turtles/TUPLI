import Vue from 'vue'
import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector';
import { timeConverter } from '../../utils/utils';


const playlist = {
  namespaced: true,
  state: {
    // [플레이리스트 생성]
    savedFormData: '',
    isSaved: false,

    // [플레이리스트 디테일]
    playlistDetail: '',
    playlistComments: [],
    isLiked: false,

    // [검색]
    searchedPlaylists: [],

    // [둘러보기]
    categoryPlaylists: [],

    // [플레이룸]
    selectedPlaylists: [],
    addedPlaylists: [],
    addedPlaylistVideoIds: [],

    myPlaylists: [],
    likedPlaylists: [],
  },
  mutations: {
    // [플레이리스트 생성]
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      state.isSaved = false
      console.log('RESET_FORM_DATA', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      state.isSaved = true
    },

    // [플레이리스트 디테일]
    GET_PLAYLIST_DETAIL: function (state, playlistDetail) {
      console.log('GET_PLAYLIST_DETAIL', playlistDetail)
      playlistDetail.tags = playlistDetail.tags.split(',')
      playlistDetail.createdAt = timeConverter(playlistDetail.createdAt)
      state.playlistDetail = playlistDetail
      state.recommendedPlaylists = playlistDetail.recommendedPlaylists

      console.log(state.playlistDetail)
    },
    // 플레이리스트 좋아요 여부 조회
    IS_LIKED: function (state, isLiked) {
      console.log('is_LIKED', isLiked)
      if (isLiked === "ok") {
        state.isLiked = true
      } else {
        state.isLiked = false
      }
    },
    // 플레이리스트 좋아요 하기
    LIKE_PLAYLIST: function (state) {
      state.isLiked = true
    },
    // 플레이리스트 좋아요 취소하기
    UNLIKE_PLAYLIST: function (state) {
      state.isLiked = false
    },
    // 플레이리스트 디테일 댓글 조회
    GET_PLAYLIST_COMMENTS: function (state, playlistComments) {
      if (playlistComments.length != 0) {
        playlistComments.forEach((playlistComment) => {
          playlistComment.created = timeConverter(playlistComment.created)
        })
        state.playlistComments = playlistComments
      }
      else {
        state.playlistComments = []
      }
    },

    // [검색]
    SEARCH_PLAYLISTS: function (state, playlists) {
      playlists.forEach((playlist) => {
        playlist.createdAt = timeConverter(playlist.createdAt)
      })
      state.searchedPlaylists = playlists
    },
    RESET_SEARCH_PLAYLISTS: function (state) {
      state.searchedPlaylists = []
    },
    // [둘러보기]
    GET_CATEGORY_PLAYLISTS: function (state, playlists) {
      playlists.forEach((playlist) => {
        playlist.createdAt = timeConverter(playlist.createdAt)
      })
      state.categoryPlaylists = playlists
    },

    // [플레이룸]
    ADD_PLAYLISTS: function (state) {
      state.addedPlaylists = []
      state.selectedPlaylists.map(selectedPlaylist => {
        if (selectedPlaylist && selectedPlaylist.videos)
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
      const idx = state.selectedPlaylists.findIndex(selectedPlaylist => selectedPlaylist.id == toRemovePlaylist.id)
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
    SET_MY_PLAYLISTS: function (state, value) {
      state.myPlaylists = value ? value : state.myPlaylists;
    },
    SET_LIKED_PLAYLISTS: function (state, value) {
      state.likedPlaylists = value ? value : state.likedPlaylists;
    },
    // 플레이리스트로 플레이룸 생성하기
    SET_PLAYLIST_FOR_PLAYROOM: function (state, playlist) {
      state.addedPlaylist.push(playlist)
    }
  },
  actions: {
    // [플레이리스트 생성]
    resetFormData: function ({ commit }) {
      commit('RESET_FORM_DATA')
    },
    createPlaylist: function ({ commit }, formData) {
      axiosConnector.post('/playlist',
        formData
      ).then((res) => {
        console.log('---------------[플레이리스트 생성]', formData)
        console.log(res)
        router.push({ name: 'PlaylistDetail', params: { playlistId: res.data.id } })
        commit('RESET_FORM_DATA')
      })
        .catch((err) => {
          console.log(err)
        })
    },

    // [플레이리스트 수정]
    updatePlaylist: function ({ commit }, { formData, id } ) {
      console.log('updatePlaylist', formData)
      axiosConnector.put(`/playlist/${id}`,
        formData
      ).then((res) => {
        console.log(res)
        router.push({ name: 'PlaylistDetail', params: { playlistId: id } })
        commit('RESET_FORM_DATA')
      })
        .catch((err) => {
          console.log(err)
        })
    },
    // 내 플레이리스트에 담기
    addVideoInPlaylist: function ({ commit }, { formData, id } ) {
      console.log('addVideoInPlaylist', formData)
      axiosConnector.put(`/playlist/${id}`,
        formData
      ).then((res) => {
        console.log(res)
        commit('RESET_FORM_DATA')
      })
        .catch((err) => {
          console.log(err)
        })
    },

    // [플레이리스트 디테일]
    getPlaylistDetail: function ({ commit, dispatch }, playlistId) {
      axiosConnector.get(`/playlist/${playlistId}`,
      )
        .then((res) => {
          console.log('getPlaylistDetail', res)
          commit('GET_PLAYLIST_DETAIL', res.data)
          console.log('playlistId', playlistId)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    saveFormData: function ({ commit }, formData) {
      console.log('saveFormData', formData)
      commit('SAVE_FORM_DATA', formData)
    },
    // 플레이리스트 좋아요
    likePlaylist: function ({ commit }, playlistId) {
      axiosConnector.post(`/playlist/${playlistId}/like`,
      )
        .then((res) => {
          console.log('playlist.js 189 likePlaylist', res)
          commit('LIKE_PLAYLIST')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 플레이리스트 좋아요 취소
    unlikePlaylist: function ({ commit }, playlistId) {
      axiosConnector.delete(`/playlist/${playlistId}/like`,
      )
        .then((res) => {
          console.log('playlist.js 189 unlikePlaylist', res)
          commit('UNLIKE_PLAYLIST')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // [플레이리스트 댓글]
    // 플레이리스트 댓글 조회
    getPlaylistComments: function ({ commit }, playlistId) {
      axiosConnector.get(`/playlist/${playlistId}/comment`)
        .then((res) => {
          commit('GET_PLAYLIST_COMMENTS', res.data)
          console.log("playlist.js : res.data", res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 플레이리스트 댓글 생성
    createPlaylistComment: function ({ commit }, { playlistId, data }) {
      axiosConnector.post(`/playlist/${playlistId}/comment`,
        data
      )
        .then((res) => {
          console.log('createPlaylistComment', res)
          // commit('CREATE_PLAYLIST_COMMENT', res.data)
          // 생성 성공. 아무 행동 안함.
          console.log("덧글 작성 완료.")
          axiosConnector.get(`/playlist/${playlistId}/comment`)
            .then((res) => {
              commit('GET_PLAYLIST_COMMENTS', res.data)
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 플레이리스트 댓글 삭제
    deletePlaylistComment: function ({ commit }, { commentId, playlistId }) {
      axiosConnector.delete(`/playlist/${ commentId }/comment`)
        .then((res) => {
          // 삭제 성공. 아무 행동 안함.
          console.log(commentId + "번 덧글 삭제 완료.")
          axiosConnector.get(`/playlist/${playlistId}/comment`)
            .then((res) => {
              commit('GET_PLAYLIST_COMMENTS', res.data)
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch((err) => {
          console.log(err)
        })
    },

    // [검색]
    searchPlaylists: function ({ commit }, params) {
      console.log('searchPlaylists params', params)
      axiosConnector.get(`/playlist/search`, {
        params
      })
        .then((res) => {
          console.log(res)
          commit('SEARCH_PLAYLISTS', res.data)
        }).catch((err) => {
          console.log(err)
        })
    },
    resetSearchPlaylists: function ({ commit }) {
      commit('RESET_SEARCH_PLAYLISTS')
    },

    // [둘러보기]
    getCategoryPlaylists: function ({ commit }, categoryName) {

      axiosConnector.get(`/playlist/category/${categoryName}`,
      ).then((res) => {
        console.log(`/playlist/category/${categoryName}`, categoryName)
        commit('GET_CATEGORY_PLAYLISTS', res.data)
      })
        .catch((err) => {
          console.log(err)
        })
    },

    // [플레이룸]
    // 플레이리스트 리스트에 추가
    addPlaylists: function ({ commit }) {
      commit('ADD_PLAYLISTS')
    },
    // 추가한 플레이리스트들을 선택한 플레이리스트 리스트로 되돌림
    revokePlaylists: function ({ commit }) {
      commit('REVOKE_PLAYLISTS')
    },
    // 선택한 플레이리스트 리스트에 추가
    selectPlaylist2: function ({ commit }, playlist) {
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
    setMyPlaylist: function ({ commit }, { data }) {
      commit('SET_MY_PLAYLISTS', data);
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
