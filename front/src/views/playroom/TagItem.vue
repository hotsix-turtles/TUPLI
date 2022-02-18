<template>
  <v-chip
    color="#5B5C9D"
    text-color="white"
    class="mt-1 ml-1"
    small
    draggable
  >
    {{ content }}
  </v-chip>
</template>

<script>
// deprecated components
export default {
  name: 'TagItem',
  props: {
    content: { type: String, default: '' }
  }
}
</script>

<style>

</style>
