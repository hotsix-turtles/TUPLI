<template>
  <div>
    <div class="mb-5">
      <!-- playroom 썸네일 -->
      <img
        src="https://i.ytimg.com/vi/aaostppQY78/hqdefault.jpg?sqp=-oaymwEcCOADEI4CSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLD3YLA-34TNe_OXh3XB4jidbDX1aQ"
        alt="썸네일"
        width="390"
        height="219"
      >
      <!-- playroom 정보 -->
      <div class="d-flex">
        <img
          style="border-radius: 100px; margin: 10px;"
          src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
          alt="프로필 사진"
          width="50"
          height="50"
          @click="$router.push({ name: 'PersonFollow'})"
        >
        <div class="d-flex flex-column align-start">
          <h4 class="mb-0 mt-2">
            롤 시작할 롤린이들 모여! 다이아 유저의 강의보면서 같이 큐 돌릴 사람 환영
          </h4>
          <h5 class="mb-0">
            포로멜 (meloooong)
          </h5>
          <div class="d-flex">
            <h5>#게임</h5>
            <h5>&nbsp;#League of Legend</h5>
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
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainPlayroomItem'
}
</script>

<style>

</style>
