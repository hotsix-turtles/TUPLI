<template>
  <v-app>
    <notice-item />
  </v-app>
</template>

<script>
import NoticeItem from './NoticeItem.vue'

export default {
  name: 'NoticeList',
  components: {
    NoticeItem
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    notices: { type: Array }
  },
}
</script>

<style>

</style>
