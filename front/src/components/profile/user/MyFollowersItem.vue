<template>
  <div>
    <div
      class="d-flex justify-center align-center"
      style="margin-top:5px; margin-bottom:5px;"
    >
      <div
        class="profile-img-medium"
        @click="goProfile"
      >
        <img
          :src="ImgUrl(follower.profileImage)"
          alt="profile img"
        >
      </div>
      <p
        class="mb-0 mx-3"
        style="font-size: 14px; width:40%;"
        @click="goProfile"
      >
        {{ follower.nickname }}&nbsp;
      </p>
      <btn
        v-if="!follow"
        class="follow-btn-true"
        @click="followBtn"
      >
        &nbsp;팔로우&nbsp;
      </btn>
      <btn
        v-else
        class="follow-btn-false"
        @click="unfollowBtn"
      >
        &nbsp;팔로잉&nbsp;
      </btn>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'FollowersItem',
  props: {
    follower: { type: Object, default() {} }
  },
  data: function() {
    return {
      follow: false,
    }
  },
  created: function() {
    this.checkFollow()
  },
  methods: {
    // 해당 유저 프로필로 가기
    goProfile: function() {
      console.log( '타인 프로필', this.follower.userSeq )
      if (this.userId === this.follower.userSeq) {
        this.$router.push({ name: 'MyProfile'})
      }
      else {
        this.$router.push({ name: 'Profile', params: { userId : this.follower.userSeq }})
      }
    },
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 팔로우 여부
    checkFollow: function() {
      axiosConnector.get(`account/follow/${this.follower.userSeq}`)
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
      axiosConnector.post(`account/follow/${this.follower.userSeq}`)
        .then(() => {
          this.follow = true
        })
        .catch((err) => {
          console.log('에러111', err)
        })
    },

    // [언팔로우]
    unfollowBtn: function() {
      axiosConnector.delete(`account/follow/${this.follower.userSeq}`)
        .then(() => {
          this.follow = false
        })
        .catch((err) => {
          console.log('에러222', err)
        })
    },
  }

}
</script>

<style>

</style>
