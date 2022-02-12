import axios from 'axios'
import router from '@/router/index.js'
import axiosConnector from '../../utils/axios-connector';
import { DurationChange } from '../../utils/utils';

const video = {
  namespaced: true,
  state: {
    // [검색]
    addedVideos: [], // 생성용으로 추가한 영상
    selectedVideos: [], // 생성용으로 추가하기 위해 선택한 영상
    searchedVideos: [], // 검색한 영상
    searchedVideoIds: [], // 검색한 영상의 ID
    rerenderKey: '',
    watchingVideo: '', // 시청하는 영상
    nextPageToken: '', // 다음 페이지(무한 스크롤용)
    query: '', // 검색어
    order: '', // 정렬 필터
    likedVideos: [], // 좋아한 영상
    order: 'relevance', // 정렬 타입

    // [둘러보기]
    videoTab: '',
    categoryVideos: {
      '지금 핫한': [],
      '여행': [],
      '게임': [],
      '일상': [],
      '노하우/스타일': [],
      '동물': [],
      '엔터테인먼트': [],
      '영화/드라마': [],
      '음악': [],
      '교육/시사': [],
      '스포츠': [],
      '기타': [],
    },
    categoryNextPageToken: {
      '지금 핫한': '',
      '여행': '',
      '게임': '',
      '일상': '',
      '노하우/스타일': '',
      '동물': '',
      '엔터테인먼트': '',
      '영화/드라마': '',
      '음악': '',
      '교육/시사': '',
      '스포츠': '',
      '기타': '',
    }
  },
  mutations: {
    // [검색]
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
    // 정렬 변경
    ON_CHANGE_ORDER: function (state, order) {
      console.log(order)
      state.order = order
    },
    // 유튜브 검색 (기본 정보)
    SEARCH_VIDEOS: function (state, { searchedVideos, isScroll }) {
      console.log('video.js 35 searchedVideos', searchedVideos)
      state.searchedVideoIds = []
      if (!isScroll) {
        state.selectedVideos = []
        state.searchedVideos = []
      }
      for (let searchedVideo of searchedVideos) {
        const idx = state.searchedVideos.findIndex(i => i.videoId === searchedVideo.videoId)
        if (idx === -1) {
          const data = {
            videoId: searchedVideo.id.videoId,
            title: searchedVideo.snippet.title,
            date: searchedVideo.snippet.publishTime,
            thumbnail: searchedVideo.snippet.thumbnails.medium.url,
            channelTitle: searchedVideo.snippet.channelTitle,
          }
          state.searchedVideos.push(data)
          state.searchedVideoIds.push(searchedVideo.id.videoId)
        }
        console.log('idx', idx)
      }
    },
    // 카테고리ID, 영상길이 정보 추가
    SEARCH_VIDEOS_ADD_INFO: function (state, addInfos) {
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
    NEXT_PAGE_TOKEN: function (state, nextPageToken) {
      state.nextPageToken = nextPageToken
    },
    QUERY: function (state, query) {
      state.query = query
    },
    ORDER: function (state, order) {
      state.order = order
    },
    // 생성하기
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
      state.selectedVideos = []
    },
    // 수정하기
    SAVE_ADDED_VIDEOS: function (state, videos) {
      state.addedVideos = videos
    },
    // 검색하기
    SELECT_ALL_ADDED_VIDEOS: function (state) {
      state.selectedVideos = state.addedVideos.slice()
    },
    DESELECT_ALL_ADDED_VIDEOS: function (state) {
      state.selectedVideos = []
    },
    SELECT_ALL_DETAIL_VIDEOS: function (state, videos) {
      state.selectedVideos = videos.slice()
    },
    DESELECT_ALL_DETAIL_VIDEOS: function (state) {
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
    },
    // [둘러보기]
    RESET_VIDEO_CATEGORY_STATE: function (state) {
      const categoryVideos = {
        '지금 핫한': [],
        '여행': [],
        '게임': [],
        '일상': [],
        '노하우/스타일': [],
        '동물': [],
        '엔터테인먼트': [],
        '영화/드라마': [],
        '음악': [],
        '교육/시사': [],
        '스포츠': [],
        '기타': [],
      }
      const categoryNextPageToken = {
        '지금 핫한': '',
        '여행': '',
        '게임': '',
        '일상': '',
        '노하우/스타일': '',
        '동물': '',
        '엔터테인먼트': '',
        '영화/드라마': '',
        '음악': '',
        '교육/시사': '',
        '스포츠': '',
        '기타': '',
      }
      state.categoryVideos = categoryVideos
      state.categoryNextPageToken = categoryNextPageToken
    },
    SET_CATEGORY_NEXT_PAGE_TOKEN: function (state, { categoryName, nextPageToken }) {
      state.categoryNextPageToken[categoryName] = nextPageToken
    },
    GET_CATEGORY_VIDEOS: function (state, { categoryName, videos }) {
      state.videoTab = categoryName
      console.log('GET_CATEGORY_VIDEOS', state.categoryVideos)
      // state.categoryVideos[categoryName] = videos
      // console.log('state.categoryVideos[categoryName]', categoryName)
      // console.log('state.categoryVideos[categoryName]', state.categoryVideos[categoryName])
      for (let video of videos) {
        const idx = state.categoryVideos[categoryName].findIndex(i => i.videoId === video.videoId)
        if (idx === -1) {
          const data = {
            videoId: video.id.videoId,
            title: video.snippet.title,
            date: video.snippet.publishTime,
            thumbnail: video.snippet.thumbnails.medium.url,
            channelTitle: video.snippet.channelTitle,
            categoryId: video.snippet.categoryId,
            duration: DurationChange(video.contentDetails.duration)
          }
          state.categoryVideos[categoryName].push(data)
        }
        console.log('data', idx, state.categoryVideos[categoryName])
      }
      console.log('state.categoryVideos[categoryName]', state.categoryVideos)
    },
  },
  actions: {
    // [검색]
    // Video 검색 데이터 초기화
    resetVideoSearchState: function ({ commit }) {
      commit('RESET_VIDEO_SEARCH_STATE')
    },
    // Video 생성 데이터 초기화
    resetVideoAddState: function ({ commit }) {
      commit('RESET_VIDEO_ADD_STATE')
    },
    // 정렬 변경
    onChangeOrder: function ({ commit }, order) {
      commit('ON_CHANGE_ORDER', order)
    },
    // 유튜브 API로 영상 리스트 검색
    searchVideos: function ({ state, commit, dispatch }, { query, order }) {
      const SEARCH_API_URL = 'https://www.googleapis.com/youtube/v3/search'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY
      console.log('video.js 184 searchVideos', query, order)

      // 검색 API
      const searchParams = {
        key: API_KEY,
        part: 'snippet',
        fields: 'nextPageToken,items(id/videoId,snippet(title,publishTime,thumbnails/medium,channelTitle))',
        type: 'video',
        q: query, // 검색어
        order: order,
        // eventType: 'completed', // 완료된 영상만 검색
        maxResults: 5, // 반환할 영상 개수
      }

      axios({
        method: 'get',
        url: SEARCH_API_URL,
        params: searchParams,
      })
        .then((res) => {
          console.log('video.js 203 searchParams', searchParams)
          const searchedVideos = res.data.items
          const isScroll = false
          commit('SEARCH_VIDEOS', { searchedVideos, isScroll })
          commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
          commit('QUERY', query)
          commit('ORDER', order)
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
      console.log('searchVideosByScroll')
      if (state.nextPageToken != '') {
        setTimeout(() => {
          const params = {
            key: API_KEY,
            part: 'snippet',
            fields: 'nextPageToken,items(id/videoId,snippet(title,publishTime,thumbnails/medium,channelTitle))',
            type: 'video',
            q: state.query, // 검색어
            order: state.order,
            // eventType: 'completed', // 완료된 영상만 검색
            maxResults: 5, // 반환할 영상 개수
            pageToken: state.nextPageToken,
          }
          axios({
            method: 'get',
            url: SEARCH_API_URL,
            params,
          })
            .then((res) => {
              const searchedVideos = res.data.items
              const isScroll = true
              commit('NEXT_PAGE_TOKEN', res.data.nextPageToken)
              commit('SEARCH_VIDEOS', { searchedVideos, isScroll })
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
    // 수정하기
    saveAddedVideos: function ({ commit }, videos) {
      commit('SAVE_ADDED_VIDEOS', videos)
    },
    // 검색하기
    selectAllAddedVideos: function ({ commit }) {
      commit('SELECT_ALL_ADDED_VIDEOS')
    },
    deselectAllAddedVideos: function ({ commit }) {
      commit('DESELECT_ALL_ADDED_VIDEOS')
    },
    // 디테일
    selectAllDetailVideos: function ({ commit }, videos) {
      commit('SELECT_ALL_DETAIL_VIDEOS', videos)
    },
    deselectAllDetailVideos: function ({ commit }) {
      commit('DESELECT_ALL_DETAIL_VIDEOS')
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
    // [둘러보기]
    // 지금 핫한
    getHotCategoryVideos: function ({ commit, dispatch }) {
      const SEARCH_API_URL = 'https://www.googleapis.com/youtube/v3/videos'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY
      console.log('video.js 338 getHotCategoryVideos')

      // 검색 API
      const params = {
        key: API_KEY,
        part: 'snippet,contentDetails',
        fields: 'nextPageToken,items(id,snippet(title,publishTime,thumbnails/medium,categoryId,channelTitle),contentDetails(duration))',
        chart: 'mostPopular',
        maxResults: 5, // 반환할 영상 개수
      }
      axiosConnector.get(SEARCH_API_URL, {
        params
      })
        .then((res) => {
          console.log('video.js 350 getHotCategoryVideos', res)
          const categoryName = '지금 핫한'
          const nextPageToken = res.nextPageToken
          const videos = res.data.items

          commit('SET_CATEGORY_NEXT_PAGE_TOKEN', { categoryName, nextPageToken })
          dispatch('getCategoryVideosLikesInfo', { categoryName, videos })
        }).catch((err) => {
          console.log(err)
        })
    },
    // [둘러보기]
    // 리셋 둘러보기 데이터
    resetVideoCategoryState: function ({ commit }) {
      commit('RESET_CATEGORY_STATE')
    },
    // 카테고리 데이터 가져오기
    getCategoryVideos: function ({ commit, dispatch }, { categoryName, categoryId }) {
      const SEARCH_API_URL = 'https://www.googleapis.com/youtube/v3/videos'
      const API_KEY = process.env.VUE_APP_YOUTUBE_API_KEY
      console.log('video.js 338 getCategoryVideos', categoryName)

      // 검색 API
      const params = {
        key: API_KEY,
        part: 'snippet,contentDetails',
        fields: 'nextPageToken,items(id,snippet(title,publishTime,thumbnails/medium,categoryId,channelTitle),contentDetails(duration))',
        chart: 'mostPopular',
        maxResults: 5, // 반환할 영상 개수
      }
      if (categoryName !== '지금 핫한') {
        params.videoCategoryId = categoryId
      }
      axiosConnector.get(SEARCH_API_URL, {
        params
      })
        .then((res) => {
          console.log('video.js 350 getCategoryVideos', res)
          const nextPageToken = res.nextPageToken
          const videos = res.data.items

          commit('SET_CATEGORY_NEXT_PAGE_TOKEN', { categoryName, nextPageToken })
          dispatch('getCategoryVideosLikesInfo', { categoryName, videos })
        }).catch((err) => {
          console.log(err)
        })
    },
    // 카테고리 영상 결과 좋아요 정보 가져오기
    getCategoryVideosLikesInfo: function ({ commit }, { categoryName, videos }) {
      const urls = []
      for (let video of videos) {
        urls.push(video.id)
      }
      const data = {
        urls: urls
      }
      console.log(data)
      axiosConnector.get(`/profile/video/isLikes`,
        data
      )
        .then((res) => {
          console.log(res)
          const isLikedList = []
          for (let i; i < videos.length; i++) {
            videos[i].isLiked = isLikedList[i]
          }
          commit('GET_CATEGORY_VIDEOS', { categoryName, videos })
        }).catch((err) => {
          console.log(err)
          commit('GET_CATEGORY_VIDEOS', { categoryName, videos })
        })
    }
  },
}
export default video
