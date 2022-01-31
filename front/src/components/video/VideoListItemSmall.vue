<template>
  <div>
    <video-item-small
      v-for="(searchedVideo, idx) in searchedVideos"
      :key="idx"
      :video="searchedVideo"
      @add-video="addVideo"
    />
  </div>
</template>

<script>
import VideoItemSmall from './VideoItemSmall.vue'
export default {
  name: 'VideoItem',
  components: { VideoItemSmall },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    searchedVideos: { type: Array }
  },
  data: function () {
    return {
      selectedVideos: []
    }
  },
  methods: {
    addVideo: function (video) {
      if (!this.selectedVideos.includes(video)) {
        this.selectedVideos.push(video)
      }
      console.log('selectedVideos', this.selectedVideos)
    },
  },
}
</script>

<style>

</style>
