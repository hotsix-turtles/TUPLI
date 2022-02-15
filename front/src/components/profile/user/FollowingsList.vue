<template>
  <v-app>
    <followings-item
      v-for="following in followinglist"
      :key="following.id"
      :following="following"
    />
  </v-app>
</template>

<script>
import FollowingsItem from '@/components/profile/user/FollowingsItem.vue'

export default {
  name: 'FollowingsList',
  components: {
    FollowingsItem,
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    followinglist: { type: Array }
  },
}
</script>

<style>

</style>
