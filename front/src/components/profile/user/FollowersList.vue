<template>
  <v-app>
    <followers-item />
  </v-app>
</template>

<script>
import FollowersItem from '@/components/profile/user/FollowersItem.vue'

export default {
  name: 'FollowersList',
  components: {
    FollowersItem,
  },
}
</script>

<style>

</style>
