<template>
  <v-expansion-panels
    class="mx-auto"
  >
    <playlist-item-small
      v-for="(playlist, idx) in playlists"
      :key="idx"
      :playlist="playlist"
      :readonly="playlistReadonly"
      :video-readonly="videoReadonly"
      :is-radio-btn="isRadioBtn"
    />
  </v-expansion-panels>
</template>

<script>
import PlaylistItemSmall from './PlaylistItemSmall.vue'
export default {
  name: 'PlaylistListItemSmall',
  components: { PlaylistItemSmall },
  props: {
    playlists: { type: Array, default() { [] } },
    playlistReadonly: { type: Boolean, default: false },
    videoReadonly: { type: Boolean, default: false },
    isRadioBtn: { type: Boolean, default: false },
  },
}
</script>

<style>

</style>
