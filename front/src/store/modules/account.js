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
    following: [],
    followers: [],
    taste: null,
    // 사용자 설정
    alarmSetting: true,  // 알림 여부      
    alarmOnRealtime: true,  // 실시간 알림 받기
    alarmOnInvite: true, // 초대 받기
    inviteDomain: 'everyone', //초대 가능한 사람
    alarmOnPlayroomMake: true,  // 플레이룸 생성시 팔로워에게 알람 보낼지 여부
    alarmOnBadge: true,  // 뱃지 알림 받을지
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
