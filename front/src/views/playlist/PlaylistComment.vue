<template>
  <div class="">
    <back :page-name="'댓글'" />
    <!-- 댓글 노출 -->
    <div
      v-for="(playlistComment, index) in playlistComments"
      :key="index"
      class="ml-2"
    >
      <v-container>
        <v-row>
          <v-col :cols="2">
            <!-- 프로필사진 노출 -->
            <img
              style="border-radius: 100px;"
              :src="ImgUrl(playlistComment.user.profileImage)"
              alt="프로필 사진"
              width="35px"
              height="35px"
              class=""
            >
          </v-col>
          <v-col :cols="10">
            <v-row style="table-layout:fixed">
              <!-- 유저닉네임 노출 -->
              <span style="font-weight:bold">
                {{ playlistComment.user.nickname }}
              </span>
              &nbsp;
              <span>
                <!-- 덧글내용 노출 -->
                {{ playlistComment.content }}
              </span>
            </v-row>
            <v-row>
              <!-- 날짜 표시 -->
              <div class="mb-2">
                <span style="color:gray">
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
      class="text-center text--secondary"
    >
      덧글이 없습니다.
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
import CommentInput from '../../components/playlist/CommentInput.vue'
import { getImage } from '../../utils/utils'

export default {
  name: 'PlaylistComment',
  components: {
    Back,
    CommentInput
  },
  data: function() {
    return {
      playlistId: 0,
      convertedTime: [],
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
  created: function() {
    this.playlistId = this.$route.params.playlistId
    this.getPlaylistComments(this.playlistId)
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
    },
    ImgUrl: function(img) {
      return getImage(img)
    }
  },
}
</script>

<style>

</style>
