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
  },
  mutations: {
  },
  actions: {
    async validateToken() {
      try {
        const { status } = await axiosConnector.get('/account/tokenvalidate');
        return (status == 200);
      } catch(err) {
        return false;
      }
    }
  },
  modules: {
  }
}
export default account
