<template>
  <div>
    <back
      :page-name="'댓글'"
    /><br><br>
    <!-- 댓글 노출 -->
    <div id="playlist-comment">
      <div
        v-for="(playlistComment, idx) in playlistComments"
        :key="idx"
        class="ml-2 mr-3"
      >
        <comment-item
          :id="parseInt(playlistId)"
          :comment-type="'playlist'"
          :comment="playlistComment"
          @delete-comment="deleteComment"
        />
      </div>

      <!-- 덧글이 한개도 없을때 나오는 출력창 -->
      <div
        v-if="playlistComments.length == 0"
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
  name: 'PlaylistComment',
  components: {
    Back,
    CommentInput,
    CommentItem,
  },
  data: function() {
    return {
      playlistId: 0,
    }
  },
  computed: {
    ...mapState({
      userId: state => state.userId,
      isLogin: state => state.isLogin,
    }),
    ...mapState('playlist', {
      playlistComments: state => state.playlistComments,
    }),
  },
  created: function() {
    this.playlistId = this.$route.params.playlistId
  },
  mounted: function () {
    this.updateScroll()
  },
  updated: function () {
    this.updateScroll()
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistComments',
      'createPlaylistComment',
      'deletePlaylistComment',
    ]),
    sendComment: function (comment) {
      const playlistId = this.playlistId
      const data = {
        content: comment,
        emoticon: null,
      }
      this.createPlaylistComment({ playlistId, data })
    },
    deleteComment: function (commentId) {
      const playlistId = this.playlistId
      console.log("playlistComment.vue deleteComment : playlistId", playlistId)
      this.deletePlaylistComment({commentId, playlistId})
      this.getPlaylistComments(this.playlistId)
    },
    updateScroll: function () {
      var element = document.getElementById('playlist-comment');
      element.scrollTop = element.scrollHeight - element.clientHeight;
    },
  },
}
</script>

<style>
  #playlist-comment {
    height: 730px;
    overflow: scroll;
  }
</style>
