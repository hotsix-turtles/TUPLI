<template>
  <div class="text-center">
    <span
      class="playlist-cd-medium"
      @click="readonly ? undefined : $router.push({ name: 'PlaylistDetail', params: { playlistId: playlistId } })"
    >
      <img
        :src="thumbnail"
      >
      <div />
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
