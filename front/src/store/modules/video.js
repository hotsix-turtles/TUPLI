import axios from 'axios'


const video = {
  namespaced: true,
  state: {
    searchedVideos: [], // 검색한 영상
    watchingVideo: '', // 시청하는 영상
    addedVideos: [], // 생성용으로 추가한 영상
    nextPageToken: '', // 다음 페이지(무한 스크롤용)
    query: '', // 검색어
    likedVideos: [], // 좋아한 영상
    savedVideos: [], // 저장한 영상
  },
  mutations: {
    SEARCH_VIDEOS: function (state, searchedVideos) {
      state.searchedVideos = searchedVideos
      for (searchedVideo of searchedVideos) {
        const data = {
          id: searchedVideo.id.videoId,
          title: searchedVideo.snippet.title,
          date: searchedVideo.snippet.publishTime,
          thumbnail: searchedVideo.snippet.thumbnails.default.url,
        }
        state.searchedVideos.push(data)
      }
    },
    SEARCH_VIDEOS_BY_SCROLL: function (state, searchedVideos) {
      for (searchedVideo of searchedVideos) {
        const data = {
          id: searchedVideo.id.videoId,
          title: searchedVideo.snippet.title,
          date: searchedVideo.snippet.publishTime,
          thumbnail: searchedVideo.snippet.thumbnails.default.url,
        }
        state.searchedVideos.push(data)
      }
      console.log('SEARCH_VIDEOS_BY_SCROLL', state.searchedVideos)
    },
    NEXT_PAGE_TOKEN: function (state, nextPageToken) {
      state.nextPageToken = nextPageToken
    },
    QUERY: function (state, query) {
      state.query = query
    },
    ADD_VIDEOS: function (state, addedVideos) {
      for (addedVideo of addedVideos) {
        if (!state.addedVideos.includes(addedVideo)) {
          state.addedVideos.push(addedVideo)
        }
      }
    },
    WATCHING_VIDEO: function (state, watchingVideo) {
      state.watchingVideo = watchingVideo
    }
  },
  actions: {
    // 유튜브 API로 영상 리스트 검색
    searchVideos: function ({ commit }, query) {
      const API_URL = 'https://www.googleapis.com/youtube/v3/search'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY

      const params = {
        key: API_KEY,
        part: 'snippet',
        type: 'video',
        q: query, // 검색어
        eventType: 'completed', // 완료된 영상만 검색
        maxResults: 5, // 반환할 영상 개수
        // pageToken: 'CAoQAA',
      }
      axios({
        method: 'get',
        url: API_URL,
        params,
      })
        .then((res) => {
          console.log(res)
          commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
          commit('QUERY', query)
          commit('SEARCH_VIDEOS', res.data.items)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    searchVideosByScroll: function ({ state, commit }, $state) {
      const API_URL = 'https://www.googleapis.com/youtube/v3/search'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY
      console.log('searchVideosByScroll 실행됨')
      if (state.nextPageToken) {
        $state.loaded()
        const params = {
          key: API_KEY,
          part: 'snippet',
          // fields: items(id/videoId,snippet(title,publishTime,thumbnails)),
          type: 'video',
          q: state.query, // 검색어
          eventType: 'completed', // 완료된 영상만 검색
          maxResults: 5, // 반환할 영상 개수
          pageToken: state.nextPageToken,
        }
        axios({
          method: 'get',
          url: API_URL,
          params,
        })
          .then((res) => {
            console.log(state.query)
            console.log(res.data.items)
            commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
            commit('SEARCH_VIDEOS_BY_SCROLL', res.data.items)
          })
          .catch((err) => {
            console.log(err)
            $state.complete()
          })
      } else {
        $state.complete()
      }
    },
    // 영상 리스트에 추가
    addVideo: function ({ commit }, videos) {
      commit('ADD_VIDEO', video)
    },
    // 영상 보기 선택한 영상
    watchingVideo: function ({ commit }, watchingVideo) {
      commit('WATCHING_VIDEO', watchingVideo)
    },
  },
}
export default video
