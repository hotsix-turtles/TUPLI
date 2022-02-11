<template>
  <v-card
    height="13vh"
    class="d-flex"
    outlined
    tile
  >
    <div
      class="d-flex justify-space-between container"
      width="100%"
      @click="clickVideo"
    >
      <div class="d-flex">
        <div class="video-thumbnail">
          <img
            :src="video.thumbnail"
            style="width: 35vw; height: 100px;"
            class=""
            @click="watchingVideo(video)"
          >
          <span class="duration">
            {{ video.duration }}
          </span>
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
import Vue from 'vue'
import { mapActions, mapState } from 'vuex'

export default {
  name: 'PlayroomPlaylistItemSmall',
  props: {
    playlistId: { type: Number, default: 0 },
    playlistSelected: { type: Boolean, default: false },
    video: { type: Object, default() {} },
    readonly: { type: Boolean, defautl: false }
  },
  data() {
    return {
    }
  },
  computed: {
  },
  methods: {
    ...mapActions('video', [
      'watchingVideo',
    ])
  },
}
</script>

<style>

</style>
