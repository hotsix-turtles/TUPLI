import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector'

const account = {
  namespaced: true,
  state: {
    // 사용자 정보
    userId: null,
    email: null,
    nickname: null,
    introduction: null,
    image: '@/assets/tupli_logo2_dark',
    is_vip: null,
    following: [],
    followers: [],
    taste: null,

    // 좋아요한 게시물
    likePlayroomList: [],
    likePlaylistList: [],
    likeBoardList: [],

    // 시청 기록
    historyList: [],


    // [검색]
    searchedAccounts: [],

    // 사용자 설정
    alarmSetting: true,  // 알림 여부
    alarmOnRealtime: true,  // 실시간 알림 받기
    alarmOnInvite: true, // 초대 받기
    inviteDomain: 'everyone', //초대 가능한 사람
    alarmOnPlayroomMake: true,  // 플레이룸 생성시 팔로워에게 알람 보낼지 여부
    alarmOnBadge: true,  // 뱃지 알림 받을지


  },
  mutations: {
    // [검색]
    SEARCH_ACCOUNTS: function (state, accounts) {
      state.searchedAccounts = accounts
    },

    // 좋아요한 게시물
    LIKE_PLAYROOM: function (state, likePlayroomList) {
      state.likePlayroomList = likePlayroomList
    },
    LIKE_PLAYLIST: function (state, likePlaylistList) {
      state.likePlaylistList = likePlaylistList
    },
    LIKE_BOARD: function (state, likeBoardList) {
      state.likeBoardList = likeBoardList
    },

    // 플레이룸 시청 기록
    GET_HISTORY: function (state, historyList) {
      state.historyList = historyList
    },

    // 유저 정보 얻기
    GET_USER_ACCOUNTS_FOLLOWERS: function (state, followers) {
      console.log('유저 정보 얻기', followers)
      state.userFollowers = followers
    },
    GET_USER_ACCOUNTS_FOLLOWING: function (state, following) {
      console.log('유저 정보 얻기', following)
      state.userFollowing = following
    },


    // 팔로우
    FOLLOW: function (state, following) {
      console.log('팔로우 뮤테이션', following)
      // state.following.push(following)
      state.following = following
      console.log('팔로우 뮤테이션2', state.following)
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

    // [좋아요한 게시물]
    // 좋아요한 플레이룸 조회
    getLikePlayroomList: function ({ commit }) {
      // console.log('getListPlayroomList', '좋아요한 플레이룸')
      axiosConnector.get(`playroom/likes`
      )
        .then((res) => {
          // console.log('좋아요한 플룸')
          commit('LIKE_PLAYROOM', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 좋아요한 플레이리스트 조회
    getLikePlaylistList: function ({ commit }) {
      // console.log('getLikePlaylistList', '좋아요한 플레이리스트 2')
      axiosConnector.get(`playlist/likes`
      )
        .then((res) => {
          // console.log('getLikePlaylistList', '좋아요한 플레이리스트 3')
          commit('LIKE_PLAYLIST', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 좋아요한 게시글 조회
    getLikeBoardList: function ({ commit }) {
      // console.log('getLikeBoardList', '좋아요한 플레이룸')
      axiosConnector.get(`board/likes`
      )
        .then((res) => {
          // console.log('좋아요한 게시글')
          commit('LIKE_BOARD', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 플레이룸 시청 기록
    getHistory: function ({ commit }) {
      axiosConnector.get(`userInfo/playroom`
      )
        .then((res) => {
          console.log('플레이룸 시청 기록', res.data)
          commit('GET_HISTORY', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },

    // 플레이룸 생성
    makePlayroom: function(params) {
      router.push({ name: 'PlayroomForm', params: params })
    },
    // 플레이리스트 생성
    makePlaylist: function(params) {
      router.push({ name: 'PlaylistForm', params: params })
    },
    // 게시글 생성
    makeBoard: function(params) {
      router.push({ name: 'BoardForm', params: params })
    },
    // 좋아요 취소
    cancelLike: function() {
      console.log('좋아요 취소')
    },

    // 팔로우
    follow: function({ commit, dispatch }, userSeq) {
      console.log('팔로우 액션')
      axiosConnector.post(`account/follow/${userSeq}`)
        .then((res) => {
          console.log('팔로우 성공적', userSeq)
          // 팔로잉 리스트 업데이트
          commit('FOLLOW', userSeq)
          dispatch('getUserInfo', res.data.token)
        })
        .catch((err) => {
          console.log('에러1')
        })
    },

    // 언팔로우
    // unfollow: function({ commit }, userSeq) {
    //   console.log('언팔로우 액션')
    //   axiosConnector.delete(`account/follow/${userSeq}`)
    //     .then((res) => {
    //       console.log('언팔로우 성공적', res.data)
    //       commit.FOLLOW(res.data)
    //     })
    //     .catch((err) => {
    //       console.log('에러2')
    //     })
    // },

    // [사용자 조회]
    // getAccounts: function ({ commit }, userSeq) {
    //   console.log('getAccounts params')
    //   axiosConnector.get(`userinfo/${userSeq}`)
    //     .then((res) => {
    //       console.log('성공적 프로필1', res.data.from_user)
    //       console.log('성공적 프로필2', res.data.to_user)
    //       commit(GET_USER_ACCOUNTS_FOLLOWERS, res.data.from_user)
    //       commit(GET_USER_ACCOUNTS_FOLLOWING, res.data.to_user)
    //       console.log('성공적 프로필3', state.userFollowers)

    //     })
    //     .catch((err) => {
    //       console.log('에러', err)
    //     })
    // },

  },
  modules: {
  }
}
export default account
