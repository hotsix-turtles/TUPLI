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
export default {
  name: 'TagItem',
  props: {
    content: String
  }
}
</script>

<style>

</style>