<template>
  <div>
    <video-item-big
      v-for="(video, idx) in videos"
      :key="idx"
      :video="video"
    />
  </div>
</template>

<script>
import VideoItemBig from './VideoItemBig.vue'
export default {
  name: 'VideoListItemBig',
  components: { VideoItemBig },
  props: {
    videos: { type: Array, default() { [] } },
  },
}
</script>

<style>

</style>
