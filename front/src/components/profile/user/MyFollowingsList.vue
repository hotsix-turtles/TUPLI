<template>
  <v-app>
    <my-followings-item />
  </v-app>
</template>

<script>
import MyFollowingsItem from '@/components/profile/user/MyFollowingsItem.vue'

export default {
  name: 'MyFollowingsList',
  components: {
    MyFollowingsItem,
  },
}
</script>

<style>

</style>
