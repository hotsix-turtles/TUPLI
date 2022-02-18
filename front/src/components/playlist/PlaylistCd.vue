<template>
  <div class="text-center">
    <span class="playlist-cd">
      <img
        :src="thumbnail"
      >
      <div />
    </span>
  </div>
</template>

<script>

export default {
  name: 'PlaylistCd',
  props: {
    thumbnail: { type: String, default: '' }
  },
}
</script>

<style>

</style>
