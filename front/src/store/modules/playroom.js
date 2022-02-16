import Vue from 'vue';
import axiosConnector from '../../utils/axios-connector';
import { playtimeConverter } from '../../utils/utils';

const defaultState = () => {
  return {
    roomId: -1,
    roomTitle: '',
    roomPublic: false,
    roomLiked: false,
    roomRepeat: false,
    roomAuthorId: -1,
    roomAuthorProfilePic: '',
    roomAuthorName: '',
    roomAuthorFollowerCount: 0,
    roomStartTime: new Date(),
    roomEndTime: new Date(),
    roomUserStartTime: new Date(),
    roomUserEndTime: new Date(),
    roomContent: '',
    roomTags: '',
    roomPlaylists: [],
    roomVideos: [],
    roomCurrentPlaylistId: 0,
    roomCurrentVideoId: 0,
    roomCurrentVideoPlaytime: 0,
    roomPlayerState: 0,
    roomSelectedChatItem: { id: '', type: null },
    roomChats: [],
    roomGuests: [],
    roomUserCountMax: 0,
    chatroomId: '',
    chatBlockedId: [],
    chatBlockedUid: [],
    savedFormData: '',
    roomLastSyncSender: 0,
    wsConnector: null,
    syncInstance: null,
    heartbeatInstance: null,
    heartbeat: 0,

    // [검색]
    searchedPlayrooms: [],

    // [둘러보기]
    categoryPlayrooms: [],
  }
}

