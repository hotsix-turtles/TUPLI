<template>
  <div class="text-center">
    <span class="playlist-cd-medium">
      <img
        :src="thumbnail"
      >
      <div />
    </span>
  </div>
</template>

<script>

export default {
  name: 'PlaylistCdItemMedium',
  props: {
    thumbnail: { type: String, default: '' }
  },
}
</script>

<style>

</style>
