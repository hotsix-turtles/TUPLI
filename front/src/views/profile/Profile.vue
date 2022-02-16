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
            :src="ImgUrl(profile.profileImage)"
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
        <div class="d-flex justify-center pt-3 mb-3">
          <div
            class="d-flex mx-3"
            @click="followList"
          >
            <p class="mr-2">
              팔로워
            </p>
            <p>{{ follower_cnt }}</p>
          </div>
          <div
            class="d-flex mx-3"
            @click="followList"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p>{{ followingList.length }}</p>
          </div>
        </div>
        <div class="d-flex justify-center">
          <v-btn
            v-if="!follow"
            class="text-center mx-2 white--text"
            color="#5B5C9D"
            rounded
            @click="followBtn"
          >
            &nbsp;팔로우&nbsp;
          </v-btn>
          <v-btn
            v-else
            class="text-center mx-2 dark--text"
            color="#5B5C9D"
            rounded
            outlined
            @click="unfollowBtn"
          >
            &nbsp;팔로잉&nbsp;
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
          <profile-playlist
            :activities="activities"
            :nickname="nickname"
          />
        </v-tab-item>
        <v-tab-item>
          <profile-taste
            :tastes="tastes"
            :nickname="nickname"
            :user-id="userId"
          />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import ProfilePlaylist from '@/components/profile/timeline/ProfilePlaylist.vue'
import ProfileTaste from '@/components/profile/timeline/ProfileTaste.vue'

import { getImage } from '../../utils/utils'
import axiosConnector from '@/utils/axios-connector.js'

import { mapActions, mapState } from 'vuex'

export default {
  components: { ProfileTaste, ProfilePlaylist },
  data: function() {
    return {
      profile: [],
      follow: false,
      followerList: [], // 팔로잉 하면 팔로워가 늘어남.
      followingList: [],
      userId: '',
      activities: '',
      tastes: '',
      nickname: '',

    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('타인 프로필 조회', this.profile)
    // this.userId = this.$route.params.userId
    this.followState()
    this.getAccounts()
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
          this.activities = res.data.activities
          this.tastes = res.data.userInfo.tasteInfo
          this.nickname = res.data.nickname
          // this.followerList = res.data.from_user
          this.follower_cnt = res.data.from_user.length
          this.followingList = res.data.to_user
          console.log('취향', this.followerList)

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

    // 팔로우 상태 확인
    followState: function() {
      console.log('팔로우리스트3', this.following)
      axiosConnector.get(`account/follow/${this.$route.params.userId}`)
        .then((res) => {
          console.log('체크', res.data)
          if (res.data === 'ok') {
            this.follow = true
          }
          else {
            this.follow = false
          }
        })
        .catch((err) => {
          console.log('에러1')
        })
    },


    // [팔로우]
    followBtn: function() {
      axiosConnector.post(`account/follow/${this.$route.params.userId}`)
        .then(() => {
          this.follow = true
          this.follower_cnt++
        })
        .catch((err) => {
          console.log('에러111', err)
        })
    },

    // [언팔로우]
    unfollowBtn: function() {
      axiosConnector.delete(`account/follow/${this.$route.params.userId}`)
        .then(() => {
          this.follow = false
          this.follower_cnt--
        })
        .catch((err) => {
          console.log('에러222', err)
        })
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
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
  },

}
</script>

<style>

</style>
