<template>
  <v-app>
    <playroom-item />
    <playlist-item />
  </v-app>
</template>

<script>
import PlayroomItem from './MainPlayroomItem.vue'
import PlaylistItem from './MainPlaylistItem.vue'

export default {
  name: 'MainList',
  components: {
    PlayroomItem,
    PlaylistItem,
  }
}
</script>

<style>

</style>
