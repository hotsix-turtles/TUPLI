<template>
  <div class="">
    <!-- 상단 뒤로가기, 좋아요, 댓글, 점3 등 -->
    <div class="fixed-top d-flex justify-space-between light-background navbar-top mx-3 py-3">
      <back-only />
      <div class="d-flex me-5">
        <!-- 좋아요 -->
        <div class="mx-2">
          <div
            v-if="boardDetail.isLiked"
            class="animate__animated animate__heartBeat"
            @click="onClickUnlike"
          >
            <v-icon color="#5B5C9D">
              mdi-cards-heart
            </v-icon>
          </div>
          <div
            v-else
            @click="onClickLike"
          >
            <v-icon color="black">
              mdi-cards-heart-outline
            </v-icon>
          </div>
        </div>
        <!-- 댓글 -->
        <div @click="onClickComment">
          <v-icon color="black">
            mdi-comment-outline
          </v-icon>
        </div>
        <!-- 작성자일 경우, 수정하기 삭제하기 모달창 -->
        <div v-if="userId === boardDetail.user.userSeq">
          <v-icon
            color="black"
            class="mx-1"
            @click="onClickModal"
          >
            mdi-dots-vertical
          </v-icon>
        </div>
      </div>
    </div><br><br>
    <div class="container pt-0">
      <v-container>
        <v-row>
          <v-col :cols="2">
            <!-- 프로필사진 -->
            <img
              style="border-radius: 100px;"
              :src="ImgUrl( boardDetail.user.profileImage)"
              width="40px"
              height="40px"
            >
          </v-col>
          <v-col :cols="10">
            <v-row>
              <!-- 작성자 정보 -->
              <div class="semi-bold mt-2">
                {{ boardDetail.user.nickname }}
              </div>
              <!-- 팔로우 수 -->
              <div class="ml-1 mt-2 font-3 color-dark-gray">
                팔로워 <span>{{ boardDetail.user.followerCnt }}</span>
              </div>
            </v-row>
            <v-row>
              <div
                style="color:gray;font-size:14px"
              >
                {{ boardDetail.created }}
              </div>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
      <!-- 소개글 -->
      <p class="mx-3 mt-3">
        {{ boardDetail.content }}
      </p>
      <br>
      <!-- 공유 게시물 -->
      <div
        class=""
      >
        <div class="mb-5">
          <div
            v-if="boardDetail.playlist !== null"
          >
            <div class="container added">
              <playlist-item-big
                :playlist="boardDetail.playlist"
              />
            </div>
          </div>
          <div
            v-if="boardDetail.playroom !== null"
            class="container added"
          >
            <playroom-item-big
              :playroom="boardDetail.playroom"
            />
          </div>
        </div>
        <!-- 전체 선택 / 영상 리스트 -->
        <div
          v-if="boardDetail.playlist != null || boardDetail.playroom != null"
          class="clickable my-3"
          @click="onClickSelectAll"
        >
          <div class="font-2 semi-bold color-main mt-5 mb-1">
            재생 영상 목록
          </div>
          <div class="mt-3 mb-2">
            <v-icon>mdi-check</v-icon>
            <span>전체 선택</span>
          </div>
          <video-list-item-small
            v-if="boardDetail.playlist !== null"
            :videos="boardDetail.playlist.videos"
          />
          <video-list-item-small
            v-if="boardDetail.playroom !== null"
            :videos="boardDetail.playroom.videos"
          />
        </div>
        <detail-button-bottom />
      </div>
    </div>
    <!-- 수정하기/삭제하기 -->
    <modal
      :items="selectList"
      :modal-name="'게시글 변경'"
      :modal-type="'modal'"
      @on-select="onSelect"
    />
    <!-- 삭제 여부 확인 다이얼로그 -->
    <normal-dialog
      title="게시글 삭제하기"
      content-html="정말 이 게시글를 삭제하시겠습니까?"
      max-width="290"
      persistent
      :buttons="[{ name: '취소', color: 'gray' }, { name: '확인', color: '#5B5C9D' }]"
      :show="deleteBoard"
      @button-click="onClickDeleteBoardDialog"
    />
    <timeout-dialog-k
      :content="'해당 게시글이 삭제되었습니다.'"
      :show="showTimeoutDialog"
      hide-progress
      :persistent="false"
      timeout="1700"
      @timeout="onTimeout"
      @click="onTimeout"
    />
    <login-dialog
      :show="showLoginDialog"
      @on-click="showLoginDialog = false"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import axiosConnector from '../../utils/axios-connector';
