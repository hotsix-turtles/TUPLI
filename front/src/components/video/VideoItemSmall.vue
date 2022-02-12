<template>
  <v-card
    height="13vh"
    class="d-flex align-center"
    outlined
    tile
    :color="color"
  >
    <div
      class="d-flex justify-space-between"
      width="100%"
      @click="selectVideo"
    >
      <div class="d-flex">
        <div class="video-thumbnail">
          <img
            :src="video.thumbnail"
            style="width: 35vw; height: 100px; object-fit: cover;"
            class=""
            @click="watchingVideo(video)"
          >
          <span class="duration">{{ video.duration }}</span>
        </div>
        <div class="d-flex-column mx-2">
          <div
            class="h6"
          >
            <div class="font-3 semi-bold line-height-s txt-3">
              {{ video.title }}
            </div>
          </div>
          <div class="font-4 color-dark-gray">
            <div>{{ video.channelTitle }}</div>
            <div>{{ video.date.slice(0, 10) }}</div>
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
    // eslint-disable-next-line vue/require-default-prop
    video: { type: Object },
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
      if (this.isSelected !== -1) {
        this.removeSelectedVideo(this.video.videoId)
      } else {
        this.addSelectedVideo(this.video)
      }
    },
  },
}
</script>

<style>

</style>
