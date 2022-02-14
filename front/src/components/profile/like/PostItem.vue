<template>
  <v-app>
    <div
      class="d-flex flex-column mt-5"
    >
      <!-- playlist 정보 -->
      <div class="d-flex">
        <div class="d-flex align-start">
          <img
            style="border-radius: 100px; margin: 10px;"
            :src="userProfileImg"
            alt="프로필 사진"
            width="50"
            height="50"
            @click="setProfile"
          >
        </div>
        <div class="d-flex flex-column align-start">
          <p class="mb-0">
            {{ content.content }}
          </p>
          <p
            class="mb-0"
            @click="setProfile"
          >
            {{ boardlist.nickName }}
          </p>
          <div class="d-flex align-center">
            <div class="d-flex align-center">
              <v-icon>
                mdi-cards-heart-outline
              </v-icon>
              <p>{{ boardlist.likesCnt }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>    <div class="d-flex">
      <div class="d-flex">
        <img
          :src="thumbnail"
          alt=""
        >
        <p>{{ boardlist.nickName }}</p>
      </div>
      <div>
        <p>{{ boardlist.content }}</p>
      </div>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'PostItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    boardlist: { type: Object },
    thumbnail: { type: String, default: '' },
  }
}
</script>

<style>

</style>
