<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="d-flex align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.go(-1)"
        >
          mdi-chevron-left
        </v-icon>
        <h3 class="">
          {{ profile.nickname }}
        </h3>
      </v-row>
    </v-container>
    <div class="d-flex mt-0">
      <v-tabs
        v-model="activeTab"
        centered
        grow
        color="#5B5C9D"
        class="sticky"
      >
        <v-tab class="v-tap-width">
          <div class="d-flex align-center">
            <p class="mx-1 mb-0">
              팔로워
            </p>
            <p class="mx-1 mb-0">
              {{ followerlist.length }}
            </p>
          </div>
        </v-tab>
        <v-tab class="v-tap-width">
          <div class="d-flex align-center">
            <p class="mx-1 mb-0">
              팔로잉
            </p>
            <p class="mx-1 mb-0">
              {{ followinglist.length }}
            </p>
          </div>
        </v-tab>

        <v-tab-item>
          <followers-list
            :followerlist="followerlist"
          />
        </v-tab-item>
        <v-tab-item>
          <followings-list
            :followinglist="followinglist"
          />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import FollowersList from '@/components/profile/user/FollowersList.vue'
import FollowingsList from '@/components/profile/user/FollowingsList.vue'

import { mapActions } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'Follow',
  components: {
    FollowersList,
    FollowingsList,
  },
  data: function() {
    return {
      profile: [],
      followerlist: [],
      followinglist: [],
    }
  },
  created: function() {
    // this.getFollowers(this.profile.userSeq)
    this.getAccounts()
    // this.getFollowerList()
  },
  methods: {
    ...mapActions('account', [
      'getFollowers'
    ]),
    // [조회]
    getAccounts: function () {
      console.log('getAccounts params')
      axiosConnector.get(`userinfo/${this.$route.params.userId}`)
        .then((res) => {
          console.log('성공적', res.data)
          this.profile = res.data

          this.followerlist = res.data.from_user
          this.followinglist = res.data.to_user

        })
        .catch((err) => {
          console.log('에러', err)
        })
    },

    // 팔로우. 팔로잉 리스트 조회
    getFollowList: function() {
      console.log('팔로우 리스트 조회 시도')
    },


  }
}
</script>

<style>

</style>
