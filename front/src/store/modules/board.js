import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector';
import { timeConverter } from '../../utils/utils';

const board = {
  namespaced: true,
  state: {
    // [게시글 생성]
    savedFormData: '',
    isSaved: false,
    chosenPlaylist: { 'id': 0 },
    chosenPlayroom: { 'id': 0 },
    playlistOrPlayroom: '',
    // [게시글 디테일]
    boardDetail: { 'id': 0 },
  },
  mutations: {
    // [게시글 생성]
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      state.isSaved = false
      state.chosenPlaylist = { 'id': 0 }
      state.chosenPlayroom = { 'id': 0 }
      console.log('RESET_FORM_DATA (board)', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      state.isSaved = true
    },
    SELECT_PLAYLIST: function (state, playlist) {
      state.chosenPlaylist = playlist
    },
    SELECT_PLAYROOM: function (state, playroom) {
      state.chosenPlayroom = playroom
    },
    SELECT_PLAYLIST_OR_PLAYROOM: function (state, radioVal) {
      state.playlistOrPlayroom = radioVal
    },
    // [게시글 디테일]
    GET_BOARD_DETAIL: function (state, boardDetail) {
      boardDetail.createdAt = timeConverter(boardDetail.createdAt)
      state.boardDetail = boardDetail
      console.log('state.boardDetail', state.boardDetail)
    },
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
    },
    // [게시글 수정]
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
          console.log(res)
          commit('GET_BOARD_DETAIL', res.data)
          // console.log('BoardId', BoardId)
          // dispatch('isLiked', BoardId)
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
  },
  modules: {
  }
}
export default board
