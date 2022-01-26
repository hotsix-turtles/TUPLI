<template>
  <v-avatar
    size="80"
    class="ml-2 my-2"
    :class="{ selected: selected, not_selected: !selected }"
  >
    <v-img
      :src="src"
      contain
    />
  </v-avatar>
</template>

<script>
export default {
  name: 'PlaylistThumbnailItem',
  props: {
    src: String,
    selected: Boolean
  },
  data() {
    return {

    }
  }
}
</script>

<style>
.selected {
    border: 2px solid #5B5C9D;
}
.not_selected {
    border: 2px solid gray;
}

</style>