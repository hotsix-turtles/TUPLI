<template>
  <v-app>
    <v-container>
      <div
        class="d-flex justify-end mt-7 mr-5 pb-5"
      />
      <div>
        <div class="d-flex flex-column align-center">
          <img
            class="my-3 profile-img-large"
            src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
            alt=""
          >
          <h3 class="text-center pt-2 pb-1">
            {{ profile.nickname }}
          </h3>
          <div class="d-flex align-center mt-1">
            <p class="mb-0">
              {{ profile.introduction }}
            </p>
          </div>
        </div>
        <div class="d-flex justify-center pt-3">
          <div
            class="d-flex mx-3"
            @click="followList"
          >
            <p class="mr-2">
              팔로워
            </p>
            <p>{{ profile.to_user.length }}</p>
          </div>
          <div
            class="d-flex mx-3"
            @click="followList"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p>{{ profile.from_user.length }}</p>
          </div>
        </div>
        <div class="d-flex justify-center">
          <v-btn
            v-if="followText === '팔로우'"
            class="text-center mx-2 white--text"
            color="#5B5C9D"
            rounded
            @click="followBtn"
          >
            &nbsp;{{ followText }}&nbsp;
          </v-btn>
          <v-btn
            v-else-if="followText === '팔로잉'"
            class="text-center mx-2 dark--text"
            color="#5B5C9D"
            rounded
            outlined
            @click="unfollowBtn"
          >
            &nbsp;{{ followText }}&nbsp;
          </v-btn>
          <v-btn
            class="text-center mx-2"
            outlined
            color="#5B5C9D"
            rounded
            @click="wacthTogether"
          >
            같이 시청하기
          </v-btn>
        </div>
      </div>
    </v-container>

    <div class="d-flex justify-space-around mt-5">
      <v-tabs
        centered
        grow
        color="#5B5C9D"
      >
        <v-tab>
          활동
        </v-tab>
        <v-tab>취향</v-tab>
        <v-tab-item>
          <profile-playlist />
        </v-tab-item>
        <v-tab-item>
          <profile-taste />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import ProfilePlaylist from '../../components/profile/timeline/ProfilePlaylist.vue'
import ProfileTaste from '../../components/profile/timeline/ProfileTaste.vue'

import axiosConnector from '@/utils/axios-connector.js'

import SERVER from '@/api/server'
import { mapActions, mapState } from 'vuex'

export default {
  components: { ProfileTaste, ProfilePlaylist },
  data: function() {
    return {
      profile: [],
      followText: '팔로우',
      follower_cnt: 0, // 팔로잉 하면 팔로워가 늘어남.
      userId: '',
      // followerList: [],
      // followingList: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('타인 프로필 조회', this.profile)
    this.getAccounts()
    this.getFollowerList()
    console.log('타인 프로필 조회', this.$route.params.userId)
    console.log('팔로우리스트', this.following)
    // console.log('팔로우리스트2', this.following.find(this.profile.userSeq))
    this.followState()


  },
  methods: {
    ...mapActions('account', ['follow', 'unfollow', 'getAccounts']),
    // [조회]
    getAccounts: function () {
      console.log('getAccounts params')
      axiosConnector.get(`userinfo/${this.$route.params.userId}`)
        .then((res) => {
          console.log('성공적 프로필', res.data)
          this.profile = res.data
        })
        .catch((err) => {
          console.log('에러', err)
        })
    },
    // // 팔로우 리스트 가져오기
    // getFollowerList: function () {
    //   console.log('getAccounts params')
    //   axiosConnector.get(`userinfo/${this.$route.params.userId}`)
    //     .then((res) => {
    //       this.followerList = res.data.from_user
    //       this.followingList = res.data.to_user
    //     })
    //     .catch((err) => {
    //       console.log('에러', err)
    //     })
    // },
    // [팔로우]
    followBtn: function() {
      if (this.followText === '팔로우') {
        console.log('follow')
        this.followText = '팔로잉'
        this.follow(this.profile.userSeq)
        this.profile.from_user.unshift("userId");
      }
    },
    // [언팔로우]
    unfollowBtn: function() {
      if (this.followText === '팔로잉') {
        console.log('unfollow')
        this.followText = '팔로우'
        console.log('unfollow222', this.profile.userSeq)
        this.unfollow(this.profile.userSeq)
        console.log('unfollow3')
        // this.profile.from_user.unshift("userId");
        console.log('unfollow4')
      }
    },
    // 팔로우 상태 확인
    followState: function() {
      console.log('팔로우리스트3', this.following)
      if (this.following.find(this.profile.userSeq)) {
        console.log('팔로잉 리스트', this.following)
        this.followText = '팔로잉'
      }  // 내가 팔로우한 사람이면 -> 내 followings 목록에 있으면 // followText = '팔로잉'
      else {
        this.followText = '팔로우'
      }  // 팔로우 안했으면 followText = '팔로우'
    },
    // 같이 시청하기
    wacthTogether: function() {
      console.log('함께 보기', this.profile.activities[0].id)
      this.$router.push({ name: 'PlayroomDetail', params: { id: this.profile.activities[0].id}})
    },
    // 팔로우 리스트 이동
    followList: function() {
      console.log('팔로우 리스트로 이동', this.userId)
      this.$router.push({ name: 'Follow', params: { userId: this.$route.params.userId }})
    },
  },

}
</script>

<style>

</style>
