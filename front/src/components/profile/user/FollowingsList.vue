<template>
  <v-app>
    <followings-item />
  </v-app>
</template>

<script>
import FollowingsItem from '@/components/profile/user/FollowingsItem.vue'

export default {
  name: 'FollowingsList',
  components: {
    FollowingsItem,
  },
}
</script>

<style>

</style>
