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
          <span class="duration">{{ video.duration }}</span>
        </div>
        <div class="d-flex-column ml-2">
          <div
            class="h6"
          >
            <div class="font-3 line-height-s txt-3">
              {{ video.title }}
            </div>
          </div>
          <div class="font-4 color-dark-gray">
            <span>{{ video.channelTitle }}</span>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapState } from 'vuex'

export default {
  name: 'PlaylistVideoItemSmall',
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
    selected() {
      return this.addedPlaylistVideoIds.find(addedPlaylistVideoId => addedPlaylistVideoId == this.video.videoId)
    },
    color() {
      return this.playlistSelected || this.selected ? "#dde" : "white"
    },
    ...mapState('playlist', ['addedPlaylists', 'addedPlaylistVideoIds', 'selectedPlaylists'])
  },
  methods: {
    clickVideo() {
      if (this.readonly) return;
      if (this.selected)
      {
        this.deselectPlaylistVideo(this.video.videoId);
      }
      else
      {
        this.selectPlaylistVideo(this.video.videoId);
      }
    },
    ...mapActions('playlist', [
      'selectPlaylistVideo',
      'deselectPlaylistVideo',
    ]),
    ...mapActions('video', [
      'watchingVideo',
    ])
  },
}
</script>

<style>

</style>
