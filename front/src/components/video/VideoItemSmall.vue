<template>
  <v-card
    height="12vh"
    class="d-flex"
    outlined
    tile
    :color="color"
  >
    <div
      class="d-flex justify-space-between align-center"
      width="100%"
      @click="selectVideo"
    >
      <div class="d-flex">
        <div class="video-thumbnail">
          <img
            v-if="!isVideoList"
            :src="video ? video.thumbnail : undefined"
            style="width: 35vw; height: 100%; object-fit: cover;"
            class=""
            @click="watchingVideo(video)"
          >
          <img
            v-else
            :src="video ? video.thumbnail : undefined"
            style="width: 35vw; height: 100%; object-fit: cover;"
            class=""
            @click="changeVideo(video)"
          >
          <span class="duration">{{ video ? video.duration : undefined }}</span>
        </div>
        <div
          v-if="!isVideoList"
          class="d-flex-column mx-2"
        >
          <div
            class="h6"
          >
            <div class="font-3 semi-bold line-height-s txt-3">
              {{ video ? video.title : undefined }}
            </div>
          </div>
          <div class="font-4 color-dark-gray">
            <div>{{ video ? video.channelTitle : undefined }}</div>
            <div>{{ video ? video.date.slice(0, 10) : undefined }}</div>
          </div>
        </div>
        <div
          v-else
          class="d-flex-column mx-2"
          @click="changeVideo(video)"
        >
          <div
            class="h6"
          >
            <div class="font-3 semi-bold line-height-s txt-3">
              {{ video ? video.title : undefined }}
            </div>
          </div>
          <div class="font-4 color-dark-gray">
            <div>{{ video ? video.channelTitle : undefined }}</div>
            <div>{{ video ? video.date.slice(0, 10) : undefined }}</div>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'VideoItemSmall',
  props: {
    video: { type: Object, default () { } },
    isVideoList: { type: Boolean, default: false }
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    }),
    isSelected: function () {
      return this.selectedVideos.findIndex(x => x.videoId === this.video.videoId)
    },
    color: function () {
      return this.isSelected === -1 ? "white" : "#dde"
    }
  },
  methods: {
    ...mapActions('video', [
      'watchingVideo',
      'addSelectedVideo',
      'removeSelectedVideo',
      'resetVideoSearchState',
    ]),
    selectVideo: function() {
      if (this.isVideoList) return;
      if (this.isSelected !== -1) {
        this.removeSelectedVideo(this.video.videoId)
      } else {
        this.addSelectedVideo(this.video)
      }
    },
    changeVideo: function (video) {
      this.$emit('change-video', video)
    }
  },
}
</script>

<style>

</style>
