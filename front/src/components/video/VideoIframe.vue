<template>
  <div>
    <iframe
      :src="videoURI"
      frameborder="0"
      width="100%"
      height="220px"
    />
    <div class="d-flex">
      <div class="mx-2 my-2">
        <div class="semi-bold">
          {{ watchingVideo.title }}
        </div>
        <div class="d-flex">
          <div>{{ watchingVideo.channelTitle }}</div>
          <v-icon>mdi-circle-small</v-icon>
          <div>{{ watchingVideo.date.slice(0,10) }}</div>
        </div>
      </div>
      <!-- 좋아요 -->
      <div class="d-flex-column text-center my-2 mr-2">
        <div
          v-if="watchingVideo.isLiked"
          class="animate__animated animate__heartBeat"
          @click="onClickUnlike"
        >
          <v-icon color="#5B5C9D">
            mdi-cards-heart
          </v-icon>
        </div>
        <div
          v-else
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
  computed: {
    videoURI: function () {
      const videoId = this.watchingVideo.videoId
      return `https://www.youtube.com/embed/${videoId}`
    }
  },
  created: function () {
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
      }).catch((err) => {
        console.log(err)
      })
  },
  methods: {
    ...mapActions('video', [
      'likeVideo',
      'unlikeVideo',
    ]),
    onClickLike: function () {
      this.video.isLiked = true
      this.video.likesCnt++
      this.likeVideo(this.video)
    },
    onClickUnlike: function () {
      this.video.isLiked = false
      this.video.likesCnt--
      this.unlikeVideo(this.video.id)
    },
  }
}
</script>

<style>

</style>
