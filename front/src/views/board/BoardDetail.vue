<template>
  <div class="">
    <!-- 상단 뒤로가기, 좋아요, 댓글, 점3 등 -->
    <div class="fixed-top d-flex justify-space-between light-background navbar-top">
      <back-only />
      <div class="d-flex me-5">
        <!-- 좋아요 -->
        <div>
          <div
            v-if="isLiked"
            @click="unlikeBoard(boardDetail.id)"
          >
            <v-icon>mdi-cards-heart</v-icon>
          </div>
          <div
            v-else
            @click="likeBoard(boardDetail.id)"
          >
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
        </div>
        <!-- 댓글 -->
        <div @click="$router.push({ name: 'BoardComment', params: { boardId: boardDetail.id }})">
          <v-icon>mdi-comment-outline</v-icon>
        </div>
        <!-- 작성자일 경우, 수정하기 삭제하기 모달창 -->
        <div v-if="userId === boardDetail.userId">
          <v-icon @click="onClickModal">
            mdi-dots-vertical
          </v-icon>
        </div>
        <modal
          :items="selectList"
          :modal-name="'플레이리스트 변경'"
          :modal-type="'modal'"
          @on-select="onSelect"
        />
      </div>
    </div><br><br>
    <!-- {{ BoardDetail }} -->
    <div class="container">
      <!-- 작성자 정보 -->
      <div class="d-flex justify-center">
        <div class="profileImg mx-1">
          {{ boardDetail.userProfileImg }}
        </div>
        <div class="">
          {{ boardDetail.nickname }}
        </div>
        <div class="mx-1">
          팔로워 <span>{{ boardDetail.userFollowersCnt }}</span>
        </div>
      </div>
      <!-- 소개글 -->
      <p>{{ boardDetail.content }}</p>
      <!-- 공유 게시물 -->
      <div
        v-if="boardDetail.sharedContent"
        class="container"
      >
        <div
          v-if="boardDetail.sharedContent.type === 'playlist'"
        >
          <div
            class="container added"
          >
            <playlist-item-big
              :playlist="boardDetail.sharedContent"
            />
          </div>
          <v-col />
        </div>
        <div
          v-else
        >
          <div
            class="container added"
          >
            <playroom-item-big
              :playroom="boardDetail.sharedContent"
            />
          </div>
          <v-col />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import axiosConnector from '../../utils/axios-connector';

import PlayroomItemBig from '../../components/playroom/PlayroomItemBig.vue'
import PlaylistItemBig from '../../components/playlist/PlaylistItemBig.vue'
import BackOnly from '../../components/common/BackOnly.vue'
import Modal from '../../components/common/Modal.vue'
export default {
  name: 'BoardFormVideo',
  components: {
    BackOnly,
    Modal,
    PlayroomItemBig,
    PlaylistItemBig,
  },
  data: function() {
    return {
      selectList: {
        '수정하기': 'update',
        '삭제하기': 'delete',
      },
    }
  },
  computed: {
    ...mapState('board', {
      boardDetail: state => state.boardDetail,
    }),
    ...mapState({
      userId: state => state.userId,
    })
  },
  created: function() {
    this.getBoardDetail(this.$route.params.boardId)
  },
  methods: {
    ...mapActions('board', [
      'getBoardDetail',
      'likeBoard',
      'unlikeBoard',
      'saveFormData',
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    onSelect: function (item) {
      if (item === 'update') {
        const formData = {
          content: this.boardDetail.content,
          type: this.boardDetail.type
        }
        this.saveFormData(formData)
        this.$router.push({ name: 'BoardUpdateForm', params: { BoardId: this.boardDetail.id } })
      } else if (item === 'delete') {
        console.log('onSelect 삭제', item)
        axiosConnector.delete(`/Board/${this.boardDetail.id}`
        ).then((res) => {
          console.log('삭제되었습니다', res)
          this.$router.push({ name: 'Home' })
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    onClickLike: function () {
      this.boardDetail.isLiked = true
      this.boardDetail.likesCnt++
      this.likeBoard(this.boardDetail.id)
    },
    onClickUnlike: function () {
      this.boardDetail.isLiked = false
      this.boardDetail.likesCnt--
      this.unlikeBoard(this.boardDetail.id)
    },
  },
}
</script>

<style>
</style>
