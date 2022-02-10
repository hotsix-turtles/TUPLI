import axios from 'axios'
import router from '@/router/index.js'


const common = {
  namespaced: true,
  state: {
    showModal: true,  // 모달 보여줄지 여부
    selected: '',  // 선택한 요소
  },
  mutations: {
    // 모달 정보 리셋
    RESET_MODAL_DATA: function (state) {
      state.showModal = true
      state.selected = ''
    },
    // 모달 클릭
    ON_CLICK_MODAL: function (state) {
      state.showModal = !state.showModal
    },
    // 요소 선택
    ON_SELECT_ITEM: function (state, item) {
      state.selected = item
    }
  },
  actions: {
    // 모달 정보 리셋
    resetModalData: function ({ commit }) {
      commit('RESET_MODAL_DATA')
    },
    // 모달 클릭
    onClickModal: function ({ commit }) {
      console.log('onClickModal')
      commit('ON_CLICK_MODAL')
    },
    // 요소 선택
    onSelectItem: function ({ commit, dispatch }, item) {
      commit('ON_SELECT_ITEM', item)
      dispatch('onClickModal')  // created할때 얘가 실행돼서 showModal이 true인거
    },
  },
}
export default common
