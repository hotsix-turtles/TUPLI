<template>
  <div class="">
    <back :page-name="'댓글'" />
    {{ playlistComments }}
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
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistComments: state => state.playlistComments,
    }),
    ...mapState('account', {
      userId: state => state.userId,
    })
  },
  created: function() {
    this.playlistId = this.$route.params.playlistId
    console.log('this.$route.params.playlistId', this.$route.params.playlistId)
    this.getPlaylistComments(playlistId)
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistComments',
      'createPlaylistComment',
      'deletePlaylistComment',
    ]),
    sendComment: function (comment) {
      console.log('comment', comment)
      const playlistId = this.playlistId
      const data = {
        content: comment,
      }
      this.createPlaylistComment({ playlistId, data })
    }
  },
}
</script>

<style>

</style>
