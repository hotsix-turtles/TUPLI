<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          플레이룸 참여 기록
        </h3>
      </v-row>
    </v-container>
    <history-list />
  </v-app>
</template>

<script>
import HistoryList from '@/components/profile/timeline/history/HistoryList'

export default {
  name: 'History',
  components: {
    HistoryList,
  }
}
</script>

<style>

</style>
