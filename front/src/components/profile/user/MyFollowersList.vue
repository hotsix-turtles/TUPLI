<template>
  <v-app>
    <my-followers-item
      v-for="follower in followerlist"
      :key="follower.id"
      :follower="follower"
    />
  </v-app>
</template>

<script>
import MyFollowersItem from './MyFollowersItem.vue'

import { mapState } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'MyFollowersList',
  components: {
    MyFollowersItem,
  },
  data: function() {
    return {
      followerlist: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('팔로워 리스트 받아오기')
    this.getFollowerList()
  },
  methods: {
    // 팔로우 리스트 받아오기
    // getFollowers: function() {
    //   console.log('팔로우 리스트', this.followers)
    //   this.followersList = this.followers
    //   console.log('팔로우 리스트 받아오기2', this.followersList)
    // },
    getFollowerList: function() {
      axiosConnector.get(`userinfo/${this.userId}`)
        .then((res) => {
          console.log('내 팔로워 리스트 가져오기11', res.data.from_user)
          this.followerlist = res.data.from_user
          console.log('내 팔로워 리스트 가져오기2', this.followerlist)
          // this.followinglist = res.data.from_user
          // console.log('내 팔로우 리스트 가져오기3', this.followinglist)
        })
        .catch((err) => {
          console.log('에러', err)
        })
    }
  }
}
</script>

<style>

</style>