const playroom = {
  namespaced: true,
  state: defaultState(),
  mutations: {
    RESET_VUEX_DATA: function (state) {
      Object.assign(state, defaultState())
    },
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
    SET_ROOM_LIKED: ( state, value ) => state.roomLiked = value != undefined ? value : state.roomLiked,
    SET_ROOM_REPEAT: ( state, value ) => state.roomRepeat = value != undefined ? value : state.roomRepeat,
    SET_ROOM_AUTHOR: ( state, value ) => {
      state.roomAuthorId = value.id != undefined ? parseInt(value.id) : state.roomAuthorId;
      state.roomAuthorName = value.name ? value.name : state.roomAuthorName;
      state.roomAuthorProfilePic = value.profileImage ? value.profileImage : state.roomAuthorProfilePic;
      state.roomAuthorFollowerCount = value.follower != undefined ? parseInt(value.follower) : state.roomAuthorFollowerCount
    },
    SET_USER_START_TIME: ( state, value ) => state.roomUserStartTime = value ? new Date(value * 1000) : new Date(),
    SET_USER_END_TIME: ( state, value ) => state.roomUserEndTime = value ? new Date(value * 1000) : new Date(),
    SET_ROOM_START_TIME: ( state, value ) => state.roomStartTime = value ? new Date(value * 1000) : new Date(),
    SET_ROOM_END_TIME: ( state, value ) => state.roomEndTime = value ? new Date(value * 1000) : new Date(),
    SET_ROOM_CONTENT: ( state, value ) => state.roomContent = value ? value : state.roomContent,
    SET_ROOM_INVITE_IDS: ( state, value ) => state.roomInviteIds = value ? value : state.roomInviteIds,
    SET_ROOM_TAGS: ( state, value ) => state.roomTags = value ? value : state.roomTags,
    SET_ROOM_CURRENT_PLAYLIST_ID: ( state, value ) => state.roomCurrentPlaylistId = value != undefined ? parseInt(value) : roomCurrentPlaylistId,
    SET_ROOM_PLAYLISTS: ( state, value ) => state.roomPlaylists = value ? value : state.roomPlaylists,
    SET_ROOM_VIDEOS: ( state, value ) => state.roomVideos = value ? value : state.roomVideos,
    SET_ROOM_CURRENT_VIDEO_ID: ( state, value ) => state.roomCurrentVideoId = value != undefined ? parseInt(value) : state.roomCurrentVideoId,
    SET_ROOM_CURRENT_VIDEO_PLAYTIME: ( state, value ) => state.roomCurrentVideoPlaytime = value != undefined ? parseInt(value) : state.roomCurrentVideoPlaytime,
    SET_ROOM_PLAYER_STATE: (state, value) => state.roomPlayerState = value ? value : state.roomPlayerState,
    SET_ROOM_CHATROOM_ID: ( state, value ) => state.chatroomId = value ? value : state.chatroomId,
    SET_ROOM_LAST_SYNC_SENDER: ( state, value ) => state.roomLastSyncSender = value ? value : state.roomLastSyncSender,
    SET_ROOM_USER_COUNT_MAX: ( state, value ) => state.roomUserCountMax = value ? value : state.roomUserCountMax,
    SET_ROOM_GUESTS: ( state, value ) => state.roomGuests = value ? value : state.roomGuests,
    SET_SYNC_INSTANCE: ( state, value ) => state.syncInstance = value ? value : state.state.syncInstance,
    SET_HEARTBEAT_INSTANCE: ( state, value ) => state.heartbeatInstance = value ? value : state.heartbeatInstance,
    CLR_HEARTBEAT_INSTANCE: ( state ) => state.heartbeatInstance = null,
    SET_HEARTBEAT: ( state ) => state.heartbeat += 1,
    CLR_HEARTBEAT: ( state ) => state.heartbeat = 0,
    SET_WS_CONNECTOR: ( state, value ) => state.wsConnector = value ? value : state.wsConnector,
    CLR_WS_CONNECTOR: ( state ) => state.wsConnector = null,
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
    },
    // [검색]
    SEARCH_PLAYROOMS: function (state, playrooms) {
      let today = new Date()
      playrooms.forEach((playroom) => {
        if (playroom.startTime <= today && playroom.endTime >= today) {
          playroom.onPlay = true
        } else {
          playroom.onPlay = false
        }
        playroom.startTime = playtimeConverter(playroom.startTime)
        playroom.endTime = playtimeConverter(playroom.endTime)
      })
      state.searchedPlayrooms = playrooms
    },
    // [둘러보기]
    GET_CATEGORY_PLAYROOMS: function (state, playrooms) {
      let today = new Date()
      playrooms.forEach((playroom) => {
        if (playroom.startTime <= today && playroom.endTime >= today) {
          playroom.onPlay = true
        } else {
          playroom.onPlay = false
        }
        playroom.startTime = playtimeConverter(playroom.startTime)
        playroom.endTime = playtimeConverter(playroom.endTime)
      })
      state.categoryPlayrooms = playrooms
      console.log(state.categoryPlayrooms)
    },
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
      commit('SET_ROOM_PLAYLISTS', data.playlists);
      commit('SET_ROOM_VIDEOS', data.videos);
      commit('SET_ROOM_CURRENT_PLAYLIST_ID', Object.keys(data.playlists)[0])
      commit('SET_ROOM_CHATROOM_ID', `playroom-${data.id}`);
      commit('SET_ROOM_USER_COUNT_MAX', data.userCountMax)
      commit('SET_ROOM_GUESTS', data.guests)
    }),
    setRoomAuthor: ({commit}, author) => {
      commit('SET_ROOM_AUTHOR', author)
    },
    followUser: ({commit}, id) => {
      console.log('유저 팔로우 처리')
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
    // [검색]
    searchPlayrooms: function ({ commit }, params) {
      console.log('searchPlayrooms params', params)
      axiosConnector.get(`/playroom/search`, {
        params
      }).then((res) => {
        console.log('searchPlayroom', res)
        commit('SEARCH_PLAYROOMS', res.data)
      }).catch((err) => {
        console.log(err)
      })
    },
    // [둘러보기]
    getCategoryPlayrooms: function ({ commit }, categoryName) {
      console.log('playroom.js 245 getCategoryPlayrooms')
      axiosConnector.get(`/playroom/category/${categoryName}`,
      ).then((res) => {
        console.log(res)
        console.log(`/playroom/category/${categoryName}`, categoryName)
        commit('GET_CATEGORY_PLAYROOMS', res.data)
      })
        .catch((err) => {
          console.log(err)
        })
    },
    // [좋아요]
    // 플레이룸 좋아요
    likePlayroom: function ({}, playroomId) {
      axiosConnector.post(`/playroom/${playroomId}/like`,
      ).then((res) => {
        console.log('playroom.js 259 likePlayroom', res)
      })
        .catch((err) => {
          console.log(err)
        })
    },
    // 플레이룸 좋아요 취소
    unlikePlayroom: function ({}, playroomId) {
      axiosConnector.delete(`/playroom/${playroomId}/like`,
      ).then((res) => {
        console.log('playroom.js 259 unlikePlayroom', res)
      })
        .catch((err) => {
          console.log(err)
        })
    },
    togglePlayroomRepeat: function ( {state, commit} ) {
      commit('SET_ROOM_REPEAT', !state.roomRepeat);
    },
    loadRoomInfo: async function ( {dispatch}, roomId ) {
      const roomInfo = await axiosConnector.get(`/playroom/${roomId}`);
      dispatch('setRoomInfo', roomInfo);
    },
    loadLikeState: async function ( {rootState, state, commit} ) {
      if (!rootState.isLogin) return;

      const { status, data } = await axiosConnector.get(`/playroom/${state.roomId}/like`)
      if (status != 200) return;

      commit('SET_ROOM_LIKED', Boolean(data))
    },
    togglePlayroomLike: async function ( {state, dispatch} ) {
      if (state.roomLiked) await axiosConnector.delete(`/playroom/${state.roomId}/like`);
      else await axiosConnector.post(`/playroom/${state.roomId}/like`);

      dispatch('loadLikeState');
    },
    updatePlayroom: async function ( {state, dispatch} ) {
      const formData = {
        title: state.roomTitle,
        content: state.roomContent,
        tags: [...state.roomTags.split(',')],
        isPublic: state.roomPublic,
        inviteIds: state.roomInviteIds,
        playlists: Object.keys(state.roomPlaylists).reduce((playlists, playlistId) => {
          playlists[playlistId] = state.roomPlaylists[playlistId].map(vid => state.roomVideos.find(roomVideo => roomVideo.id == vid).videoId);
          return playlists;
        }, {}),
        userCountMax: state.roomUserCountMax
      }

      dispatch('saveFormData', formData)
    },
    deletePlayroom: async function ( {state, commit} ) {
      return await axiosConnector.delete(`/playroom/${state.roomId}`);
    },
    startHeartbeat: function ( {state, commit}, heartbeat_method ) {
      if (state.heartbeatInstance) return;
      commit('SET_HEARTBEAT_INSTANCE', heartbeat_method);
    },
    stopHeartbeat: function ( {state, commit} ) {
      if (!state.heartbeatInstance) return;
      clearInterval(state.heartbeatInstance);
      commit('CLR_HEARTBEAT_INSTANCE');
    },
    setHeartbeat: function ( {commit} ) {
      commit('SET_HEARTBEAT');
    },
    resetHeartbeat: function ( {commit} ) {
      commit('CLR_HEARTBEAT');
    },
    setWsConnector: function ( {commit}, wsConnector ) {
      commit('SET_WS_CONNECTOR', wsConnector);
    },
    resetWsConnector: function ( {commit} ) {
      commit('CLR_WS_CONNECTOR');
    }
  },
  getters: {
    isAuthor: ( {roomAuthorId}, {}, {userId} ) => userId && roomAuthorId && userId == roomAuthorId,
    roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
    roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n'),
    roomCurrentPlaylistVideos: ( {roomPlaylists, roomVideos, roomCurrentPlaylistId} ) => {
      //console.log(roomPlaylists[roomCurrentPlaylistId] ? roomPlaylists[roomCurrentPlaylistId].reduce((prev, cur) => { prev.push(roomVideos.find(video => cur == video.id)); return prev; }, []) : [])
      return roomPlaylists[roomCurrentPlaylistId] ? roomPlaylists[roomCurrentPlaylistId].reduce((prev, cur) => { prev.push(roomVideos.find(video => cur == video.id)); return prev; }, []) : []
    },
    roomFirstVideo: ( {roomPlaylists, roomCurrentPlaylistId } ) => {
      if (!roomPlaylists || !roomCurrentPlaylistId) return null;
      if (!Object.keys(roomPlaylists[roomCurrentPlaylistId]).length) return null;

      var firstVideo = {};

      firstVideo.playlistOffset = 0;
      firstVideo.playlistId = Object.keys(roomPlaylists)[firstVideo.playlistOffset];
      firstVideo.videoOffset = 0;
      firstVideo.videoId = roomPlaylists[firstVideo.playlistId][firstVideo.videoOffset];

      return firstVideo;
    },
    roomPrevVideo: ( {roomPlaylists, roomVideos, roomCurrentPlaylistId, roomCurrentVideoId} ) => {
      const roomCurrentPlaylistOffset = Object.keys(roomPlaylists).findIndex(roomPlaylistId => roomPlaylistId == roomCurrentPlaylistId)
      const roomCurrentVideoOffset = roomPlaylists[roomCurrentPlaylistId].findIndex(roomVideoId => roomVideoId == roomCurrentVideoId)

      var prevVideo = {};

      // 현재 플레이리스트의 첫번째 비디오라면
      if (roomCurrentVideoOffset == 0)
      {
        // 현재 플레이리스트가 첫번째인지 확인
        if (roomCurrentPlaylistOffset == 0)
        {
          prevVideo = null; // 이전 비디오가 없으므로 null
        } else {
          // 첫번째가 아니라면 이전 플레이리스트의 마지막 비디오 ID를 반환
          prevVideo.playlistOffset = roomCurrentPlaylistOffset - 1;
          prevVideo.playlistId = Object.keys(roomPlaylists)[prevVideo.playlistOffset];
          prevVideo.videoOffset = roomPlaylists[prevVideo.playlistId].length - 1;
          prevVideo.videoId = roomPlaylists[prevVideo.playlistId][prevVideo.videoOffset];
        }
      } else {
        // 현재 플레이리스트의 이전 비디오 ID를 반환
        prevVideo.playlistOffset = roomCurrentPlaylistOffset;
        prevVideo.playlistId = Object.keys(roomPlaylists)[prevVideo.playlistOffset];
        prevVideo.videoOffset = roomCurrentVideoOffset - 1;
        prevVideo.videoId = roomPlaylists[prevVideo.playlistId][prevVideo.videoOffset];
      }

      return prevVideo;
    },
    roomNextVideo: ( {roomPlaylists, roomCurrentPlaylistId, roomCurrentVideoId} ) => {
      const roomCurrentPlaylistOffset = Object.keys(roomPlaylists).findIndex(roomPlaylistId => roomPlaylistId == roomCurrentPlaylistId)
      const roomCurrentVideoOffset = roomPlaylists[roomCurrentPlaylistId].findIndex(roomVideoId => roomVideoId == roomCurrentVideoId)

      var nextVideo = {}

      // 현재 플레이리스트의 마지막 비디오라면
      if (roomCurrentVideoOffset >= roomPlaylists[roomCurrentPlaylistId].length - 1)
      {
        // 현재 플레이리스트가 마지막인지 확인
        if (roomCurrentPlaylistOffset >= Object.keys(roomPlaylists).length - 1)
        {
          nextVideo = null; // 더이상 비디오가 없으므로 null
        } else {
          // 마지막이 아니라면 다음 플레이리스트의 첫번째 비디오 ID를 반환
          nextVideo.playlistOffset = roomCurrentPlaylistOffset + 1;
          nextVideo.playlistId = Object.keys(roomPlaylists)[nextVideo.playlistOffset];
          nextVideo.videoOffset = 0;
          nextVideo.videoId = roomPlaylists[nextVideo.playlistId][nextVideo.videoOffset];
        }
      } else {
        // 현재 플레이리스트의 다음 비디오 ID를 반환
        nextVideo.playlistOffset = roomCurrentPlaylistOffset;
        nextVideo.playlistId = Object.keys(roomPlaylists)[nextVideo.playlistOffset];
        nextVideo.videoOffset = roomCurrentVideoOffset + 1;
        nextVideo.videoId = roomPlaylists[nextVideo.playlistId][nextVideo.videoOffset];
      }

      return nextVideo;
    },
  }
};

export default playroom;
