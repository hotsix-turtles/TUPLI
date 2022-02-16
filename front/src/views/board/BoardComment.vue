<template>
  <div>
    <!-- 뒤로가기와 제목 -->
    <back :page-name="'댓글'" /><br><br>
    <!-- 댓글 노출 -->
    <div
      v-for="(boardComment, index) in boardComments.reverse()"
      :key="index"
      class="ml-2"
    >
      <v-container>
        <v-row>
          <v-col :cols="2">
            <!-- 프로필사진 노출 -->
            <img
              style="border-radius: 100px;"
              :src="ImgUrl(boardComment.user.profileImage)"
              width="40px"
              height="40px"
            >
          </v-col>
          <v-col :cols="10">
            <v-row style="table-layout:fixed">
              <div class="mt-2">
                <!-- 유저닉네임 노출 -->
                <span class="semi-bold">
                  {{ boardComment.user.nickname }}
                </span>
                <span>
                  <!-- 덧글내용 노출 -->
                  {{ boardComment.content }}
                </span>
              </div>
            </v-row>
            <v-row>
              <!-- 날짜 표시 -->
              <div>
                <span style="color:gray;font-size:14px">
                  {{ boardComment.created }}
                </span>
                <!-- 댓글삭제 버튼 -->
                <span
                  v-if="userId == boardComment.user.userSeq"
                  style="color:gray;font-size:13px"
                  class="ml-1"
                  @click.stop="deleteComment(boardComment.id)"
                >
                  삭제
                </span>
              </div>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
    </div>

    <!-- 덧글이 한개도 없을때 나오는 출력창 -->
    <div
      v-if="boardComments.length == 0"
      class="text-center text--secondary"
    >
      댓글이 없습니다.
    </div>

    <!-- 댓글 입력창 -->
    <div
      class="
      container"
    >
      <comment-input @send-comment="sendComment" />
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import CommentInput from '../../components/common/CommentInput.vue'
import { getImage } from '../../utils/utils'


export default {
  name: 'BoardComment',
  components: {
    Back,
    CommentInput
  },
  data: function() {
    return {
      boardId: 0,
    }
  },
  computed: {
    ...mapState({
      userId: state => state.userId,
    }),
    ...mapState('board', {
      boardComments: state => state.boardComments,
    }),
  },
  created: function() {
    this.boardId = this.$route.params.boardId
    this.getBoardComments(this.boardId)
  },
  methods: {
    ...mapActions('board', [
      'getBoardComments',
      'createBoardComment',
      'deleteBoardComment',
    ]),
    sendComment: function (comment) {
      const boardId = this.boardId
      const data = {
        content: comment,
        emoticon: null,
      }
      this.createBoardComment({ boardId, data })
    },
    deleteComment: function(commentId) {
      const boardId = this.boardId
      this.deleteBoardComment({commentId, boardId})
    },
    ImgUrl: function(img) {
      return getImage(img)
    }
  },
}
</script>

 <style>

 </style>
