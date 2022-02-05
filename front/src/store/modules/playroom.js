import Vue from 'vue'
import axiosConnector from '../../utils/axios-connector'
import wsConnector from '../../utils/ws-connector'

const playroom = {
  namespaced: true,
  state: {
    roomId: -1,
    roomTitle: '',
    roomPublic: false,
    roomLiked: false,
    roomAuthorProfilePic: '',
    roomAuthorName: '',
    roomAuthorFollowerCount: 0,
    roomStartTime: new Date(),
    roomEndTime: new Date(),
    roomContent: '',
    roomTags: [],
    roomPlaylists: [],
    roomCurrentPlaylistOffset: 0,
    roomCurrentVideoOffset: '',
    roomCurrentVideoPlaytime: 0,
    roomSelectedChatItem: { id: '', type: null },
    roomChats: [],
    chatroomId: '',
    chatBlockedId: [],
    chatBlockedUid: [],
    savedFormData: ''
  },
  mutations: {
    RESET_FORM_DATA: function (state) {
      state.savedFormData = ''
      console.log('RESET_FORM_DATA', state.savedFormData)
    },
    SAVE_FORM_DATA: function (state, formData) {
      state.savedFormData = formData
      console.log('SAVE_FORM_DATA', state.savedFormData)
    },
    SET_ROOM_ID: ( state, value ) => state.roomId = value,
    SET_ROOM_TITLE: ( state, value ) => state.roomTitle = value,
    SET_ROOM_PUBLIC: ( state, value ) => state.roomPublic = value,
    SET_ROOOM_LIKED: ( state, value ) => state.roomLiked = value,
    SET_ROOM_AUTHOR: ( state, value ) => {
      state.roomAuthorName = value.name;
      state.roomAuthorProfilePic = value.pic;
      state.roomAuthorFollowerCount = value.follower
    },
    SET_ROOM_START_TIME: ( state, value ) => state.roomStartTime = new Date(value),
    SET_ROOM_END_TIME: ( state, value ) => state.roomEndTime = new Date(value),
    SET_ROOM_CONTENT: ( state, value ) => state.roomContent = value,
    SET_ROOM_TAGS: ( state, value ) => state.roomTags = value,
    SET_ROOM_CURRENT_PLAYLIST_OFFSET: ( state, value ) => state.roomCurrentPlaylistOffset = value,
    SET_ROOM_PLAYLISTS: ( state, value ) => { state.roomPlaylists = value },
    SET_ROOM_CURRENT_VIDEO_OFFSET: ( state, value ) => { state.roomCurrentVideoOffset = value },
    SET_ROOM_CURRENT_VIDEO_PLAYTIME: ( state, value ) => { state.roomCurrentVideoPlaytime = value },
    SET_ROOM_CHATROOM_ID: ( state, value ) => state.chatroomId = value,
    BLOCK_CHAT_BY_ID: ( state, id ) => {
      state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = true; })
      state.chatBlockedId.push(id)
    },
    UNBLOCK_CHAT_BY_ID: ( state, id ) => {
      state.roomChats.map((v) => { if (v.id == id) v.blockedMessage = false; })
      const idx = state.chatBlockedId.indexOf(id)
      if (idx > -1) state.chatBlockedId.splice(idx, 1)
    },
    BLOCK_CHAT_BY_UID: ( state, id ) => {
      const authorId = state.roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = true; })
      state.chatBlockedUid.push(authorId)
    },
    UNBLOCK_CHAT_BY_UID: ( state, id ) => {
      const authorId = state.roomChats.filter((v) => v.id == id)[0].author.id
      state.roomChats.map((v) => { if (v.author.id == authorId) v.blockedUser = false; })
      const idx = state.chatBlockedUid.indexOf(authorId)
      if (idx > -1) state.chatBlockedUid.splice(idx, 1)
    },
    SELECT_CHAT_ITEM: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != '') return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_ITEM');
      return roomSelectedChatItem;
    },
    SELECT_CHAT_AVATAR: ( {roomSelectedChatItem}, id ) => {
      if (roomSelectedChatItem.id != '') return;
      Vue.set(roomSelectedChatItem, 'id', id);
      Vue.set(roomSelectedChatItem, 'type', 'CHAT_AVATAR');
      return roomSelectedChatItem;
    },
    DESELECT_CHAT_ITEM: ( {roomSelectedChatItem} ) => {
      if (roomSelectedChatItem.id == '') return;
      Vue.set(roomSelectedChatItem, 'id', '');
      Vue.set(roomSelectedChatItem, 'type', null);
      return roomSelectedChatItem;
    },
    SEEK_VIDEO: ( {roomCurrentVideoPlaytime}, time ) => {
      roomCurrentVideoPlaytime = time;
    },
    RECEIVE_MESSAGE: ( { roomChats }, payload ) => {
      roomChats.push(payload);
    }
  },
  actions: {
    setRoomInfo: (({commit}, {data}) => {
      commit('SET_ROOM_ID', data.id);
      commit('SET_ROOM_TITLE', data.title);
      commit('SET_ROOM_PUBLIC', data.isPublic);
      commit('SET_ROOM_AUTHOR', { name: data.authorName, pic: data.authorProfilePic, follower: data.authorFollowerCount});
      commit('SET_ROOM_START_TIME', data.startTime);
      commit('SET_ROOM_END_TIME', data.endTime);
      commit('SET_ROOM_CONTENT', data.content);
      commit('SET_ROOM_TAGS', data.tags);
      commit('SET_ROOM_CURRENT_PLAYLIST_OFFSET', data.currentPlaylistOffset)
      commit('SET_ROOM_PLAYLISTS', data.playlists);
      commit('SET_ROOM_CURRENT_VIDEO_OFFSET', data.currentVideoOffset)
      commit('SET_ROOM_CURRENT_VIDEO_PLAYTIME', data.currentVideoPlaytime)
      commit('SET_ROOM_CHATROOM_ID', data.chatroomId);
    }),
    followUser: ({commit}, id) => {
      console.log('유저 팔로우 처리')
      commit('DESELECT_CHAT_ITEM')
    },
    showUserProfile: ({commit}, id) => {
      console.log('유저 프로필 로드 처리')
      commit('DESELECT_CHAT_ITEM')
    },
    blockUser: ({commit}, id) => {
      console.log('유저 차단 처리')
      commit('BLOCK_CHAT_BY_UID', id)
      commit('DESELECT_CHAT_ITEM')
    },
    blockMessage: ({commit}, id) => {
      console.log('메시지 차단 처리')
      commit('BLOCK_CHAT_BY_ID', id)
      commit('DESELECT_CHAT_ITEM')
    },
    unblockUser: ({commit}, id) => {
      console.log('유저 차단 해제 처리')
      commit('UNBLOCK_CHAT_BY_UID', id)
      commit('DESELECT_CHAT_ITEM')
    },
    unblockMessage: ({commit}, id) => {
      console.log('메시지 차단 해제 처리')
      commit('UNBLOCK_CHAT_BY_ID', id)
      commit('DESELECT_CHAT_ITEM')
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
    },
    saveFormData: function ({ commit }, formData) {
      console.log('saveFormData', formData)
      commit('SAVE_FORM_DATA', formData)
    },
    createPlayroom: function ({ commit }, formData) {
      console.log('createPlayroom', formData)
      axiosConnector.post('/playroom', formData)
        .then((res) => {
          console.log(res)
        })
        .catch((err) => {
          console.log(err)
        })
      commit('RESET_FORM_DATA')
    },
  },
  getters: {
    roomPlayTime: ( {roomStartTime, roomEndTime} ) => `${roomStartTime.getHours()}:${roomStartTime.getMinutes()} - ${roomEndTime.getHours()}:${roomEndTime.getMinutes()}`,
    roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
    roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n'),
    roomCurrentPlaylistVideos: ( {roomPlaylists, roomCurrentPlaylistOffset} ) => roomPlaylists[roomCurrentPlaylistOffset] ? roomPlaylists[roomCurrentPlaylistOffset].videos : []
  }
};

export default playroom;
