<template>
  <v-app>
    <followers-item
      v-for="follower in followerlist"
      :key="follower.id"
      :follower="follower"
    />
  </v-app>
</template>

<script>
import FollowersItem from './FollowersItem.vue'

import { mapState, mapActions } from 'vuex'

export default {
  name: 'FollowersList',
  components: {
    FollowersItem,
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    followerlist: { type: Array }
  },
  data: function() {
    return {
    }
  },
  computed: {
  },
  created: function() {
    console.log('팔로우 리스트 받아오기')
  },
  methods: {

  }
}
</script>

<style>

</style>
