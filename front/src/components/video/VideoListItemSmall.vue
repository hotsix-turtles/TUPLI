<template>
  <div>
    <video-item-small
      v-for="(video, idx) in videos"
      :key="idx"
      :video="video"
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
    videos: { type: Array }
  },
  data: function () {
    return {
    }
  },
  methods: {
  },
}
</script>

<style>

</style>
