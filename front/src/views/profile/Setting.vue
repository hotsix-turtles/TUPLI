<template>
  <v-app>
    <h1>설정 페이지</h1>

    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <router-link
          to="/profile"
          class="no-background-hover-text"
        >
          <v-icon
            color="#5B5C9D"
          >
            mdi-chevron-left
          </v-icon>
        </router-link>
        <h4 class="">
          설정
        </h4>
      </v-row>
    </v-container>

    <div>
      <hr>
      <router-link
        to="/like"
        class="no-background-hover-text"
      >
        <p>좋아요한 게시물</p>
      </router-link>
      <hr>
      <router-link
        to="/save"
        class="no-background-hover-text"
      >
        <p>저장한 게시물</p>
      </router-link>
      <hr>
      <router-link
        to="/history"
        class="no-background-hover-text"
      >
        <p>플레이룸 참여 기록</p>
      </router-link>
      <hr>
      <router-link
        to="payment"
        class="no-background-hover-text"
      >
        <p>결제 서비스</p>
      </router-link>
      <hr>
      <router-link
        to="notice"
        class="no-background-hover-text"
      >
        <p>초대/알림 설정</p>
      </router-link>
      <hr>
      <router-link
        to="terms"
        class="no-background-hover-text"
      >
        <p>약관</p>
      </router-link>
      <hr>
      <router-link
        to="/changepassword"
        class="no-background-hover-text"
      >
        <p>비밀번호 변경</p>
      </router-link>
      <hr>
      <p>로그아웃</p>
      <hr>
      <p>회원 탈퇴</p>
    </div>
  </v-app>
</template>

<script>
export default {

}
</script>

<style scoped>
  p { margin: 10px;
    padding-top: 5px;
    padding-bottom: 5px;
    text-decoration-color: black !important;
  }

  .no-background-hover-text {
    text-decoration: none !important;
    text-decoration-color: black !important;
  }
</style>
