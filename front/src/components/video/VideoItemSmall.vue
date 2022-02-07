<template>
  <v-card
    height="13vh"
    class="d-flex"
    outlined
    tile
    :color="color"
  >
    <div
      class="d-flex justify-space-between container"
      width="100%"
      @click="selectVideo"
    >
      <div class="d-flex">
        <div class="video-thumbnail">
          <img
            :src="video.thumbnail"
            style="width: 35vw; height: 100px;"
            class=""
            @click="watchingVideo(video)"
          >
          <span class="duration">{{ video.duration }}</span>
        </div>
        <div class="d-flex-column ml-2">
          <div
            class="h6"
          >
            <div class="font-3 line-height-s">
              {{ video.title }}
            </div>
          </div>
          <div class="font-4 color-dark-gray">
            <span>{{ video.channelTitle }}</span>
          </div>
        </div>
      </div>
      <div class="">
        <v-icon>mdi-dots-vertical</v-icon>
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
