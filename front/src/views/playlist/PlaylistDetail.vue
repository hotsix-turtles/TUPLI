<template>
  <div class="">
    <!-- 상단 뒤로가기, 플레이룸생성, 좋아요, 점3 등 -->
    <div class="fixed-top d-flex justify-space-between light-background navbar-top">
      <back-only />
      <div class="d-flex me-5">
        <!-- 플레이룸 생성 -->
        <div>
          <v-icon>mdi-youtube</v-icon>
        </div>
        <!-- 좋아요 -->
        <div @click="likePlaylist(playlistDetail.id)">
          <div v-if="isLiked">
            <v-icon>mdi-cards-heart</v-icon>
          </div>
          <div v-else>
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
        </div>
        <!-- 댓글 -->
        <div @click="$router.push({ name: 'PlaylistComment', params: { playlistId: playlistDetail.id }})">
          <v-icon>mdi-comment-outline</v-icon>
        </div>
        <!-- 작성자일 경우, 수정하기 삭제하기 모달창 -->
        <div v-if="userId === playlistDetail.userId">
          <v-icon>mdi-dots-vertical</v-icon>
        </div>
      </div>
    </div><br><br>
    <div class="container">
      <div class="text-center">
        <!-- 제목 공개여부 -->
        <div class="d-flex justify-center">
          <div class="font-1">
            {{ playlistDetail.title }}
          </div>
          <div v-if="!playlistDetail.isPublic">
            <v-icon>mdi-lock</v-icon>
          </div>
        </div>
        <!-- 작성자 정보 -->
        <div class="d-flex justify-center">
          <div class="profileImg mx-1">
            {{ playlistDetail.userProfileImg }}
          </div>
          <div class="">
            {{ playlistDetail.userName }}
          </div>
          <div class="mx-1">
            팔로워 <span>{{ playlistDetail.userFollowersCnt }}</span>
          </div>
        </div>
      </div>
      <!-- CD -->
      <playlist-cd :thumbnail="playlistDetail.videos[0].thumbnail" />
      <!-- 소개글 -->
      <p>{{ playlistDetail.content }}</p>
      <!-- 태그 -->
      <tags :tags="playlistDetail.tags" />
      <!-- 전체 선택 / 영상 리스트 -->
      <div @click="onClickSelectAll">
        <v-icon>mdi-check</v-icon>
        <span class="clickable">전체 선택</span>
      </div>
      <video-list-item-small
        :videos="playlistDetail.videos"
      /><br><br>
    </div>
    <!-- 플레이룸생성/내 플레이리스트에 넣기/저장하기 -->
    <detail-button-bottom />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

import BackOnly from '../../components/common/BackOnly.vue'
import DetailButtonBottom from '../../components/playlist/DetailButtonBottom.vue'

import Tags from '../../components/common/Tags.vue'
import PlaylistCd from '../../components/playlist/PlaylistCd.vue'

import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
export default {
  name: 'PlaylistFormVideo',
  components: {
    BackOnly,
    DetailButtonBottom,
    VideoListItemSmall,
    PlaylistCd,
    Tags,
  },
  data: function() {
    return {
      isSelectedAll: false,
      isLiked: false,
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistDetail: state => state.playlistDetail,
      isPlaylistLiked: state => state.isPlaylistLiked,
    }),
    ...mapState('account', {
      userId: state => state.userId,
    })
    // 좋아요 여부 받아와서 isLiked에 저장
  },
  created: function() {
    this.getPlaylistDetail(this.$route.params.playlistId)
    this.isLiked =  this.isPlaylistLiked
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistDetail',
      'likePlaylist',
    ]),
    ...mapActions('video', [
      'deselectAllDetailVideos',
      'selectAllDetailVideos',
    ]),
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        this.deselectAllDetailVideos()
      } else {
        this.selectAllDetailVideos(this.playlistDetail.videos)
      }
      this.isSelectedAll = !this.isSelectedAll
    },
  },
}
</script>

<style>
</style>
