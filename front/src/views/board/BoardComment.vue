<template>
  <div>
    <!-- 뒤로가기와 제목 -->
    <back :page-name="'댓글'" /><br><br>
    <!-- 댓글 노출 -->
    <div id="board-comment">
      <div
        v-for="(boardComment, idx) in boardComments"
        :key="idx"
        class="ml-2"
      >
        <comment-item
          :id="parseInt(boardId)"
          :comment-type="'board'"
          :comment="boardComment"
          @delete-comment="deleteComment"
        />
      </div>

      <!-- 덧글이 한개도 없을때 나오는 출력창 -->
      <div
        v-if="boardComments.length == 0"
        class="text-center text--secondary"
      >
        댓글이 없습니다.
      </div>
    </div>

    <!-- 댓글 입력창 -->
    <div
      class="container"
    >
      <comment-input @send-comment="sendComment" />
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import CommentInput from '../../components/common/CommentInput.vue'
import CommentItem from '../../components/common/CommentItem.vue'


export default {
  name: 'BoardComment',
  components: {
    Back,
    CommentInput,
    CommentItem,
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
  },
  mounted: function () {
    this.updateScroll()
  },
  updated: function () {
    this.updateScroll()
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
      this.getBoardComments(this.boardId)
    },
    updateScroll: function () {
      var element = document.getElementById('board-comment');
      element.scrollTop = element.scrollHeight - element.clientHeight;
    },
  },
}
</script>

 <style>
  #board-comment {
    height: 730px;
    overflow: scroll;
  }
 </style>
