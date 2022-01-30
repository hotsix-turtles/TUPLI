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
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
    ])
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
