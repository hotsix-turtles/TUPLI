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
        @click="$router.push({ name: 'Notice' })"
      >
        mdi-bell
      </v-icon>
    </div>
    <div>
      <div class="d-flex mx-4 my-4">
        <p
          class="deep-purple--text font-weight-bold mb-0"
          bold
        >
          {{ nickname }}
        </p>
        <p class="mb-0">
          님이 좋아하는&nbsp;
        </p>
        <p class="mb-0 deep-purple--text">
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

  }

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
