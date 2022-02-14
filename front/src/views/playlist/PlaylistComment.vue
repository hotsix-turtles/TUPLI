<template>
  <div class="">
    <back :page-name="'댓글'" />
    <!-- 댓글 노출 -->
    <div
      v-for="(playlistComment, index) in playlistComments"
      :key="index"
    >
      <v-img lazy-src="`{playlistComment.user.profileImage}`" />
      <!-- playlistComment.created 하면 생성시간이어야 하는데, 숫자가 이상하다. 오류가 있는 듯 하다. -->
      {{ playlistComment.user.nickname }} {{ playlistComment.content }}
      <!-- 댓글삭제 버튼 -->
      <span class="text-right">
        <v-btn
          v-if="userId == playlistComment.user.userSeq"
          x-small
          @click="deleteComment(playlistComment.id)"
        >
          X
        </v-btn>
      </span>
    </div>
    <!-- 댓글 입력창 -->
    <div class="container">
      <comment-input @send-comment="sendComment" />
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import CommentInput from '../../components/playlist/CommentInput.vue'

export default {
  name: 'PlaylistComment',
  components: {
    Back,
    CommentInput
  },
  data: function() {
    return {
      playlistId: 0,
      forShowComments: null,
    }
  },
  computed: {
    ...mapState({
      userId: state => state.userId,
      authToken: state => state.authToken
    }),
    ...mapState('playlist', {
      playlistComments: state => state.playlistComments,
    }),
  },
  watch: {
    playlistComments: function(value) {
      this.playlistComments = value;
    }
  },
  created: function() {
    this.playlistId = this.$route.params.playlistId
    this.getPlaylistComments(this.playlistId)
    this.forShowComments = this.playlistComments
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
    deleteComment: function(commentId) {
      const playlistId = this.playlistId
      console.log("playlistComment.vue : playlistId", playlistId)
      this.deletePlaylistComment({commentId, playlistId})
    }
  },
}
</script>

<style>

</style>
