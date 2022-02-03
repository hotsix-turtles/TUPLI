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
    // Video State 초기화
    RESET_VIDEO_SEARCH_STATE: function (state) {
      state.searchedVideos = []
      state.selectedVideos = []
      state.nextPageToken = ''
      console.log('RESET_VIDEO_SEARCH_STATE')
    },
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
    SEARCH_VIDEOS_ADD_INFO: function (state, addInfos) {

      state.searchedVideos.forEach(searchedVideo => {

        for (let addInfo of addInfos) {
          if (searchedVideo.videoId === addInfo.id) {
            // const addInfoDuration = addInfo.contentDetails.duration
            // let duration = ''
            // const type = ['S', 'M', 'H']
            // let idx1 = addInfoDuration.length - 1
            // let idx2 = 0
            // while (addInfoDuration[idx1] !== 'T') {
            //   if (addInfoDuration[idx1] === type[idx2]) {
            //     while (!isNaN(addInfoDuration[idx1])) {
            //       duration = addInfoDuration[idx1] + duration
            //       idx1++
            //     }
            //     duration = ':' + duration
            //   } else {
            //     duration = ':00' + duration
            //   }
            //   idx2++
            // }
            // console.log('duration', duration)
            searchedVideo.categoryId = Number(addInfo.snippet.categoryId)
            searchedVideo.duration = addInfo.contentDetails.duration
            // searchedVideo.duration = duration
            break
          }
        }
        state.rerenderKey = addInfos[0].id
      })
      console.log('state.searchedVideos', state.searchedVideos)
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
      console.log('SEARCH_VIDEOS_BY_SCROLL', state.searchedVideos)
    },
    NEXT_PAGE_TOKEN: function (state, nextPageToken) {
      state.nextPageToken = nextPageToken
      console.log('state.nextPageToken', state.nextPageToken)
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
    ADD_SELECTED_VIDEO: function (state, video) {
      state.selectedVideos.push(video)
    },
    REMOVE_SELECTED_VIDEO: function (state, video) {
      const idx = state.selectedVideos.findIndex(i => i.videoId === video.videoId)
      state.selectedVideos.splice(idx, 1)
    },
    WATCHING_VIDEO: function (state, watchingVideo) {
      state.watchingVideo = watchingVideo
      router.push({ name: 'VideoWatch' })
    }
  },
  actions: {
    // Video 데이터 초기화
    resetVideoSearchState: function ({ state, commit }) {
      commit('RESET_VIDEO_SEARCH_STATE')
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
      console.log(ids)
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

      console.log('searchVideosByScroll 실행됨')
      console.log('state.nextPageToken', state.nextPageToken)
      if (state.nextPageToken) {
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
        axios({
          method: 'get',
          url: SEARCH_API_URL,
          params,
        })
          .then((res) => {
            commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
            commit('SEARCH_VIDEOS_BY_SCROLL', res.data.items)
          })
          .then(() => {
            dispatch('searchVideosAddInfo')
            $state.loaded()
          })
          .catch((err) => {
            console.log(err)
            $state.complete()
          })
      }
    },
    // 영상 리스트에 추가
    addVideos: function ({ commit }) {
      commit('ADD_VIDEOS')
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
