import axios from 'axios'
import router from '@/router/index.js'


const video = {
  namespaced: true,
  state: {
    addedVideos: [], // 생성용으로 추가한 영상
    selectedVideos: [], // 생성용으로 추가하기 위해 선택한 영상
    searchedVideos: [], // 검색한 영상
    searchedVideoIds: [], // 검색한 영상의 ID
    rerenderKey: '',
    watchingVideo: '', // 시청하는 영상
    nextPageToken: '', // 다음 페이지(무한 스크롤용)
    query: '', // 검색어
    likedVideos: [], // 좋아한 영상
    savedVideos: [], // 저장한 영상
  },
  mutations: {
    // Video Search State 초기화
    RESET_VIDEO_SEARCH_STATE: function (state) {
      state.searchedVideos = []
      state.selectedVideos = []
      state.nextPageToken = ''
      console.log('RESET_VIDEO_SEARCH_STATE')
    },
    // Video Add State 초기화
    RESET_VIDEO_ADD_STATE: function (state) {
      state.addedVideos = []
      state.selectedVideos = []
      console.log('RESET_VIDEO_ADD_STATE')
    },
    // 유튜브 검색 (기본 정보)
    SEARCH_VIDEOS: function (state, searchedVideos) {
      state.selectedVideos = []
      state.searchedVideos = []
      state.searchedVideoIds = []
      for (let searchedVideo of searchedVideos) {
        const data = {
          videoId: searchedVideo.id.videoId,
          title: searchedVideo.snippet.title,
          date: searchedVideo.snippet.publishTime,
          thumbnail: searchedVideo.snippet.thumbnails.default.url,
          channelTitle: searchedVideo.snippet.channelTitle,
        }
        state.searchedVideos.push(data)
        state.searchedVideoIds.push(searchedVideo.id.videoId)
      }
    },
    // 카테고리ID, 영상길이 정보 추가
    SEARCH_VIDEOS_ADD_INFO: function (state, addInfos) {

      function HMS(input, type){
        let index = input.indexOf(type);

        if(index < 0 & type != 'H'){
          return "00"; // 들어오는 값이 없는 경우 index가 -1 , H를 00: 으로 표시하고 싶으면 뒤에 유닛값조건만 지우면 된다.
        }
        if(isNaN(input.charAt(index-2))){ //해당 유닛의 인덱스 2번째앞이 숫자인지 확인
          if (type == 'H') { // H이고 한자리 숫자인경우 한자리 숫자만 반환
            return input.charAt(index-1);
          } else{
            return '0' + input.charAt(index-1);} // 숫자 아닌 경우에는 0을 붙인걸 반환
        }else{
          return input.charAt(index-2) + input.charAt(index-1); // 숫자인 경우에는 합쳐서 반환
        }
      }

      function DurationChange(input){
        let H = HMS(input, 'H');
        let M = HMS(input, 'M');
        let S = HMS(input, 'S');

        if (H) { //H가 들어있는 경우에는 : 더하기
          H += ':'
        } else
        {
          if (M=='00') { // H없고 M이 00 인 경우 0으로 변환
            M='0'
          }
        }
        return H  + M + ':' + S ;
      }
      state.searchedVideos.forEach(searchedVideo => {
        for (let addInfo of addInfos) {
          if (searchedVideo.videoId === addInfo.id) {
            searchedVideo.categoryId = Number(addInfo.snippet.categoryId)
            searchedVideo.duration = DurationChange(addInfo.contentDetails.duration)
            break
          }
        }
        state.rerenderKey = addInfos[0].id
      })
    },
    SEARCH_VIDEOS_BY_SCROLL: function (state, searchedVideos) {
      state.searchedVideoIds = []
      for (let searchedVideo of searchedVideos) {
        const data = {
          videoId: searchedVideo.id.videoId,
          title: searchedVideo.snippet.title,
          date: searchedVideo.snippet.publishTime,
          thumbnail: searchedVideo.snippet.thumbnails.default.url,
          channelTitle: searchedVideo.snippet.channelTitle,
        }
        state.searchedVideos.push(data)
        state.searchedVideoIds.push(searchedVideo.id.videoId)
      }
    },
    NEXT_PAGE_TOKEN: function (state, nextPageToken) {
      state.nextPageToken = nextPageToken
    },
    QUERY: function (state, query) {
      state.query = query
    },
    ADD_VIDEOS: function (state) {
      for (let selectedVideo of state.selectedVideos) {
        const idx = state.addedVideos.findIndex(i => i.videoId === selectedVideo.videoId)
        if (idx === -1) {
          state.addedVideos.push(selectedVideo)
        }
      }
      state.selectedVideos = []
      console.log('state.addedVideos', state.addedVideos)
    },
    REMOVE_VIDEOS: function (state) {
      for (let selectedVideo of state.selectedVideos) {
        const idx = state.addedVideos.findIndex(i => i.videoId === selectedVideo.videoId)
        state.addedVideos.splice(idx, 1)
      }
      // let i = 0
      // while (state.selectedVideos.length > 0) {
      //   const idx = state.addedVideos.findIndex(x => x.videoId === state.selectedVideos[i].videoId)
      //   console.log(state.selectedVideos[i].videoId)
      //   state.addedVideos.splice(idx, 1)
      //   i++
      // }
      state.selectedVideos = []
    },
    SELECT_ALL_ADDED_VIDEOS: function (state) {
      state.selectedVideos = state.addedVideos.slice()
    },
    UNSELECT_ALL_ADDED_VIDEOS: function (state) {
      state.selectedVideos = []
    },
    ADD_SELECTED_VIDEO: function (state, video) {
      state.selectedVideos.push(video)
    },
    REMOVE_SELECTED_VIDEO: function (state, videoId) {
      const idx = state.selectedVideos.findIndex(i => i.videoId === videoId)
      state.selectedVideos.splice(idx, 1)
    },
    WATCHING_VIDEO: function (state, watchingVideo) {
      state.watchingVideo = watchingVideo
      router.push({ name: 'VideoWatch' })
    }
  },
  actions: {
    // Video 검색 데이터 초기화
    resetVideoSearchState: function ({ state, commit }) {
      commit('RESET_VIDEO_SEARCH_STATE')
    },
    // Video 생성 데이터 초기화
    resetVideoAddState: function ({ state, commit }) {
      commit('RESET_VIDEO_ADD_STATE')
    },
    // 유튜브 API로 영상 리스트 검색
    searchVideos: function ({ commit, dispatch }, query) {
      const SEARCH_API_URL = 'https://www.googleapis.com/youtube/v3/search'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY

      // 검색 API
      const searchParams = {
        key: API_KEY,
        part: 'snippet',
        fields: 'nextPageToken,items(id/videoId,snippet(title,publishTime,thumbnails/default,channelTitle))',
        type: 'video',
        q: query, // 검색어
        eventType: 'completed', // 완료된 영상만 검색
        maxResults: 5, // 반환할 영상 개수
      }
      axios({
        method: 'get',
        url: SEARCH_API_URL,
        params: searchParams,
      })
        .then((res) => {
          commit('SEARCH_VIDEOS', res.data.items)
          commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
          commit('QUERY', query)
          console.log('113 NEXT_PAGE_TOKEN', res.data.nextPageToken)
        })
        .then(() => {
          dispatch('searchVideosAddInfo')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 유튜브 API로 영상 리스트 검색 (추가 정보 categoryId, duration)
    searchVideosAddInfo: function ({ state, commit }) {
      const VIDEOS_API_URL = 'https://www.googleapis.com/youtube/v3/videos'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY

      const ids = state.searchedVideoIds.join()
      const videoParams = {
        key: API_KEY,
        part: 'snippet,contentDetails',
        fields: 'items(id,snippet/categoryId,contentDetails/duration)',
        id: ids,
      }
      axios({
        method: 'get',
        url: VIDEOS_API_URL,
        params: videoParams,
      })
        .then((res) => {
          console.log('res.data.items add Info', res.data.items)
          commit('SEARCH_VIDEOS_ADD_INFO', res.data.items)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 유튜브 검색 (무한스크롤)
    searchVideosByScroll: function ({ state, commit, dispatch }, $state) {
      const SEARCH_API_URL = 'https://www.googleapis.com/youtube/v3/search'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY

      if (state.nextPageToken != '') {
        setTimeout(() => {
          const params = {
            key: API_KEY,
            part: 'snippet',
            fields: 'nextPageToken,items(id/videoId,snippet(title,publishTime,thumbnails/default,channelTitle))',
            type: 'video',
            q: state.query, // 검색어
            eventType: 'completed', // 완료된 영상만 검색
            maxResults: 5, // 반환할 영상 개수
            pageToken: state.nextPageToken,
          }
          // console.log(params)
          axios({
            method: 'get',
            url: SEARCH_API_URL,
            params,
          })
            .then((res) => {
              // console.log(res)
              commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
              commit('SEARCH_VIDEOS_BY_SCROLL', res.data.items)
              // console.log('248 NEXT_PAGE_TOKEN', res.data.nextPageToken)
            })
            .then(() => {
              dispatch('searchVideosAddInfo')
              $state.loaded()
            })
            .catch((err) => {
              console.log(err)
              $state.complete()
            })
        }, 800)
      }
    },
    // 최종 생성할 영상 리스트에 추가
    addVideos: function ({ commit }) {
      commit('ADD_VIDEOS')
    },
    // 최종 생성할 영상 리스트에서 제거
    removeVideos: function ({ commit }) {
      commit('REMOVE_VIDEOS')
    },
    //
    selectAllAddedVideos: function ({ commit }) {
      commit('SELECT_ALL_ADDED_VIDEOS')
    },
    unselectAllAddedVideos: function ({ commit }) {
      commit('UNSELECT_ALL_ADDED_VIDEOS')
    },
    // 선택한 영상 리스트에 추가
    addSelectedVideo: function ({ commit }, video) {
      commit('ADD_SELECTED_VIDEO', video)
    },
    // 선택한 영상 리스트에서 제거
    removeSelectedVideo: function ({ commit }, videoId) {
      commit('REMOVE_SELECTED_VIDEO', videoId)
    },
    // 영상 보기 선택한 영상
    watchingVideo: function ({ commit }, watchingVideo) {
      commit('WATCHING_VIDEO', watchingVideo)
    },
  },
}
export default video
