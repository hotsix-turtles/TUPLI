<template>
  <div class="text-center">
    <span
      class="playlist-cd-medium"
    >
      <img
        :src="thumbnail"
        @click="readonly ? undefined : $router.push({ name: 'PlaylistDetail', params: { playlistId: playlistId } })"
      >
      <div @click="readonly ? undefined : $router.push({ name: 'PlaylistDetail', params: { playlistId: playlistId } })" />
    </span>
  </div>
</template>

<script>

export default {
  name: 'PlaylistCdMedium',
  props: {
    thumbnail: { type: String, default: '' },
    playlistId: { type: Number, default: 0 },
    readonly: { type: Boolean, default: false }
  },
}
</script>

<style>

</style>
