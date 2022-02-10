<template>
  <div class="text-center">
    <span
      class="playlist-cd-medium"
    >
      <img
        :src="thumbnail"
        @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })"
      >
      <div @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })" />
    </span>
  </div>
</template>

<script>

export default {
  name: 'PlaylistCdMedium',
  props: {
    thumbnail: { type: String, default: '' },
    playlistId: { type: Number, default: 0 },
  },
}
</script>

<style>

</style>
