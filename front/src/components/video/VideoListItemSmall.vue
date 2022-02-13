<template>
  <div>
    <video-item-small
      v-for="(video, idx) in videos"
      :key="idx"
      :video="video"
      :is-video-list="isVideoList"
      @change-video="changeVideo"
    />
  </div>
</template>

<script>
import VideoItemSmall from './VideoItemSmall.vue'
export default {
  name: 'VideoItem',
  components: { VideoItemSmall },
  props: {
    videos: { type: Array, default () { [] } },
    isVideoList: { type: Boolean, default: false }
  },
  data: function () {
    return {
    }
  },
  methods: {
    changeVideo: function (video) {
      this.$emit('change-video', video)
    }
  },
}
</script>

<style>

</style>
