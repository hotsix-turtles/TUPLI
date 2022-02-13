<template>
  <div>
    <back :page-name="'영상 보기'" />
    <video-iframe
      :watching-video="watchingVideo"
    />
  </div>
</template>

<script>
import { mapState } from 'vuex'

import Back from '../../components/common/Back.vue'
import VideoIframe from '../../components/video/VideoIframe.vue'

export default {
  name: 'VideoWatch',
  components: {
    Back,
    VideoIframe,
  },
  computed: {
    ...mapState('video', {
      watchingVideo: state => state.watchingVideo,
    })
  },
}
</script>

<style>

</style>
