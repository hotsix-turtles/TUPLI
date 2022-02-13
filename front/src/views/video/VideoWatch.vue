<template>
  <div>
    <back
      :page-name="'영상 보기'"
      :cnt="cnt"
    />
    <video-iframe
      :key="cnt"
      :watching-video="video"
    />
    <div class="container">
      <div class="semi-bold color-main mb-2">
        재생 영상 목록
      </div>
      <video-list-item-small
        v-if="$route.params.isVideoList"
        :videos="watchingVideos"
        :is-video-list="isVideoList"
        @change-video="changeVideo"
      />
      <video-list-item-small
        v-else
        :videos="watchingVideo"
        :is-video-list="isVideoList"
      />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

import Back from '../../components/common/Back.vue'
import VideoIframe from '../../components/video/VideoIframe.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'

export default {
  name: 'VideoWatch',
  components: {
    Back,
    VideoIframe,
    VideoListItemSmall,
  },
  data: function () {
    return {
      isVideoList: true,
      video: {},
      cnt: 0,
    }
  },
  computed: {
    ...mapState('video', {
      watchingVideo: state => state.watchingVideo,
      watchingVideos: state => state.watchingVideos,
    })
  },
  created: function () {
    if (this.$route.params.isVideoList) {
      this.video = this.watchingVideos[0]
    } else {
      this.video = this.watchingVideo
    }
  },
  methods: {
    changeVideo: function (video) {
      this.video = video
      this.cnt++
    }
  }
}
</script>

<style>

</style>
