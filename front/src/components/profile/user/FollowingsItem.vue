<template>
  <div>
    <div
      class="d-flex justify-center mx-4 my-2"
      style="width: 360px;"
    >
      <div class="d-flex align-center">
        <div
          class="d-flex align-center"
          @click="goProfile"
        >
          <div
            class="d-flex profile-img-medium"
          >
            <img
              :src="ImgUrl(following.profileImage)"
              alt="profile img"
            >
          </div>
          <p
            class="mb-0 mx-3"
          >
            {{ following.nickname }}&nbsp;
          </p>
        </div>
        <!-- 팔로우 버튼 -->
        <div>
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
        </div>
        <div>
          내가 팔로워일 때 팔로워 버튼 없애기
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'FollowingsItem',
  props: {
    following: { type: Object, default() {} }
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
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 해당 유저 프로필로 가기
    goProfile: function() {
      console.log( '타인 프로필', this.following.userSeq )
      if (this.userId === this.following.userSeq) {
        this.$router.push({ name: 'MyProfile'})
      }
      else {
        this.$router.push({ name: 'Profile', params: { userId : this.following.userSeq }})
      }
    },

    // 팔로우 여부
    checkFollow: function() {
      axiosConnector.get(`account/follow/${this.following.userSeq}`)
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
      axiosConnector.post(`account/follow/${this.following.userSeq}`)
        .then(() => {
          this.follow = true
        })
        .catch((err) => {
          console.log('에러111', err)
        })
    },

    // [언팔로우]
    unfollowBtn: function() {
      axiosConnector.delete(`account/follow/${this.following.userSeq}`)
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
