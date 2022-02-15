<template>
  <div>
    <history-item
      v-for="activity in activities"
      :key="activity.id"
      :activity="activity"
    />
  </div>
</template>

<script>
import HistoryItem from '@/components/profile/timeline/history/HistoryItem.vue'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'HistoryList',
  components: {
    HistoryItem,
  },
  props: {
    activities: { type: Array, default() { [] } }
  },
  data: function() {
    return {
      historys: [],
    }
  },
  computed: {
    ...mapState('account', ['historyList'])
  },
  created: function() {
    // this.getHistory()
    // this.historys = this.historyList
    // console.log('좋아요한 플레이룸', this.historyList)
  },
  methods: {
    ...mapActions('account', [
      'getHistory'
    ]),

  }
}
</script>

<style>

</style>
