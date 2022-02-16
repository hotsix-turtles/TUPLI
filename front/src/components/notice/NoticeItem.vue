<template>
  <div>
    <div class="d-flex align-center px-4 my-3">
      <!-- 프로필 사진 -->
      <div
        class="d-flex justify-center align-center notice-img"
        style="background-color: yellow; border-radius: 100%;"
        @click="setProfile"
      >
        <img
          src="@/assets/tupli_logo2_dark.png"
          alt="profile img"
          width="30"
        >
      </div>

      <!-- [알림 종류] -->
      <!-- 팔로우 -->
      <div
        v-if="notice.type === 'follow'"
        class="mx-3 d-flex align-center"
      >
        <div
          class="d-flex flex-column"
        >
          <div class="notice-content">
            <p
              class="mb-0"
              @click="setProfile"
            >
              {{ notice.from }}
              님이&nbsp;
              {{ nickname }}님을 팔로우합니다.
            </p>
          </div>
          <p
            class="mb-0"
          >
            15분
          </p>
        </div>
        <!-- 팔로우 버튼 -->
        <div v-if="!meCheck">
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
      </div>

      <!-- 플레이룸 초대 -->
      <div
        v-else-if="notice.type === 'invite'"
        class="mx-3 align-center"
      >
        <div class="d-flex">
          <div class="d-flex flex-column mb-0">
            <div class="d-flex flex-wrap notice-content">
              <p
                class="mb-0"
                @click="setProfile"
              >
                {{ notice.from }}님이&nbsp;{{ nickname }}님을 초대하였습니다.
                플레이룸 제목이 들어갈 공간입니다
              </p>
            </div>
            <p
              class="mb-0"
            >
              15분
            </p>
          </div>
          <div>
            <v-btn
              class="ml-4 notice-btn"
              @click="acceptInvite"
            >
              수락
            </v-btn>
          </div>
        </div>
      </div>

      <!-- 플레이룸 개설 알림 -->
      <div
        v-else-if="notice.type === 'playroomMake'"
        class="mx-3  align-center"
      >
        <div class="d-flex mb-0">
          <p
            class="mb-0"
            @click="setProfile"
          >
            {{ notice.from }}님이&nbsp;{{ nickname }}님의 플레이리스트로 플레이룸을 생성하였습니다.
          </p>
        </div>
        <div>
          <p
            class="mb-0"
          >
            15분
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'NoticeItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    notice: { type: Object },
  },
  data: function() {
    return {
      noticeType: '',
      follow: false,
      meCheck: false,
    }
  },
  computed: {
    ...mapState(['nickname'])
  },
  created: function() {
    this.checkFollow()
  },
  methods: {
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 타 유저 프로필로 가기
    setProfile: function() {
      // 만약 클릭한 사람이 나라면
      axiosConnector.get(`userinfo/${this.notice.fromId}`)
        .then((res) => {
          if (res.data.meCheck === false) {  // 내가 아니라면, 프로필로 !
            // console.log('타인 프로필', res.data.meCheck)
            this.$router.push({ name: 'Profile', params: { userId : this.notice.fromId }})
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
    // 팔로워가 본인일 때
      axiosConnector.get(`account/follow/${this.notice.fromId}`)
        .then((res) => {
          console.log('체크', res.data)
          // 팔로워 상태일 때
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
      axiosConnector.post(`account/follow/${this.notice.fromId}`)
        .then(() => {
          this.follow = true
        })
        .catch((err) => {
          console.log('에러111', err)
        })
    },

    // [언팔로우]
    unfollowBtn: function() {
      axiosConnector.delete(`account/follow/${this.notice.fromId}`)
        .then(() => {
          this.follow = false
        })
        .catch((err) => {
          console.log('에러222', err)
        })
    },

    // 플레이룸 수락
    acceptInvite: function() {
      this.$router.push({ name: 'PlayroomDetail', params: { id: this.notice.routeId }})
    }
  },
}
</script>

<style scoped>
p {
  font-size: 10px;
}

/* 알림 이미지 */
.notice-img {
  width: 44px ;
  height: 44px;
}

/* 알림 내용 */
.notice-content {
  margin-right: 10px;
  overflow: hidden;
  line-height: 1.2em;
  max-height: 2.4em;
}

/* 알림 버튼 */
.notice-btn {
  width: 20px;
}
</style>
