<template>
  <v-app>
    <playlist-item />
  </v-app>
</template>

<script>
import PlaylistItem from '@/components/profile/save/PlaylistItem.vue'

export default {
  name: 'PlaylistList',
  components: {
    PlaylistItem,
  },
}
</script>

<style>

</style>
