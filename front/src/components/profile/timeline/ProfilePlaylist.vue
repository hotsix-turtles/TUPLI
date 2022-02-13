<template>
  <v-app>
    <h1>활동 페이지</h1>
    <!-- 플레이룸 -->
    <div>
      <!-- playroom 썸네일 -->
      <img
        src=""
        alt="썸네일"
        width="390"
        height="219"
        @click="goPlayroom"
      >
      <!-- playroom 정보 -->
      <div class="d-flex">
        <img
          style="border-radius: 100px; margin: 10px;"
          src=""
          alt="프로필 사진"
          width="50"
          height="50"
          @click="setProfile"
        >
        <div
          class="d-flex flex-column align-start"
        >
          <h4
            class="mb-0 mt-2"
            @click="goPlayroom"
          >
            제목
          </h4>
          <p
            class="mb-0"
            @click="setProfile"
          >
            닉네임
          </p>
          <div
            class="d-flex"
          >
            <p
              class="mb-0 main-tag"
            >
              태그
            </p>
          </div>
        </div>
        <!-- 리액션 아이콘 -->
        <div class="d-flex mr-2 align-center">
          <div class="d-flex flex-column align-center mx-1">
            <v-icon>
              mdi-cards-heart-outline
            </v-icon>
            <p>4.2K</p>
          </div>
          <div class="d-flex flex-column align-center mx-1">
            <v-icon>
              mdi-album
            </v-icon>
            <p>PLI</p>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'ProfilePlaylist',
}
</script>

<style>

</style>
