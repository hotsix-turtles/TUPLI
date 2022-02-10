import axiosConnector from '../../utils/axios-connector'

const account = {
  namespaced: true,
  state: {
    // 사용자 정보
    userId: null,
    email: null,
    nickname: null,
    introduction: null,
    image: null,
    is_vip: null,
    // 팔로잉, 팔로워
    following: null,
    followers: null,

    // [검색]
    searchedAccounts: [],
  },
  mutations: {
    // [검색]
    SEARCH_ACCOUNTS: function (state, accounts) {
      state.searchedAccounts = accounts
    },
  },
  actions: {
    async validateToken() {
      try {
        const { status } = await axiosConnector.get('/account/tokenvalidate');
        return (status == 200);
      } catch(err) {
        return false;
      }
    },
    // [검색]
    searchAccounts: function ({ commit }, params) {
      console.log('searchAccounts params', params)
      axiosConnector.get(`/account/search`, {
        params
      }).then((res) => {
        console.log(res)
        commit('SEARCH_ACCOUNTS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
  },
  modules: {
  }
}
export default account
