<template>
  <div>
    <back-only />
    <search-bar
      :label="'새로운 영상을 검색해주세요.'"
      @input-change="searchVideos"
    />
    <video-list-item-small
      :searched-videos="searchedVideos"
    />
    <!-- 무한스크롤 -->
    <!-- <infinite-loading
      spinner="waveDots"
      @infinite="searchVideosByScroll"
    >
      <div slot="no-results" />
    </infinite-loading> -->
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
// import InfiniteLoading from "vue-infinite-loading"

import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
    // InfiniteLoading,
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
      'searchVideosByScroll',
    ]),
  },
  // eslint-disable-next-line vue/order-in-components
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos
    })
  }
}
</script>

<style>

</style>
