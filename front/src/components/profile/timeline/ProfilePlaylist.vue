<template>
  <div>
    <history-list
      :activities="activities"
    />
  </div>
</template>

<script>
import HistoryList from '@/components/profile/timeline/history/HistoryList.vue'

export default {
  name: 'ProfilePlaylist',
  components: {
    HistoryList,
  },
  props: {
    activities: { type: Array, default() { [] } }
  },
  created: function() {
    console.log('활동 리스트', this.activities)
  }
}
</script>

<style>

</style>