import { timeConverter, getImage } from '../../utils/utils'

import PlayroomItemBig from '../../components/playroom/PlayroomItemBig.vue'
import PlaylistItemBig from '../../components/playlist/PlaylistItemBig.vue'
import BackOnly from '../../components/common/BackOnly.vue'
import Modal from '../../components/common/Modal.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue';

import NormalDialog from '../../components/common/NormalDialog.vue';
import LoginDialog from '../../components/common/LoginDialog.vue';
import TimeoutDialogK from '../../components/common/TimeoutDialogK.vue'
import DetailButtonBottom from '../../components/playlist/DetailButtonBottom.vue'

export default {
  name: 'BoardDetail',
  components: {
    BackOnly,
    Modal,
    PlayroomItemBig,
    PlaylistItemBig,
    DetailButtonBottom,
    VideoListItemSmall,
    NormalDialog,
    LoginDialog,
    TimeoutDialogK,
  },
  data: function() {
    return {
      isSelectedAll: false,
      selectList: {
        '수정하기': 'update',
        '삭제하기': 'delete',
      },
      deleteBoard: false,
      showTimeoutDialog: false,
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState('board', {
      boardDetail: state => state.boardDetail,
    }),
    ...mapState({
      userId: state => state.userId,
      isLogin: state => state.isLogin,
    })
  },
  created: function() {
    this.getBoardDetail(this.$route.params.boardId)
    console.log(this.getBoardDetail)
  },
  methods: {
    ...mapActions('board', [
      'getBoardDetail',
      'likeBoard',
      'unlikeBoard',
      'saveFormData',
      'choosePlaylist',
      'choosePlayroom',
      'getBoardComments',
    ]),
    ...mapActions('video', [
      'deselectAllDetailVideos',
      'selectAllDetailVideos',
      'saveAddedVideos',
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    onSelect: function (item) {
      if (item === 'update') {
        const formData = {
          content: this.boardDetail.content,
          isBoard: false,
          radioVal: 'playroom',
        }
        if (this.boardDetail.playlist !== null ) {
          formData.isBoard = true
          formData.radioVal = 'playlist'
          this.choosePlaylist(this.boardDetail.playlist)
        } else if (this.boardDetail.playroom !== null) {
          formData.isBoard = true
          this.choosePlayroom(this.boardDetail.playroom)
        }
        this.saveFormData(formData)
        this.$router.push({ name: 'BoardUpdateForm', params: { boardId: this.boardDetail.id } })
      } else if (item === 'delete') {
        this.deleteBoard = true
      }
    },
    onClickLike: function () {
      if (this.isLogin) {
        this.boardDetail.isLiked = true
        this.likeBoard(this.boardDetail.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickUnlike: function () {
      if (this.isLogin) {
        this.boardDetail.isLiked = false
        this.unlikeBoard(this.boardDetail.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        this.deselectAllDetailVideos()
      } else {
        if (this.boardDetail.playlist !== null) {
          this.selectAllDetailVideos(this.boardDetail.playlist.videos)
        } else {
          this.selectAllDetailVideos(this.boardDetail.playroom.videos)
        }
      }
      this.isSelectedAll = !this.isSelectedAll
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    onClickDeleteBoardDialog: function (idx) {
      if (idx === 1) { // 확인
        this.deleteBoard = false
        axiosConnector.delete(`/board/${this.boardDetail.id}`
        ).then((res) => {
          console.log('게시글이 삭제되었습니다', res)
          this.showTimeoutDialog = true
        }).catch((err) => {
          console.log(err)
        })
      } else {
        this.deleteBoard = false
      }
    },
    onTimeout () {
      this.showTimeoutDialog = false
      this.$router.push({ name: 'Home' })
    },
    onClickComment: function () {
      this.getBoardComments(this.boardDetail.id)
      this.$router.push({ name: 'BoardComment', params: { boardId: this.boardDetail.id }})
    }
  },
}
</script>

<style>
  .added {
    border: solid 1px;
    border-color: #d8d8ee;
  }
</style>
