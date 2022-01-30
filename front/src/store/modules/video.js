import axios from 'axios'


const video = {
  namespaced: true,
  state: {
    searchedVideos: [], //
    watchingVideo: '',
    selectedVideos: [],
  },
  mutations: {
    SEARCH_VIDEOS: function (state, searchedVideos) {
      state.searchedVideos = searchedVideos
    },
    ADD_VIDEOS: function (state, videos) {
      for (video of videos) {
        if (!state.selectedVideos.includes(video)) {
          state.selectedVideos.push(video)
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
        q: query,
      }
      axios({
        method: 'get',
        url: API_URL,
        params,
      })
        .then((res) => {
          console.log(res.data)
          commit('SEARCH_VIDEOS', res.data.items)
        })
        .catch((err) => {
          console.log(err)
        })
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
