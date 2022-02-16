<template>
  <v-app>
    <div
      class="d-flex justify-space-between sticky-header px-5 pb-4 pt-5"
      style="background-color: white;"
    >
      <img
        src="@/assets/tupli_logo_dark.png"
        class="home-logo"
      >
      <!-- 알림 아이콘 -->
      <v-icon
        color="#000000"
        @click="goNotice"
      >
        mdi-bell
      </v-icon>
    </div>
    <div class="d-flex-column">
      <hr style="width: 100%; height: 1px; background-color: #f1f1f4 !important; ">
      <div
        v-if="authToken"
        class="d-flex mx-5 mt-4 mb-3"
      >
        <p
          class="font-weight-bold color-main"
          style="margin-right: 1px;"
          bold
        >
          {{ nickname }}
        </p>
        <p class="">
          님이 좋아하는&nbsp;
        </p>
        <p class="color-main">
          오늘의 추천 컨텐츠
        </p>
      </div>
      <div
        v-else
        class="d-flex mx-5 mt-4 mb-3"
      >
        <p
          class="font-weight-bold color-main"
          style="margin-right: 1px;"
          bold
        >
          게스트
        </p>
        <p class="">
          님이 좋아하는&nbsp;
        </p>
        <p class="color-main">
          오늘의 추천 컨텐츠
        </p>
      </div>
    </div>

    <div>
      <main-list />
    </div>
  </v-app>
</template>

<script>
import MainList from '@/components/home/MainList'

import { mapState } from 'vuex'

export default {
  name: 'Home',

  components: {
    MainList,
  },

  computed: {
    ...mapState(['authToken', 'nickname'])
  },

  methods: {
    // 로그인 상태 확인
    goNotice: function() {
      if (this.authToken) {
        this.$router.push({ name: 'Notice' })
      }
      else {
        this.$router.push({ name: 'Login' })
      }
    }
  },

}
</script>

<style>

  .home-logo {
    width: 100px;
    height: 28px;
  }

  .sticky-header {
    position: sticky;
    top: 0;
    z-index: 1;
  }
</style>
