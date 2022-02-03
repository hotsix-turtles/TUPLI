<template>
  <div>
    <back-only />
    <search-bar
      :label="'새로운 영상을 검색해주세요.'"
      @input-change="searchVideos"
    />
    <video-list-item-small
      :key="rerenderKey"
      :videos="searchedVideos"
      width="100vw"
    />
    <!-- 하단 리스트에 추가하기 버튼 -->
    <add-button-bottom />
    {{ selectedVideos }}
    <!-- {{ searchedVideos }} -->
    <!-- 무한스크롤 -->
    <infinite-loading
      spinner="waveDots"
      @infinite="searchVideosByScroll"
    >
      <div slot="no-results" />
    </infinite-loading>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import InfiniteLoading from "vue-infinite-loading"

import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import AddButtonBottom from '../../components/common/addButtonBottom.vue'

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
  },
  data: function () {
    return {
    }
  },
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
      selectedVideos: state => state.selectedVideos,
      rerenderKey: state => state.rerenderKey,
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
