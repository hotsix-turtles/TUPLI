<template>
  <div>
    <playlist-video-item-small
      v-for="(video, idx) in playlistVideos"
      :key="idx"
      :playlist-id="playlist.playlistId"
      :playlist-selected="selected"
      :video="video"
      :readonly="videoReadonly"
    />
  </div>
</template>

<script>
import Vue from 'vue'
import PlaylistVideoItemSmall from './PlaylistVideoItemSmall.vue'

export default {
  name: 'PlaylistVideoListItemSmall',
  components: { PlaylistVideoItemSmall },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    playlistVideos: { type: Array }
  },
  data() {
    return {
    }
  },
  computed: {
  },
  methods: {
  },
}
</script>

<style>

</style>
