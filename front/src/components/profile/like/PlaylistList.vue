<template>
  <v-app>
    <playlist-item />
  </v-app>
</template>

<script>
import PlaylistItem from '@/components/profile/like/PlaylistItem.vue'

export default {
  name: 'PlaylistList',
  components: {
    PlaylistItem,
  },
}
</script>

<style>

</style>
