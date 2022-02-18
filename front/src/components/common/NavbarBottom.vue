np/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <!-- 만들기 버튼 나오면서 나오는 검정 배경 -->
    <div>
      <div
        v-if="isClickedMakeBtn"
        class="dark-background"
        @click="toggle"
      />
      <!-- 만들기 버튼 (플레이리스트/플레이룸/게시글) -->
      <div
        v-if="isClickedMakeBtn"
        class="d-flex-column text-center makeBtns animate__animated animate__slideInUp"
      >
        <div class="my-5">
          <v-btn
            rounded
            @click="goPlaylistForm"
          >
            플레이리스트
          </v-btn>
        </div>
        <div class="my-5">
          <v-btn
            rounded
            @click="goPlayroomForm"
          >
            플레이룸
          </v-btn>
        </div>
        <div class="my-5">
          <v-btn
            rounded
            @click="goBoardForm"
          >
            게시글
          </v-btn>
        </div>
      </div>
    </div>
    <!-- 네브바 -->
    <div class="d-flex-column fixed-bottom-navbar">
      <div
        class="d-flex justify-space-around align-center fixed-bottom-navbar navbar-background pb-2"
      >
        <!-- 홈 버튼 -->
        <div
          class="clickable d-flex-column text-center"
          @click="changeRouter('Home')"
        >
          <v-icon
            :class="{ 'font-size' : true, colored: selectedPage === 'Home' }"
          >
            mdi-home-variant-outline
          </v-icon>
        </div>

        <!-- 검색 버튼 -->
        <div
          class="clickable d-flex-column text-center"
          @click="changeRouter('Search')"
        >
          <v-icon :class="{ 'font-size' : true, colored: selectedPage === 'Search' }">
            mdi-magnify
          </v-icon>
        </div>

        <div
          class="clickable mx-3"
        />

        <!-- 탐색 버튼 -->
        <div
          class="clickable d-flex-column text-center"
          @click="changeRouter('Category')"
        >
          <v-icon :class="{ 'font-size' : true, colored: selectedPage === 'Category' }">
            mdi-compass-outline
          </v-icon>
        </div>

        <!-- 프로필 버튼 -->
        <img
          v-if="isLogin && is_vip == 'Y'"
          :src="profileImg ? profileImg : 'data:image/jpeg;base64,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'"
          class="clickable profile"
          :class="{ 'border-colored-img': selectedPage === 'MyProfile' }"
          @click="changeRouter('MyProfile')"
        >
        <div
          v-else-if="isLogin"
          class="clickable d-flex-column text-center"
          @click="changeRouter('MyProfile')"
        >
          <v-icon :class="{ 'font-size' : true, colored: selectedPage === 'MyProfile' }">
            mdi-account-circle-outline
          </v-icon>
        </div>
        <div
          v-else
          class="clickable d-flex-column text-center"
          @click="changeRouter('Login')"
        >
          <v-icon :class="{ 'font-size' : true, colored: selectedPage === 'Login' }">
            mdi-login
          </v-icon>
        </div>
      </div>
      <!-- 만들기 버튼 -->
      <div>
        <v-icon
          color="#5B5C9D"
          :class="{ makeBtn: true, rotate: isClickedMakeBtn }"
          @click="toggle"
        >
          mdi-plus-circle
        </v-icon>
      </div>
    </div>

    <alert-dialog
      v-model="followDialogState"
      :title="followDialogTitle"
      max-width="350"
      timeout="2000"
      @timeout="hideFollowDialog"
    />
    <login-dialog
      :show="showLoginDialog"
      @on-click="showLoginDialog = false"
    />
    <Alarm />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Alarm from '@/components/alarm/Alarm'
import LoginDialog from './LoginDialog.vue'

export default {
  name: 'NavbarBottom',
  components: {
    Alarm,
    LoginDialog,
  },
  data: function() {
    return {
      value: 1,
      isClickedMakeBtn: false,
      selectedPage: '',
      showLoginDialog: false,
    }
  },
  computed : {
    ...mapState([
      'isLogin',
      'profileImage',
      'is_vip',
      'followDialogTitle',
      'followDialogState'
    ]),
  },
  created: function () {
    console.log('route', this.$router.name)
    this.$store.dispatch('checkLogin')  // 토근 유효 확인
  },
  methods: {
    toggle: function() {
      this.isClickedMakeBtn = !this.isClickedMakeBtn
    },
    goPlaylistForm: function() {
      this.toggle()
      if (this.isLogin) {
        this.$router.push({ name: 'PlaylistForm' })
      } else {
        this.showLoginDialog = true
      }
    },
    goPlayroomForm: function() {
      this.toggle()
      if (this.isLogin) {
        this.$router.push({ name: 'PlayroomForm' })
      } else {
        this.showLoginDialog = true
      }
    },
    goBoardForm: function() {
      this.toggle()
      if (this.isLogin) {
        this.$router.push({ name: 'BoardForm' })
      } else {
        this.showLoginDialog = true
      }
    },
    changeRouter: function (pageName) {
      this.$router.push({ name: pageName })
      this.selectedPage = pageName
    },
    ...mapActions(['showFollowDialog','hideFollowDialog']),
  }
}
</script>

<style scoped>
  /* 만들기 버튼 일반 문서 흐름에서 제거 */
  .makeBtns {
    z-index: 2;
    position: fixed;
    bottom: 9vh;
    width: 100% !important;
  }

  .makeBtn {
    z-index: 5;
    font-size: 3.6rem !important;
    position: fixed;
    bottom: 0.8vh;
    left: 50%;
    transform: translate(-50%, 0%);
  }

  .v-btn {
    font-size: 16px !important;
    color: #5B5C9D !important;
    font-weight: 600 !important;
  }

  .font-size {
    font-size: 1.9rem !important;
  }

  .colored {
    color: black !important;
  }

  .border-colored {
    border-color: black !important;
  }

  .border-colored-img {
    border: solid 2px;
    border-color: #5B5C9D !important;
  }

  .profile {
    border-radius: 100%;
    width: 1.67rem;
    height: 1.67rem;
  }

</style>
