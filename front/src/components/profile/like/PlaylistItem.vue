<template>
  <div>
    <div class="d-flex align-center">
      <div class="playlist-cd-small mx-4 mb-2">
        <img
          :src="thumbnail"
          alt="playlist image"
        >
        <div />
      </div>
      <div
        class="d-flex flex-column"
        style="width: 200px;"
      >
        <p class="mb-0">
          {{ playlistlist.title }}
        </p>
        <p class="mb-0">
          {{ playlistlist.nickName }}
        </p>
        <p class="mb-0">
          {{ playlistlist.tag }}
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
          <v-list-item
            dense
            @click="makePlayroom(playlistlist.id)"
          >
            플레이룸 생성
          </v-list-item>
          <v-list-item
            dense
            @click="makeBoard(playlistlist.id)"
          >
            게시글 작성
          </v-list-item>
          <v-list-item
            dense
            @click="cancelLike(playlistlist.id)"
          >
            좋아요 취소
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'PlayroomItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    playlistlist: { type: Object },
    thumbnail: { type: String, default: '' },
  },
  data: function() {
    return {
      options: {
        addPlaylist: null,
        writePost: null,
        like: true,
      },
    }
  },
  methods: {
    ...mapActions('account', [
      'makePlayroom', 'makeBoard', 'cancelLike'
    ]),
    // 태그
    getTag: function() {
      console.log('태그', this.playroomlist.tags)
      this.tags = this.playroomlist.tags.split(',')
      console.log('태그2', this.tags)
    },
  }
}
</script>

<style>

</style>
