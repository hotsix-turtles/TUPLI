<template>
  <div>
    <back-only />
    <search-bar
      :label="'새로운 영상을 검색해주세요.'"
      @input-change="searchVideos"
    />
    <order :select-list="selectList" />
    <video-list-item-small
      :key="rerenderKey"
      :videos="searchedVideos"
      width="100vw"
    />
    <!-- 하단 리스트에 추가하기 버튼 -->
    <add-button-bottom />
    <!-- 무한스크롤 -->
    <infinite-loading
      v-if="searchedVideos.length > 0"
      spinner="waveDots"
      @infinite="searchVideosByScroll(order)"
    >
      <div slot="no-results" />
    </infinite-loading><br><br>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import InfiniteLoading from "vue-infinite-loading"

import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import AddButtonBottom from '../../components/common/AddButtonBottom.vue'
import Order from '../../components/common/Order.vue'

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
    Order,
  },
  data: function () {
    return {
      selectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '조회순': 'viewCount',
      },
    }
  },
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
      selectedVideos: state => state.selectedVideos,
      rerenderKey: state => state.rerenderKey,  // video는 API 두번 요청해서 변경내용 반영하기 위한 key
      nextPageToken: state => state.nextPageToken,
    })
  },
  created: function () {
    // this.$router.beforeEach((to, from, next) => {
    //   console.log(from.path)
    //   console.log(from.path != '/video/watch')
    //   console.log(typeof(from.path),typeof('/video/watch'))
    //   if (from.path != '/video/watch') {
    //     this.resetVideoSearchState()
    //   }
    //   next()
    // })
    this.resetVideoSearchState()
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
      'searchVideosByScroll',
      'resetVideoSearchState',
    ]),
  },
}
</script>

<style>

</style>
