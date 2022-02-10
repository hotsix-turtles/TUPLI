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
        class="d-flex-column text-center makeBtns"
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
          >
            게시글
          </v-btn>
        </div>
      </div>
    </div>
    <!-- 네브바 -->
    <div class="d-flex-column fixed-bottom-navbar">
      <div
        class="d-flex justify-space-around fixed-bottom-navbar navbar-background py-2"
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
        <div
          v-if="isLogin"
          class="clickable profile"
          :class="{ 'border-colored': selectedPage === 'Profile' }"
          @click="changeRouter('Profile')"
        >
          {{ profileImage }}
        </div>
        <div
          v-else
          class="clickable d-flex-column text-center"
          @click="changeRouter('Login')"
        >
          <v-icon :class="{ 'font-size' : true, colored: selectedPage === 'Login' }">
            mdi-account-circle-outline
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

    <Alarm />
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Alarm from '@/components/alarm/Alarm'

export default {
  name: 'NavbarBottom',
  components: {
    Alarm
  },
  data: function() {
    return {
      value: 1,
      isClickedMakeBtn: false,
      selectedPage: '',
    }
  },
  computed : {
    ...mapState([
      'isLogin',
      'profileImage',
    ]),
  },
  created: function () {
    console.log('route', this.$router.name)
  },
  methods: {
    toggle: function() {
      this.isClickedMakeBtn = !this.isClickedMakeBtn
    },
    goPlaylistForm: function() {
      this.$router.push({ name: 'PlaylistForm' })
      this.toggle()
    },
    goPlayroomForm: function() {
      this.$router.push({ name: 'PlayroomForm' })
      this.toggle()
    },
    // goBoardForm: function() {
    //   this.$router.push({ name: 'BoardForm' })
    //   this.toggle()
    // },
    changeRouter: function (pageName) {
      this.$router.push({ name: pageName })
      this.selectedPage = pageName
    }
  }
}
</script>

<style scoped>
  /* 만들기 버튼 일반 문서 흐름에서 제거 */
  .makeBtns {
    z-index: 10;
    position: fixed;
    bottom: 6vh;
    left: 50%;
    transform: translate(-50%, 0%);
  }

  .makeBtn {
    z-index: 9;
    font-size: 3.2rem !important;
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

  .profile {
    border-radius: 100%;
    border: solid 2px;
    border-color: lightgrey;
    width: 1.67rem;
    height: 1.67rem;
  }

</style>
