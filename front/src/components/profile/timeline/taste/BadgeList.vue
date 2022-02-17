<template>
  <div
    class="d-flex-contents justify-center"
  >
    <div class="d-flex flex-wrap justify-start">
      <badge-item
        v-for="badge in badges"
        :key="badge.id"
        :badge="badge"
      />
    </div>
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
