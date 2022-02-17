import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector';
import { timeConverter, playtimeConverter } from '../../utils/utils';

const board = {
  namespaced: true,
  state: {
    // [게시글 생성]
    savedFormData: '',
    isSaved: false,
    chosenPlaylist: { 'id': 0 },
    chosenPlayroom: { 'id': 0 },
    playlistOrPlayroom: '',
    myPlaylists: [],
    likedPlaylists: [],
    myPlayrooms: [],
    likedPlayrooms: [],
    // [게시글 디테일]
    boardDetail: { 'id': 0 },
    // [게시글 덧글]
    boardComments: [],
  },
  mutations: {
    // [게시글 생성]
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      state.isSaved = false
      state.chosenPlaylist = { 'id': 0 }
      state.chosenPlayroom = { 'id': 0 }
    },
    RESET_CHOOSE_DATA: function (state) {
      state.chosenPlaylist = { 'id': 0 }
      state.chosenPlayroom = { 'id': 0 }
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      state.isSaved = true
    },
    CHOOSE_PLAYLIST: function (state, playlist) {
      state.chosenPlaylist = playlist
    },
    CHOOSE_PLAYROOM: function (state, playroom) {
      state.chosenPlayroom = playroom
    },
    CHOOSE_PLAYLIST_OR_PLAYROOM: function (state, radioVal) {
      state.playlistOrPlayroom = radioVal
    },
    // 나의 플레이리스트/플레이룸 정보
    GET_MY_PLAYLISTS: function (state, playlists) {
      state.myPlaylists = playlists
    },
    GET_LIKED_PLAYLISTS: function (state, playlists) {
      state.likedPlaylists = playlists
    },
    GET_MY_PLAYROOMS: function (state, playrooms) {
      let today = new Date()
      playrooms.forEach((playroom) => {
        if (playroom.startTime <= today && playroom.endTime >= today) {
          playroom.onPlay = true
        } else {
          playroom.onPlay = false
        }
        playroom.playTime = playtimeConverter(playroom.startTime, playroom.endTime)
      })
      state.myPlayrooms = playrooms
      console.log(state.myPlayrooms)
    },
    GET_LIKED_PLAYROOMS: function (state, playrooms) {
      let today = new Date() / 1000
      playrooms.forEach((playroom) => {
        if (playroom.startTime <= today && playroom.endTime >= today) {
          playroom.onPlay = true
        } else {
          playroom.onPlay = false
        }
        playroom.playTime = playtimeConverter(playroom.startTime, playroom.endTime)
      })
      state.likedPlayrooms = playrooms
    },
    // [게시글 디테일]
    GET_BOARD_DETAIL: function (state, boardDetail) {
      boardDetail.created = timeConverter(boardDetail.created)
      if (boardDetail.playroom !== null) {
        const today = new Date() / 1000
        if (boardDetail.playroom.startTime <= today && boardDetail.playroom.endTime >= today) {
          boardDetail.playroom.onPlay = true
        } else {
          boardDetail.playroom.onPlay = false
        }
        boardDetail.playroom.playTime = playtimeConverter(boardDetail.playroom.startTime, boardDetail.playroom.endTime)
      }
      state.boardDetail = boardDetail
      console.log('-------------------state.boardDetail', state.boardDetail)
    },
    // 게시글 댓글 조회
    GET_BOARD_COMMENTS: function (state, boardComments) {
      if (boardComments.length != 0) {
        boardComments.forEach((boardComment) => {
          boardComment.created = timeConverter(boardComment.created)
        })
        state.boardComments = boardComments
      }
      else {
        state.boardComments = []
      }
    },
  },
  actions: {
    resetFormData: function ({ commit }) {
      commit('RESET_FORM_DATA')
    },
    resetChooseData: function ({ commit }) {
      commit('RESET_CHOOSE_DATA')
    },
    saveFormData: function ({ commit }, formData) {
      // console.log('saveFormData (board)', formData)
      commit('SAVE_FORM_DATA', formData)
    },
    choosePlaylist: function ({ commit }, playlist) {
      commit('CHOOSE_PLAYLIST', playlist)
    },
    choosePlayroom: function ({ commit }, playroom) {
      commit('CHOOSE_PLAYROOM', playroom)
    },
    resetBoardPlaylistAddState: function ({ commit }) {
      commit('RESET_FORM_DATA',);
    },
    choosePlaylistOrPlayroom: function ({ commit }, radioVal) {
      commit('CHOOSE_PLAYLIST_OR_PLAYROOM', radioVal)
    },
    // [게시글 생성]
    createBoard: function ({ commit }, formData) {
      console.log('---------------[게시글 생성]', formData)
      axiosConnector.post('/board',
        formData
      ).then((res) => {
        console.log(res)
        router.push({ name: 'BoardDetail', params: { boardId: res.data.id } })
        commit('RESET_FORM_DATA')
      })
        .catch((err) => {
          console.log(err)
        })
    },
    // 나의 플레이리스트/플레이룸 정보
    getMyPlaylists: function ({ commit }) {
      axiosConnector.get(`/playlist/my`
      ).then((res) => {
        console.log('/playlist/my', res)
        commit('GET_MY_PLAYLISTS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
    getLikedPlaylists: function ({ commit }) {
      axiosConnector.get(`/playlist/likes`
      ).then((res) => {
        console.log('/playlist/likes', res)
        commit('GET_LIKED_PLAYLISTS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
    getMyPlayrooms: function ({ commit }) {
      axiosConnector.get(`/playroom/my`
      ).then((res) => {
        commit('GET_MY_PLAYROOMS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
    getLikedPlayrooms: function ({ commit }) {
      axiosConnector.get(`/playroom/likes`
      ).then((res) => {
        commit('GET_LIKED_PLAYROOMS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
    // [게시글 수정]
    updateBoard: function ({ commit }, { formData, id } ) {
      console.log('updateBoard', formData)
      axiosConnector.put(`/board/${id}`,
        formData
      ).then((res) => {
        console.log(res)
        router.push({ name: 'BoardDetail', params: { BoardId: id } })
        commit('RESET_FORM_DATA')
      })
        .catch((err) => {
          console.log(err)
        })
    },

    // [게시글 디테일]
    getBoardDetail: function ({ commit, dispatch }, BoardId) {
      axiosConnector.get(`/board/${BoardId}`,
      )
        .then((res) => {
          console.log('getBoardDetail', res)
          commit('GET_BOARD_DETAIL', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // [게시글 리액션]
    // 게시글 좋아요
    likeBoard: function ({}, boardId) {
      axiosConnector.post(`/board/${boardId}/like`,
      )
        .then((res) => {
          console.log('board.js 189 likeBoard', res)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 게시글 좋아요 취소
    unlikeBoard: function ({}, boardId) {
      axiosConnector.delete(`/board/${boardId}/like`,
      )
        .then((res) => {
          console.log('Board.js 189 unlikeBoard', res)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // [게시글 댓글]
    // 게시글 댓글 조회
    getBoardComments: function ({ commit }, boardId) {
      axiosConnector.get(`/board/${boardId}/comment`)
        .then((res) => {
          commit('GET_BOARD_COMMENTS', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 게시글 댓글 생성
    createBoardComment: function ({ commit }, { boardId, data }) {
      axiosConnector.post(`/board/${boardId}/comment`,
        data
      )
        .then((res) => {
          // commit('CREATE_PLAYLIST_COMMENT', res.data)
          // 생성 성공. 아무 행동 안함.
          console.log("덧글 작성 완료.")
          axiosConnector.get(`/board/${boardId}/comment`)
            .then((res) => {
              commit('GET_BOARD_COMMENTS', res.data)
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 게시글 댓글 삭제
    deleteBoardComment: function ({ commit }, { commentId, boardId }) {
      axiosConnector.delete(`/board/${ commentId }/comment`)
        .then((res) => {
          // 삭제 성공. 아무 행동 안함.
          console.log(commentId + "번 덧글 삭제 완료.")
          axiosConnector.get(`/board/${boardId}/comment`)
            .then((res) => {
              commit('GET_BOARD_COMMENTS', res.data)
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
  modules: {
  }
}
export default board
