<template>
  <v-app>
    <my-followings-item
      v-for="following in followinglist"
      :key="following.id"
      :following="following"
    />
  </v-app>
</template>

<script>
import MyFollowingsItem from '@/components/profile/user/MyFollowingsItem.vue'

import { mapState } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'MyFollowingsList',
  components: {
    MyFollowingsItem,
  },
  data: function() {
    return {
      followinglist: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('팔로워 리스트 받아오기')
    this.getFollowingList()
  },
  methods: {
    getFollowingList: function() {
      // axiosConnector.get(`userinfo/${this.userId}`)
      axiosConnector.get(`/account/userInfo`)
        .then((res) => {
          console.log('내 팔로잉 리스트 가져오기11', res.data.to_user)
          this.followinglist = res.data.to_user
          console.log('내 팔로잉 리스트 가져오기2', this.followinglist)
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
