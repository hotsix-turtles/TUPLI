<template>
  <v-app>
    <div>
      <div
        class="d-flex justify-end mt-5"
      />
      <v-icon
        size="20"
        color="black"
        class="px-2"
        @click="$router.go(-1)"
      >
        mdi-arrow-left
      </v-icon>
      <div>
        <div class="d-flex flex-column align-center">
          <img
            class="my-3 profile-img-large"
            :src="ImgUrl(profile.profileImage)"
            alt=""
          >
          <p
            class="text-center pt-2 pb-1"
            style="font-weight:700;"
          >
            {{ profile.nickname }}
          </p>
          <div class="d-flex align-center">
            <p class="mt-1 profile-text">
              {{ profile.introduction }}
            </p>
          </div>
        </div>
        <div class="d-flex justify-center mt-2 mb-3">
          <div
            class="d-flex mx-3"
            @click="followList"
          >
            <p class="mr-2 profile-text">
              팔로워
            </p>
            <p class="profile-text">
              {{ follower_cnt }}
            </p>
          </div>
          <div
            class="hr-vertical"
            style="margin-left:2px; margin-right:2px;"
          />
          <div
            class="d-flex mx-3 profile-text"
            @click="followList"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p class="profile-text">
              {{ followingList.length }}
            </p>
          </div>
        </div>
        <div class="d-flex justify-center">
          <v-btn
            v-if="!follow"
            class="text-center mx-2 white--text"
            color="#5B5C9D"
            rounded
            style="font-size: 13px; padding: 8px 14px 8px 14px;"
            small
            @click="followBtn"
          >
            &nbsp;팔로우&nbsp;
          </v-btn>
          <v-btn
            v-else
            class="text-center mx-2 dark--text"
            color="#5B5C9D"
            rounded
            style="font-size: 13px; padding: 8px 14px 8px 14px;"
            small
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
            style="font-size: 13px; padding: 8px 14px 8px 14px;"
            small
            @click="wacthTogether"
          >
            같이 시청하기
          </v-btn>
        </div>
      </div>
    </div>

    <div class="d-flex justify-space-around mt-1">
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
            class="pt-4"
            :tastes="tastes"
            :nickname="nickname"
            :user-id="userId"
          />
        </v-tab-item>
      </v-tabs>
    </div>

    <!--무한스크롤 -->
    <infinite-loading
      spinner="waveDots"
      @infinite="getAccounts"
    >
      <div slot="no-results" />
      <div slot="no-more" />
    </infinite-loading><br><br>
  </v-app>
</template>

<script>
import ProfilePlaylist from '@/components/profile/timeline/ProfilePlaylist.vue'
import ProfileTaste from '@/components/profile/timeline/ProfileTaste.vue'
import InfiniteLoading from "vue-infinite-loading"

import { getImage } from '../../utils/utils'
import axiosConnector from '@/utils/axios-connector.js'

import { mapActions, mapState } from 'vuex'

export default {
  components: { ProfileTaste, ProfilePlaylist, InfiniteLoading, },
  data: function() {
    return {
      profile: [],
      follow: false,
      followerList: [], // 팔로잉 하면 팔로워가 늘어남.
      followingList: [],
      userId: '',
      activities: [], // 무한스크롤 적용
      tastes: '',
      nickname: '',

      // 무한스크롤용
      page: 0,
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'following', 'followers'])
  },
  created: function() {
    console.log('타인 프로필 조회', this.$route.params.userId)
    console.log('타인 프로필 조회', this.profile)
    this.followState()
  },
  methods: {
    ...mapActions('account', ['follow', 'unfollow', 'getAccounts']),
    // [조회]
    getAccounts: function ($state) {
      console.log('getAccounts params')
      const params = {
        paged: true,
        page: this.page,
        size: 5,
      }
      axiosConnector.get(`userinfo/${this.$route.params.userId}`, {
        params
      })
        .then((res) => {
          if (this.page === 0) {
            this.page++
            console.log('성공적 프로필', res.data)
            this.profile = res.data
            this.activities.push(...res.data.activities)
            this.tastes = res.data.userInfo.tasteInfo
            this.nickname = res.data.nickname
            this.follower_cnt = res.data.from_user.length
            this.followingList = res.data.to_user
            console.log('취향', this.followerList)
            $state.loaded()
          } else if (res.data.activities.length) {
            this.page++
            this.activities.push(...res.data.activities)
            $state.loaded()
          } else {
            $state.complete()
          }
        })
        .catch((err) => {
          console.log('에러', err)
          $state.complete()
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
