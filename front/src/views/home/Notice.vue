<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Home' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          알림
        </h3>
      </v-row>
    </v-container>
    <notice-list />
  </v-app>
</template>

<script>
import NoticeList from '@/components/notice/NoticeList.vue'

export default {
  name: 'Notice',
  components: { NoticeList },
}
</script>

<style>

</style>
