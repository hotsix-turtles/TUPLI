import Vue from 'vue'

const playroom = {
  namespaced: true,
  state: {
    roomId: -1,
    roomTitle: '',
    roomPublic: false,
    roomLiked: false,
    roomAuthorId: -1,
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
    roomPlayerState: 0,
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
    SET_ROOM_ID: ( state, value ) => state.roomId = value != undefined ? parseInt(value) : state.roomId,
    SET_ROOM_TITLE: ( state, value ) => state.roomTitle = value ? value : state.roomTitle,
    SET_ROOM_PUBLIC: ( state, value ) => state.roomPublic = value ? value : state.roomPublic,
    SET_ROOM_LIKED: ( state, value ) => state.roomLiked = value ? value : state.roomLiked,
    SET_ROOM_AUTHOR: ( state, value ) => {
      state.roomAuthorId = value.id != undefined ? parseInt(value.id) : state.roomAuthorId;
      state.roomAuthorName = value.name ? value.name : state.roomAuthorName;
      state.roomAuthorProfilePic = value.profileImage ? value.profileImage : state.roomAuthorProfilePic;
      state.roomAuthorFollowerCount = value.follower != undefined ? parseInt(value.follower) : state.roomAuthorFollowerCount
    },
    SET_ROOM_START_TIME: ( state, value ) => state.roomStartTime = value ? new Date(value * 1000) : new Date(),
    SET_ROOM_END_TIME: ( state, value ) => state.roomEndTime = value ? new Date(value * 1000) : new Date(),
    SET_ROOM_CONTENT: ( state, value ) => state.roomContent = value ? value : state.roomContent,
    SET_ROOM_INVITE_IDS: ( state, value ) => state.roomInviteIds = value ? value : state.roomInviteIds,
    SET_ROOM_TAGS: ( state, value ) => state.roomTags = value ? value : state.roomTags,
    SET_ROOM_CURRENT_PLAYLIST_OFFSET: ( state, value ) => state.roomCurrentPlaylistOffset = value != undefined ? parseInt(value) : roomCurrentPlaylistOffset,
    SET_ROOM_PLAYLISTS: ( state, value ) => state.roomPlaylists = value ? value : state.roomPlaylists,
    SET_ROOM_CURRENT_VIDEO_OFFSET: ( state, value ) => state.roomCurrentVideoOffset = value != undefined ? parseInt(value) : state.roomCurrentVideoOffset,
    SET_ROOM_CURRENT_VIDEO_PLAYTIME: ( state, value ) => state.roomCurrentVideoPlaytime = value != undefined ? parseInt(value) : state.roomCurrentVideoPlaytime,
    SET_ROOM_PLAYER_STATE: (state, value) => state.roomPlayerState = value ? value : state.roomPlayerState,
    SET_ROOM_CHATROOM_ID: ( state, value ) => state.chatroomId = value ? value : state.chatroomId,
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
    RECEIVE_MESSAGE: ( { roomChats }, payload ) => {
      roomChats.push(payload);
    }
  },
  actions: {
    setRoomInfo: (({state, commit}, {data}) => {
      commit('SET_ROOM_ID', data.id);
      commit('SET_ROOM_TITLE', data.title);
      commit('SET_ROOM_PUBLIC', data.isPublic);
      commit('SET_ROOM_AUTHOR', { id: data.user.userSeq, name: (data.user.nickname ? data.user.nickname : data.user.username), profileImage: data.user.profileImage });
      commit('SET_ROOM_START_TIME', data.startTime);
      commit('SET_ROOM_END_TIME', data.endTime);
      commit('SET_ROOM_CONTENT', data.content);
      commit('SET_ROOM_INVITE_IDS', data.inviteIds);
      commit('SET_ROOM_TAGS', data.tags);
      // commit('SET_ROOM_CURRENT_PLAYLIST_OFFSET', data.currentPlaylistOffset)
      commit('SET_ROOM_PLAYLISTS', data.playlists);
      // commit('SET_ROOM_CURRENT_VIDEO_OFFSET', data.currentVideoOffset)
      // commit('SET_ROOM_CURRENT_VIDEO_PLAYTIME', data.currentVideoPlaytime)
      commit('SET_ROOM_CHATROOM_ID', '731f3b99-8257-4eae-86b2-ed38ea36ccff');//data.chatroomId);
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
    saveFormData: function ({ commit }, formData) {
      console.log('saveFormData', formData)
      commit('SAVE_FORM_DATA', formData)
    },
  },
  getters: {
    roomPlayTime: ( {roomStartTime, roomEndTime} ) => {
      const roomStartDate = new Date(roomStartTime);
      const roomEndDate = new Date(roomEndTime);
      return `${roomStartDate.getHours()}:${roomStartDate.getMinutes()} - ${roomEndDate.getHours()}:${roomEndDate.getMinutes()}`
    },
    roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
    roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n'),
    roomCurrentPlaylistVideos: ( {roomPlaylists, roomCurrentPlaylistOffset} ) => roomPlaylists[roomCurrentPlaylistOffset] ? roomPlaylists[roomCurrentPlaylistOffset].videos : []
  }
};

export default playroom;
