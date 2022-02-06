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
    <v-container>
      <div class="d-flex mt-4 mx-4">
        <p
          class="deep-purple--text font-weight-bold"
          bold
        >
          라이언
        </p>
        <p>님이 좋아하는&nbsp;</p>
        <p class="deep-purple--text">
          오늘의 추천 컨텐츠
        </p>
      </div>
    </v-container>

    <div>
      메인 컨텐츠 나열
      <div style="width: 390px; height:300px; background-color: green; padding: 5px;" />
      <div style="width: 390px; height:300px; background-color: yellow; padding: 5px;" />
      <div style="width: 390px; height:300px; background-color: green; padding: 5px;" />
      <div style="width: 390px; height:300px; background-color: yellow; padding: 5px;" />
    </div>
    <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
    </router-link>
    <router-link to="/signup2">
      Signup
    </router-link>
    <router-link to="/profile">
      Profile
    </router-link>
  </v-app>
</template>

<script>
export default {
  name: 'Home',

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
  }
</style>
