import Vue from 'vue'
import axiosConnector from '../../utils/axios-connector'
import wsConnector from '../../utils/ws-connector'

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
    roomSelectedChatItem: { id: '', type: null },
    roomChats: [],
    // roomChats: [
    //   { id: 1, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    //   { id: 2, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    //   { id: 3, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    //   { id: 4, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    //   { id: 5, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    //   { id: 6, author: { id: 1, name: '김형준', thumbnail: 'https://picsum.photos/80/80' }, content: '와 맛있겠다 ㅋㅋㅋ', timestamp: 1643448193, blockedUser: false, blockedMessage: false },
    // ]
    chatroomId: '',
    chatBlockedId: [],
    chatBlockedUid: []
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
    setChatroomId: ( state, value ) => state.chatroomId = value,
    blockChatById: ( state, id ) => {
      state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = true; })
      state.chatBlockedId.push(id)
    },
    unblockChatById: ( state, id ) => {
      state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = false; })
      const idx = state.chatBlockedId.indexOf(id)
      if (idx > -1) state.chatBlockedId.splice(idx, 1)
    },
    blockChatByUid: ( state, id ) => {
      const authorId = state.roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = true; })
      state.chatBlockedUid.push(authorId)
    },
    unblockChatByUid: ( state, id ) => {
      const authorId = state.roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = false; })
      const idx = state.chatBlockedUid.indexOf(authorId)
      if (idx > -1) state.chatBlockedUid.splice(idx, 1)
    },
    selectChatItem: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != '') return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_ITEM');
      return roomSelectedChatItem;
    },
    selectChatAvatar: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != '') return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_AVATAR');
      return roomSelectedChatItem;
    },
    deselectChatItem: ( {roomSelectedChatItem} ) => {
      if (roomSelectedChatItem.id == '') return;
      Vue.set(roomSelectedChatItem, 'id', '');
      Vue.set(roomSelectedChatItem, 'type', null);
      return roomSelectedChatItem;
    },
    recvMessage: async ( {roomChats, chatBlockedUid, chatBlockedId}, payload ) => {
      const id = payload.headers['message-id']
      const body = JSON.parse(payload.body);
      const profile = await axiosConnector.post('/echo', {
        nickname: '시스템',
        profilePictureUrl: 'https://picsum.photos/80/80'
      })
      //const profile = await axiosConnector.get(`/profile/${body.id}`)
      const author = { id: body.id, name: profile.data.nickname, thumbnail: profile.data.profilePictureUrl };
      const content = body.message;
      const timestamp = new Date().getTime();
      const blockedUser = ( chatBlockedUid.find((v) => v == author.id) != undefined );
      const blockedMessage = ( chatBlockedId.find((v) => v == id) != undefined );
      roomChats.push({ id, author, content, timestamp, blockedUser, blockedMessage });
    }
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
      commit('setChatroomId', data.chatroomId);
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
    sendMessage: (state, payload) => {
      if (!this.roomId) return;
      if (!payload || !payload.type || !payload.message || !payload.token) return;
      return wsConnector.send(
        "/pub/chat/message",
        JSON.stringify({ type: payload.type, roomId: state.roomId, message: payload.message }),
        { Authorization: payload.token }
      );
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
