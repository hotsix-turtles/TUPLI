import Vue from 'vue'

const playroom = {
  namespaced: true,
  state: {
    roomId: -1,
    roomTitle: '',
    roomPublic: false,
    roomAuthorProfilePic: '',
    roomAuthorName: '',
    roomAuthorFollowerCount: 0,
    roomStartTime: new Date(),
    roomEndTime: new Date(),
    roomContent: '',
    roomTags: [],
    roomPlaylists: [],
    roomCurrentPlaylist: '',
    roomVideos: [],
    roomCurrentVideo: '',
    roomCurrentPlayTime: '',
    roomSelectedChatItem: { id: -1, type: null },
    roomChats: [],
  },
  mutations: {
    setRoomId: ( state, value ) => state.roomId = value,
    setRoomTitle: ( state, value ) => state.roomTitle = value,
    setRoomPublic: ( state, value ) => state.roomPublic = value,
    setRoomAuthor: ( state, value ) => {
      state.roomAuthorName = value.name;
      state.roomAuthorProfilePic = value.pic;
      state.roomAuthorFollowerCount = value.follower
    },
    setRoomStartTime: ( state, value ) => state.roomStartTime = new Date(value),
    setRoomEndTime: ( state, value ) => state.roomEndTime = new Date(value),
    setRoomContent: ( state, value ) => state.roomContent = value,
    setRoomTags: ( state, value ) => state.roomTags = value,
    setRoomPlaylists: ( state, value ) => state.roomPlaylists = value,
    setRoomVideos: ( state, value ) => state.roomVideos = value,
    blockChatById: ( state, id ) => state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = true; }),
    unblockChatById: ( state, id ) => state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = false; }),
    blockChatByUid: ( state, id ) => {
      const authorId = roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = true; })
    },
    unblockChatByUid: ( state, id ) => {
      const authorId = roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = false; })
    },
    selectChatItem: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != -1) return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_ITEM');
      return roomSelectedChatItem;
    },
    selectChatAvatar: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != -1) return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_AVATAR');
      return roomSelectedChatItem;
    },
    deselectChatItem: ( {roomSelectedChatItem} ) => {
      if (roomSelectedChatItem.id == -1) return;
      Vue.set(roomSelectedChatItem, 'id', -1);
      Vue.set(roomSelectedChatItem, 'type', null);
      return roomSelectedChatItem;
    },
  },
  actions: {
    setRoomInfo: (({commit}, {data}) => {
      commit('setRoomId', data.roomId);
      commit('setRoomTitle', data.roomTitle);
      commit('setRoomPublic', data.roomPublic);
      commit('setRoomAuthor', { name: data.roomAuthorName, pic: data.roomAuthorProfilePic, follower: data.roomAuthorFollowerCount});
      commit('setRoomStartTime', data.roomStartTime);
      commit('setRoomEndTime', data.roomEndTime);
      commit('setRoomContent', data.roomContent);
      commit('setRoomTags', data.roomTags);
      commit('setRoomPlaylists', data.roomPlaylists);
      commit('setRoomVideos', data.roomVideos);
    }),
    followUser: ({commit}, id) => {
      console.log('유저 팔로우 처리')
      commit('deselectChatItem')
    },
    showUserProfile: ({commit}, id) => {
      console.log('유저 프로필 로드 처리')
      commit('deselectChatItem')
    },
    blockUser: ({commit}, id) => {
      console.log('유저 차단 처리')
      commit('blockChatByUid', id)
      commit('deselectChatItem')
    },
    blockMessage: ({commit}, id) => {
      console.log('메시지 차단 처리')
      commit('blockChatById', id)
      commit('deselectChatItem')
    },
    unblockUser: ({commit}, id) => {
      console.log('유저 차단 해제 처리')
      commit('unblockChatByUid', id)
      commit('deselectChatItem')
    },
    unblockMessage: ({commit}, id) => {
      console.log('메시지 차단 해제 처리')
      commit('unblockChatById', id)
      commit('deselectChatItem')
    },
    sharePlayroom: (state, id) => {
      // TODO:
      // 1. 플레이룸 접속 URL 생성
      // 2. 카카오톡 공유 API URL 요청 혹은 생성
      // 3. API URL로 리다이렉트
      console.log('플레이룸 카카오톡 공유 처리')
    },
    reportPlayroom: (state, id) => {
      // TODO:
      // 1. 플레이룸 신고 axios 처리 후 결과값(성공여부) 리턴
      console.log('불량 플레이룸 신고 처리')
    },
    sendMessage: ({commit}, payload) => {
      if (!payload) return;
      return new Promise((res, rej) => {
        setTimeout(() => res(), 1000);
      })
    }
  },
  getters: {
    roomPlayTime: ( {roomStartTime, roomEndTime} ) => `${roomStartTime.getHours()}:${roomStartTime.getMinutes()} - ${roomEndTime.getHours()}:${roomEndTime.getMinutes()}`,
    roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
    roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n'),
    roomPlaylistItems: ( {roomPlaylists} ) => roomPlaylists.map((v) => {
      const playlistItems = {
        1: { id: 1, thumbnail_url: 'https://picsum.photos/100/101' },
        2: { id: 2, thumbnail_url: 'https://picsum.photos/100/102' },
        3: { id: 3, thumbnail_url: 'https://picsum.photos/100/103' },
        4: { id: 4, thumbnail_url: 'https://picsum.photos/100/104' },
        5: { id: 5, thumbnail_url: 'https://picsum.photos/100/105' },
        6: { id: 6, thumbnail_url: 'https://picsum.photos/100/106' },
      }
      // TODO: axios json으로 대체

      return playlistItems[v]
    }),
    roomPlaylistVideoItems: ( {roomVideos} ) => roomVideos.map((v) => {
      const playlistVideoItems = {
        1: { id: 1, thumbnail_url: 'https://picsum.photos/161/90', title: "먹물파스타 먹방", playtime: '01:30' },
        2: { id: 2, thumbnail_url: 'https://picsum.photos/162/90', title: "먹물파스타 먹방", playtime: '01:30' },
        3: { id: 3, thumbnail_url: 'https://picsum.photos/163/90', title: "먹물파스타 먹방", playtime: '01:30' },
        4: { id: 4, thumbnail_url: 'https://picsum.photos/164/90', title: "먹물파스타 먹방", playtime: '01:30' },
        5: { id: 5, thumbnail_url: 'https://picsum.photos/165/90', title: "먹물파스타 먹방", playtime: '01:30' },
        6: { id: 6, thumbnail_url: 'https://picsum.photos/166/90', title: "먹물파스타 먹방", playtime: '01:30' },
        7: { id: 7, thumbnail_url: 'https://picsum.photos/167/90', title: "먹물파스타 먹방", playtime: '01:30' },
        8: { id: 8, thumbnail_url: 'https://picsum.photos/168/90', title: "먹물파스타 먹방", playtime: '01:30' },
        9: { id: 9, thumbnail_url: 'https://picsum.photos/169/90', title: "먹물파스타 먹방", playtime: '01:30' },
      }
      // TODO: axios json으로 대체

      return playlistVideoItems[v]
    })
  }
};

export default playroom;
