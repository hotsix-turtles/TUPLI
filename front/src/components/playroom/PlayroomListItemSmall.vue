<template>
  <v-expansion-panels
    class="mx-auto"
  >
    <playroom-item-small
      v-for="(playroom, idx) in playrooms"
      :key="idx"
      :playroom="playroom"
    />
  </v-expansion-panels>
</template>

<script>
import PlayroomItemSmall from './PlayroomItemSmall.vue'
export default {
  name: 'PlayroomListItemSmall',
  components: { PlayroomItemSmall },
  props: {
    playrooms: { type: Array, default() { [] } },
  },
}
</script>

<style>

</style>
