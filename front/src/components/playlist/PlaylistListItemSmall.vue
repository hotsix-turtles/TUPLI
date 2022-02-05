<template>
  <v-expansion-panels
    class="mx-auto"
  >
    <playlist-item-small
      v-for="(playlist, idx) in playlists"
      :key="idx"
      :playlist="playlist"
    />
  </v-expansion-panels>
</template>

<script>
import PlaylistItemSmall from './PlaylistItemSmall.vue'
export default {
  name: 'PlaylistListItemSmall',
  components: { PlaylistItemSmall },
  props: {
    playlists: { type: Array, default() { [] } }
  },
  created() {
    console.log(this.playlists)
  },
  data: function () {
    return {
    }
  },
  methods: {
  },
}
</script>

<style>

</style>
