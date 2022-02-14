<template>
  <div>
    <div
      class="d-flex mt-5"
    >
      <!-- board 정보 -->
      <div class="d-flex">
        <div class="d-flex">
          <img
            style="border-radius: 100px; margin: 10px;"
            :src="ImgUrl(boardlist.user.profileImage)"
            alt="프로필 사진"
            width="50"
            height="50"
            @click="setProfile"
          >
        </div>
        <div class="d-flex flex-column justify-center align-start">
          <p
            class="mb-0"
            @click="goBoard"
          >
            {{ boardlist.content }}
          </p>
          <p
            class="mb-0"
            @click="setProfile"
          >
            {{ boardlist.user.nickname }}
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
            @click="unLike"
          >
            좋아요 취소
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'
import { mapActions } from 'vuex'

export default {
  name: 'PostItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    boardlist: { type: Object },
  },
  data: function() {
    return{
      options: {
        like: true,
      },
    }
  },
  created: function() {
    console.log('타인 프로필', this.boardlist )
  },
  methods: {
    ...mapActions(
      'board', [
        'unlikeBoard'
      ]),
    // 타 유저 프로필로 가기
    setProfile: function() {
      console.log('타인 프로필')
      this.$router.push({ name: 'Profile', params: { userId : this.boardlist.user.userSeq }})
    },
    // 해당 게시글로 가기
    goBoard: function() {
      console.log('해당 게시글로 가기', this.boardlist.user.userSeq )
      this.$router.push({ name: 'BoardDetail', params: { boardId : this.boardlist.id }})
    },
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 좋아요 취소
    unLike: function() {
      this.unlikeBoard(this.boardlist.id)
    }

  }
}
</script>

<style>

</style>
