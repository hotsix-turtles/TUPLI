<template>
  <div id="comment">
    <back
      :page-name="'댓글'"
    /><br><br>
    <!-- 댓글 노출 -->
    <div
      v-for="(playlistComment, index) in playlistComments.reverse()"
      :key="index"
      class="ml-2 mr-3"
    >
      <comment-item
        :id="playlistId"
        :comment-type="'playlist'"
        :comment="playlistComment"
        @delete-comment="deleteComment"
      />
    </div>

    <!-- 덧글이 한개도 없을때 나오는 출력창 -->
    <div
      v-if="playlistComments.length == 0"
      class="text-center text--secondary mt-5"
    >
      댓글이 없습니다.
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
import { getImage, renderEmoticon } from '../../utils/utils'
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
    this.getPlaylistComments(this.playlistId)
  },
  mounted: function () {
    this.updateScroll()
  },
  // updated: function () {
  //   this.updateScroll()
  // },
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
      this.updateScroll()
    },
    deleteComment: function (commentId) {
      const playlistId = this.playlistId
      console.log("playlistComment.vue deleteComment : playlistId", playlistId)
      this.deletePlaylistComment({commentId, playlistId})
      this.getPlaylistComments(this.playlistId)
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    updateScroll: function () {
      console.log('updateScroll')
      var element = document.getElementById('comment');
      element.scrollTop = element.scrollHeight - element.clientHeight;
    },
    renderContent: function (content) {
      console.log(content)
      return renderEmoticon(content)
    },
  },
}
</script>

<style>
  #comment {
    overflow: scroll;
    height: 915px;
  }
  .comment {
    overflow: scroll;
  }
</style>
