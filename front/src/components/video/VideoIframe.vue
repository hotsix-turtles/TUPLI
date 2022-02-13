<template>
  <div class="">
    <iframe
      :src="videoURI"
      frameborder="0"
      width="100%"
      height="220px"
    />
    <div class="d-flex justify-space-between">
      <div class="mx-2 my-2">
        <div class="semi-bold">
          {{ watchingVideo.title }}
        </div>
        <div class="d-flex color-dark-gray">
          <div>{{ watchingVideo.channelTitle }}</div>
          <v-icon>mdi-circle-small</v-icon>
          <div>{{ watchingVideo.date.slice(0,10) }}</div>
        </div>
      </div>
      <!-- 좋아요 -->
      <div class="d-flex-column text-center my-2 mr-2">
        <div
          v-if="watchingVideo.isLiked"
          :key="rerenderKey"
          class="animate__animated animate__heartBeat"
          @click="onClickUnlike"
        >
          <v-icon color="#5B5C9D">
            mdi-cards-heart
          </v-icon>
        </div>
        <div
          v-else
          :key="rerenderKey"
          @click="onClickLike"
        >
          <v-icon>mdi-cards-heart-outline</v-icon>
        </div>
        <div class="font-4">
          {{ watchingVideo.likesCnt }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'VideoIframe',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    watchingVideo: { type: Object }
  },
  data: function () {
    return {
      rerenderKey: 0,
    }
  },
  computed: {
    videoURI: function () {
      const videoId = this.watchingVideo.videoId
      return `https://www.youtube.com/embed/${videoId}`
    }
  },
  created: function () {
    console.log('VideoIframe created')
    const data = {
      urls: [this.watchingVideo.videoId]
    }
    axiosConnector.put(`/profile/video/isLikes`,
      data
    )
      .then((res) => {
        console.log('VideoIframe', res)
        this.watchingVideo.isLiked = res.data.isLikesList[0]
        this.watchingVideo.likesCnt = res.data.isLikesCnt[0]
        this.rerenderKey++
      }).catch((err) => {
        console.log(err)
      })

    axiosConnector.post(`/video`,
      this.watchingVideo
    )
      .then((res) => {
        console.log('시청한 영상 추가', res)
      }).catch((err) => {
        console.log('시청한 영상 추가 에러', err)
      })
    console.log('watchingVideo', this.watchingVideo)
  },
  methods: {
    ...mapActions('video', [
      'likeVideo',
      'unlikeVideo',
    ]),
    onClickLike: function () {
      this.watchingVideo.isLiked = true
      this.watchingVideo.likesCnt++
      this.rerenderKey++
      this.likeVideo(this.watchingVideo)
      console.log('watchingVideo', this.watchingVideo)
    },
    onClickUnlike: function () {
      this.watchingVideo.isLiked = false
      this.watchingVideo.likesCnt--
      this.rerenderKey++
      this.unlikeVideo(this.watchingVideo.id)
      console.log('watchingVideo', this.watchingVideo)
    },
  }
}
</script>

<style>
.iframe {
  display: inline-block;
  position: fixed !important;
  z-index: 3;
  top: 5.2vh !important;
  width: 100% !important;
}
</style>
