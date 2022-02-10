<template>
  <div
    class="row"
  >
    <playlist-item-medium
      v-for="(playlist, idx) in playlists"
      :key="idx"
      :playlist="playlist"
      class="col-6"
    />
  </div>
</template>

<script>
import PlaylistItemMedium from './PlaylistItemMedium.vue'
export default {
  name: 'PlaylistListItemMedium',
  components: { PlaylistItemMedium },
  props: {
    playlists: { type: Array, default() { [] } },
  },
}
</script>

<style>

</style>
