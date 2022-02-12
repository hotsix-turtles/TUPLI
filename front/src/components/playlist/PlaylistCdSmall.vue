<template>
  <div class="text-center">
    <span
      class="playlist-cd-small"
    >
      <img
        :src="thumbnail"
        @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlistId } })"
      >
      <div @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlistId } })" />
    </span>
  </div>
</template>

<script>

export default {
  name: 'PlaylistCdSmall',
  props: {
    thumbnail: { type: String, default: '' },
    playlistId: { type: Number, default: 0 }
  },
}
</script>

<style>

</style>
