<template>
  <v-app>
    <history-item
      v-for="history in historys"
      :key="history.id"
      :history="history"
    />
  </v-app>
</template>

<script>
import HistoryItem from '@/components/profile/timeline/history/HistoryItem'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'HistoryList',
  components: {
    HistoryItem,
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
    this.getHistory()
    this.historys = this.historyList
    console.log('좋아요한 플레이룸', this.historyList)
  },
  methods: {
    ...mapActions('account', [
      'getHistory'
    ])
  }
}
</script>

<style>

</style>
