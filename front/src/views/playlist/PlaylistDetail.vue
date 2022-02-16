<template>
  <div class="">
    <!-- 상단 뒤로가기, 플레이룸생성, 좋아요, 점3 등 -->
    <div class="fixed-top d-flex justify-space-between light-background navbar-top mx-3 py-3">
      <back-only />
      <div class="d-flex me-5">
        <!-- 플레이룸 생성 -->
        <div @click="showCreatePlayroomDialog">
          <v-icon color="black">
            mdi-youtube
          </v-icon>
        </div>
        <!-- 좋아요 -->
        <div class="mx-1">
          <div
            v-if="playlistDetail.isLiked"
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
        <div @click="$router.push({ name: 'PlaylistComment', params: { playlistId: playlistDetail.id }})">
          <v-icon color="black">
            mdi-comment-outline
          </v-icon>
        </div>
        <!-- 작성자일 경우, 수정하기 삭제하기 모달창 -->
        <div v-if="userId === playlistDetail.userId">
          <v-icon
            color="black"
            @click="onClickModal"
          >
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
    <div class="container mt-2">
      <div class="d-flex-column justify-center">
        <!-- 제목 공개여부 -->
        <div class="d-flex justify-center semi-bold">
          <div
            class=""
            style="font-size: 20px;"
          >
            {{ playlistDetail.title }}
          </div>
          <div v-if="!playlistDetail.isPublic">
            <v-icon>mdi-lock</v-icon>
          </div>
        </div>
        <!-- 작성자 정보 -->
        <div class="d-flex justify-center mt-1 mb-3">
          <img
            class="profileImg mr-2 mb-2"
            :src="ImgUrl(playlistDetail.userProfileImg)"
            alt="프로필 이미지"
          >
          <div class="d-flex align-center">
            <div class="font-2 semi-bold color-dark-gray mr-2">
              {{ playlistDetail.nickname === undefined ? playlistDetail.nickName : playlistDetail.nickname }}
            </div>
            <div class="">
              팔로워 <span>{{ playlistDetail.userFollowersCnt }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- CD -->
      <!-- playlist 썸네일 -->
      <span
        class="playlist-cd-case"
      >
        <div id="case" />
        <img
          :src="playlistDetail.videos[0].thumbnail"
          alt="썸네일"
        >
        <div />
      </span>
      <div class="container">
        <!-- 소개글 -->
        <div class="mx-4">
          {{ playlistDetail.content }}
        </div>
        <!-- 태그 -->
        <tags
          v-if="playlistDetail.tags"
          class="mx-4"
          :tags="playlistDetail.tags"
        /><br>
        <!-- 유사 플레이리스트 추천 -->
        <div
          v-if="playlistDetail.recommendPlaylists !== null && playlistDetail.recommendPlaylists !== []"
          class="mx-3"
        >
          <div class="font-2 semi-bold color-main mt-2 mb-2">
            유사 플레이리스트 추천
          </div>
          <v-card
            outlined
            style="display:flex; flex-wrap: nowrap; overflow-x: auto"
            class="playlistThumbnailWrapper"
          >
            <PlaylistThumbnailItem
              v-for="(playlist, idx) in playlistDetail.recommendPlaylists"
              :key="idx"
              :playlist-id="playlist.id"
              :src="playlist.image"
            />
          </v-card>
        </div><br>
        <!-- 전체 선택 / 영상 리스트 -->
        <div class="mx-3">
          <div class="font-2 semi-bold color-main mt-2 mb-1">
            재생 영상 목록
          </div>
          <div
            class=""
            @click="onClickSelectAll"
          >
            <v-icon>mdi-check</v-icon>
            <span class="clickable">전체 선택</span>
          </div>
          <video-list-item-small
            :videos="playlistDetail.videos"
          /><br><br>
        </div>
      </div>
    </div>

    <!-- 플레이룸생성/내 플레이리스트에 넣기/저장하기 -->
    <detail-button-bottom />
    <!-- 플레이룸 생성 -->
    <normal-dialog
      title="플레이룸 생성하기"
      content-html="이 플레이리스트를 넣어서 플레이룸을 생성하시겠습니까?"
      max-width="290"
      persistent
      :buttons="[{ name: '확인', color: '#5B5C9D' }, { name: '취소', color: 'gray' }]"
      :show="createPlayroom"
      @button-click="onClickCreatePlayroomDialog"
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
import { getImage } from '../../utils/utils'

import BackOnly from '../../components/common/BackOnly.vue'
import DetailButtonBottom from '../../components/playlist/DetailButtonBottom.vue'
import Tags from '../../components/common/Tags.vue'
import Modal from '../../components/common/Modal.vue'
import NormalDialog from '../../components/common/NormalDialog.vue';
import LoginDialog from '../../components/common/LoginDialog.vue';

import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import PlaylistThumbnailItem from '../../components/playlist/PlaylistThumbnailItem.vue'

export default {
  name: 'PlaylistFormVideo',
  components: {
    BackOnly,
    DetailButtonBottom,
    VideoListItemSmall,
    PlaylistThumbnailItem,
    // PlaylistCd,
    Tags,
    Modal,
    NormalDialog,
    LoginDialog,
  },
  data: function() {
    return {
      isSelectedAll: false,
      selectList: {
        '수정하기': 'update',
        '삭제하기': 'delete',
      },
      createPlayroom: false,
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistDetail: state => state.playlistDetail,
      recommendPlaylists: state => state.recommendPlaylists,
      isLiked: state => state.isLiked,
    }),
    ...mapState({
      userId: state => state.userId,
      isLogin: state => state.isLogin,
    })
  },
  created: function() {
    this.getPlaylistDetail(this.$route.params.playlistId)
    console.log('취향 반영됐나', this.taste)
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistDetail',
      'likePlaylist',
      'unlikePlaylist',
      'saveFormData',
      'getRecommendPlaylists'
    ]),
    ...mapActions('video', [
      'deselectAllDetailVideos',
      'selectAllDetailVideos',
      'saveAddedVideos',
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    ...mapActions('playroom', [
      'savePlaylistData',
    ]),
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        this.deselectAllDetailVideos()
      } else {
        this.selectAllDetailVideos(this.playlistDetail.videos)
      }
      this.isSelectedAll = !this.isSelectedAll
    },
    onSelect: function (item) {
      if (item === 'update') {
        const formData = {
          title: this.playlistDetail.title,
          content: this.playlistDetail.content,
          tags: this.playlistDetail.tags,
          isPublic: this.playlistDetail.isPublic,
          videos: [],
        }
        this.saveFormData(formData)
        this.saveAddedVideos(this.playlistDetail.videos)
        this.$router.push({ name: 'PlaylistUpdateForm', params: { playlistId: this.playlistDetail.id } })
      } else if (item === 'delete') {
        console.log('onSelect 삭제', item)
        axiosConnector.delete(`/playlist/${this.playlistDetail.id}`
        ).then((res) => {
          console.log('삭제되었습니다', res)
          this.$router.push({ name: 'Home' })
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    showCreatePlayroomDialog: function () {
      if (this.isLogin) {
        this.createPlayroom = true
      } else {
        this.showLoginDialog = true
      }
    },
    onClickCreatePlayroomDialog: function (idx) {
      if (idx === 0) { // 확인
        console.log('onClickCreatePlayroomDialog')
        this.createPlayroom = false
        const videoIds = this.playlistDetail.videos.map((video) => {
          return video.videoId
        })
        const playlists = {
          [this.playlistDetail.id]: videoIds
        }
        const data = {
          title: this.playlistDetail.title,
          content: this.playlistDetail.content,
          tags: this.playlistDetail.tags,
          isPublic: this.playlistDetail.isPublic,
          playlists: playlists,
        }
        this.savePlaylistData(data)
      } else {
        this.createPlayroom = false
      }
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    onClickLike: function () {
      console.log(this.isLogin)
      if (this.isLogin) {
        this.playlist.isLiked = true
        this.likePlaylist(playlistDetail.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickUnlike: function () {
      if (this.isLogin) {
        this.playlist.isLiked = false
        this.unlikePlaylist(playlistDetail.id)
      } else {
        this.showLoginDialog = true
      }
    },
  },
}
</script>

<style>
</style>
