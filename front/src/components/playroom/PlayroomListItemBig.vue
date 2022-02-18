<template>
  <div>
    <playroom-item-big
      v-for="(playroom, idx) in playrooms"
      :key="idx"
      :playroom="playroom"
    />
  </div>
</template>

<script>
import PlayroomItemBig from './PlayroomItemBig.vue'
export default {
  name: 'PlayroomListItemBig',
  components: { PlayroomItemBig },
  props: {
    playrooms: { type: Array, default() { [] } },
  },
}
</script>

<style>

</style>
