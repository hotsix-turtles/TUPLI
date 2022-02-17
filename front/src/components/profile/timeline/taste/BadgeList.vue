<template>
  <div
    class="d-flex flex-wrap"
  >
    <badge-item
      v-for="badge in badges"
      :key="badge.id"
      :badge="badge"
    />
  </div>
</template>

<script>
import BadgeItem from './BadgeItem.vue'

export default {
  name: 'BadgeList',
  components: {
    BadgeItem,
  },
  props: {
    badges: {type : Array, default() { [] } },
  },
  created: function() {
    // console.log('뱃지333', this.badges)
  },
}
</script>

<style>

</style>
