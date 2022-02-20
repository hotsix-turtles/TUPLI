<template>
  <div>
    <div class="d-flex justify-center align-center mt-2">
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
      <div v-if="!meCheck">
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
      <div v-else>
        <button class="follow-btn-none">
          프로필
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'
import { mapState, mapActions } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'


export default {
  name: 'FollowersItem',
  props: {
    follower: { type: Object, default() {} },
  },
  data: function() {
    return {
      ...mapState(['authToken', 'userSeq']),
      follow: false,
      meCheck: false,
    }
  },
  created: function() {
    // this.getUserInfo(this.authToken)
    this.checkFollow()
  },
  methods: {
    ...mapActions(['getUserInfo']),
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 해당 유저 프로필로 가기
    goProfile: function() {
      // 만약 클릭한 사람이 나라면
      axiosConnector.get(`userinfo/${this.follower.userSeq}`)
        .then((res) => {
          if (res.data.meCheck === false) {  // 내가 아니라면, 프로필로 !
            // console.log('타인 프로필', res.data.meCheck)
            this.$router.push({ name: 'Profile', params: { userId : this.userSeq }})
          }
          else if (res.data.meCheck === true) {  // 나라면
            // console.log('내 프로필', res.data.meCheck)
            this.$router.push({ name: 'MyProfile'})
          }
          else {  // 로그인
            this.$router.push({ name: 'Login'})
          }
        })
        .catch((err) => {
          console.log('에러', err)
        })
    },

    // 팔로우 여부
    checkFollow: function() {
      axiosConnector.get(`account/follow/${this.follower.userSeq}`)
        .then((res) => {
          console.log('체크', res.data)
          if (res.data === 'ok') {
            this.follow = true
          }
          else if (res.data === 'me') {
            this.meCheck = true
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
