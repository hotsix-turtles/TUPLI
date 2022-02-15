<template>
  <div>
    <div class="d-flex justify-center mt-5 px-4">
      <img
        :src="thumbnail"
        alt="playroom img"
        style="width: 126px; height: 71px"
        @click="goPlayroom"
      >
      <div
        class="d-flex flex-column align-start mx-2"
        style="width: 200px;"
      >
        <p
          class="mb-0"
          @click="goPlayroom"
        >
          {{ playroomlist.title }}
        </p>
        <p
          class="mb-0"
          @click="setProfile"
        >
          {{ playroomlist.user.nickname }}
        </p>
        <div class="d-flex">
          <p
            v-for="tag in tags"
            :key="tag.id"
            class="mb-0 main-tag"
          >
            {{ tag }}
          </p>
        </div>
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
            @click="makePlaylist(playroomlist.id)"
          >
            플레이리스트 생성
          </v-list-item>
          <v-list-item
            dense
            @click="makeBoard(playroomlist.id)"
          >
            게시글 작성
          </v-list-item>
          <v-list-item
            dense
            @click="unLike"
          >
            좋아요 취소
          </v-list-item>
        </v-list>
      </v-menu>



      <!-- dialog 형태의 더보기 버튼 -->

      <!-- <v-icon
        class="pb-10"
        @click.stop="dialog = true"
        >
        mdi-dots-vertical
      </v-icon>
      <v-dialog
        v-model="dialog"
        max-width="200"
      >
        <v-card>
          <v-card-actions>
            <div class="d-flex flex-column align-center">
              <v-btn
                color="green darken-1"
                text
                @click="dialog = false"
              >
                플레이리스트 생성하기
              </v-btn>

              <v-btn
                color="green darken-1"
                text
                @click="dialog = false"
              >
                게시글 작성하기
              </v-btn>
              <v-btn
                color="green darken-1"
                text
                @click="dialog = false"
              >
                좋아요 취소
              </v-btn>
            </div>
          </v-card-actions>
        </v-card>
      </v-dialog> -->
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'PlayroomItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    playroomlist: { type: Object },
    thumbnail: { type: String, default: '' }
  },
  data: function() {
    return {
      tags: [],
      options: {
        addPlaylist: null,
        writePost: null,
        like: true,
      },
      // dialog: false,  // dialog 사용

    }
  },
  created: function() {
    this.getTag()
  },
  methods: {
    ...mapActions( 'account',[
      'makePlaylist', 'makeBoard'
    ]),
    ...mapActions(
      'playroom', [
        'unlikePlayroom',
      ]),
    // 태그
    getTag: function() {
      // console.log('태그', this.playroomlist.tags)
      this.tags = this.playroomlist.tags.split(',')
      // console.log('태그2', this.tags)
    },
    // 좋아요 취소
    unLike: function() {
      this.unlikePlayroom(this.playroomlist.id)
    },
    // 플레이룸 상세로 가기
    goPlayroom: function() {
      console.log( '플레이룸 상세', this.playroomlist.id )
      this.$router.push({ name: 'PlayroomDetail', params: { id : this.playroomlist.id }})
    },
    // 타 유저 프로필로 가기
    setProfile: function() {
      console.log('타인 프로필')
      this.$router.push({ name: 'Profile', params: { userId : this.playroomlist.user.userSeq }})
    },
  }
}
</script>

<style>

</style>
