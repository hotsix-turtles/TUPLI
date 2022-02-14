<template>
  <v-app>
    <followers-item
      v-for="follower in followersList"
      :key="follower.id"
      :follower="follower"
    />
  </v-app>
</template>

<script>
import FollowersItem from './FollowersItem.vue'

import { mapState } from 'vuex'

export default {
  name: 'FollowersList',
  components: {
    FollowersItem,
  },
  data: function() {
    return {
      followersList: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('팔로우 리스트 받아오기')
    this.getFollowers()
  },
  methods: {
    // 팔로우 리스트 받아오기
    getFollowers: function() {
      console.log('팔로우 리스트')
      this.followersList = this.followers
      console.log('팔로우 리스트 받아오기2', this.followersList)

    }
  }
}
</script>

<style>

</style>
