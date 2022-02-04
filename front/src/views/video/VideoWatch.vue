<template>
  <div>
    <cancel :page-name="'영상 보기'" />
    <video-iframe :watching-video="watchingVideo" />
  </div>
</template>

<script>
import { mapState } from 'vuex'

import Cancel from '../../components/common/Cancel.vue'
import VideoIframe from '../../components/video/VideoIframe.vue'

export default {
  name: 'VideoWatch',
  components: {
    Cancel,
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
