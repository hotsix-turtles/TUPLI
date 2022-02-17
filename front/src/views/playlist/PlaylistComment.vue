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
      <v-container>
        <v-row>
          <v-col :cols="2">
            <!-- 프로필사진 노출 -->
            <img
              style="border-radius: 100px;"
              :src="ImgUrl(playlistComment.user.profileImage)"
              width="40px"
              height="40px"
            >
          </v-col>
          <v-col :cols="10">
            <v-row
              style="table-layout:fixed"
            >
              <div class="mt-2">
                <!-- 유저닉네임 노출 -->
                <span class="semi-bold">
                  {{ playlistComment.user.nickname }}
                </span>
                <!-- <span
                  class=""
                  v-html="renderContent(playlistComment.content)"
                > -->
                <span>
                  <!-- 덧글내용 노출 -->
                  {{ playlistComment.content }}
                </span>
              </div>
            </v-row>
            <v-row>
              <!-- 날짜 표시 -->
              <div class="">
                <span style="color:gray;font-size:14px">
                  {{ playlistComment.created }}
                </span>
                <!-- 댓글삭제 버튼 -->
                <span
                  v-if="userId == playlistComment.user.userSeq"
                  style="color:gray;font-size:13px"
                  class="ml-1"
                  @click.stop="deleteComment(playlistComment.id)"
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


export default {
  name: 'PlaylistComment',
  components: {
    Back,
    CommentInput,
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
  // mounted: function () {
  //   this.updateScroll()
  // },
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
    },
    deleteComment: function () {
      const playlistId = this.playlistId
      console.log("playlistComment.vue : playlistId", playlistId)
      this.deletePlaylistComment({commentId, playlistId})
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    updateScroll: function () {
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
