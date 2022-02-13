<template>
  <div>
    <!-- 플레이룸 -->
    <div
      v-if="content.type == 'playroom'"
      class="mb-5"
    >
      <!-- playroom 썸네일 -->
      <img
        src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
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
        >
        <div class="d-flex flex-column align-start">
          <h4 class="mb-0 mt-2">
            {{ content.videos.title }}
          </h4>
          <h5 class="mb-0">
            {{ content.nickName }}
          </h5>
          <div
            class="d-flex"
          >
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

    <!-- 플레이리스트 -->
    <div
      v-else-if="content.type == 'playlist'"
      class="mb-5"
    >
      <!-- playlist 썸네일 -->
      <span
        class="playlist-cd-case"
      >
        <div id="case" />
        <img
          src="https://i.ytimg.com/vi/aaostppQY78/hqdefault.jpg?sqp=-oaymwEcCOADEI4CSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLD3YLA-34TNe_OXh3XB4jidbDX1aQ"
          alt="썸네일"
        >
        <div />
      </span>
      <!-- playroom 정보 -->
      <div class="d-flex">
        <img
          style="border-radius: 100px; margin: 10px;"
          src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
          alt="프로필 사진"
          width="50"
          height="50"
          @click="$router.push({ name: 'Profile' })"
        >
        <div class="d-flex flex-column align-start">
          <h4 class="mb-0 mt-2">
            {{ content.title }}
          </h4>
          <p class="mb-0">
            {{ content.nickName }}
          </p>
          <div class="d-flex">
            <p class="main-tag">
              #게임
            </p>
            <p class="main-tag">
              #League of Legend
            </p>
          </div>
        </div>
        <div class="d-flex mr-2 align-center">
          <div class="d-flex flex-column align-center mx-1">
            <v-icon>
              mdi-cards-heart-outline
            </v-icon>
            <p>4.2K</p>
          </div>
          <div class="d-flex flex-column align-center mx-1">
            <v-icon>
              mdi-video-plus-outline
            </v-icon>
            <p>PLI</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 게시글 -->
  </div>
</template>

<script>
export default {
  name: 'MainItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    content: { type: Array },
  },
  data: function() {
    return {
      thumbnailImage: '',
      tags: [],
    }
  },
  created: function() {
    console.log('내용보기', this.content)
  },
  methods: {
    getTag: function() {
      this.content.tags
      this.tags = tags
    }
  },
}
</script>

<style >


</style>
