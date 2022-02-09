<template>
  <v-app>
    <div class="d-flex justify-center mt-5 px-4">
      <img
        src="@/assets/playroom_image.png"
        alt=""
        style="width: 126px; height: 71px"
      >
      <div
        class="d-flex flex-column align-start mx-2"
        style="width: 200px;"
      >
        <p class="mb-0">
          플레이룸 제목 1
        </p>
        <p class="mb-0">
          작성자 이름
        </p>
        <p class="mb-0">
          태그
        </p>
      </div>


      <!-- menu 형태의 더보기 버튼 -->
      <v-menu
        transition="slide-y-transition"
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-icon
            class="pb-10"
            v-bind="attrs"
            @click.stop="dialog = true"
            v-on="on"
          >
            mdi-dots-vertical
          </v-icon>
        </template>
        <v-list>
          <v-list-item dense>
            플레이리스트 생성
          </v-list-item>
          <v-list-item dense>
            게시글 작성
          </v-list-item>
          <v-list-item dense>
            좋아요 취소
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'PlayroomItem',
  data: function() {
    return {
      options: {
        addPlaylist: null,
        writePost: null,
        like: true,
      },

    }
  }
}
</script>

<style>

</style>
