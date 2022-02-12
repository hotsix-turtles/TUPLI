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
        <div>
          <div
            v-if="isLiked"
            @click="unlikePlaylist(playlistDetail.id)"
          >
            <v-icon>mdi-cards-heart</v-icon>
          </div>
          <div
            v-else
            @click="likePlaylist(playlistDetail.id)"
          >
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
        </div>
        <!-- 댓글 -->
        <div @click="$router.push({ name: 'PlaylistComment', params: { playlistId: playlistDetail.id }})">
          <v-icon>mdi-comment-outline</v-icon>
        </div>
        <!-- 작성자일 경우, 수정하기 삭제하기 모달창 -->
        <div v-if="userId === playlistDetail.userId">
          <v-icon @click="onClickModal">
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
    <!-- {{ playlistDetail }} -->
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
import axiosConnector from '../../utils/axios-connector';

import BackOnly from '../../components/common/BackOnly.vue'
import DetailButtonBottom from '../../components/playlist/DetailButtonBottom.vue'

import Tags from '../../components/common/Tags.vue'
import PlaylistCd from '../../components/playlist/PlaylistCd.vue'

import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import Modal from '../../components/common/Modal.vue'
export default {
  name: 'PlaylistFormVideo',
  components: {
    BackOnly,
    DetailButtonBottom,
    VideoListItemSmall,
    PlaylistCd,
    Tags,
    Modal
  },
  data: function() {
    return {
      isSelectedAll: false,
      selectList: {
        '수정하기': 'update',
        '삭제하기': 'delete',
      }
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistDetail: state => state.playlistDetail,
      isLiked: state => state.isLiked,
    }),
    // ...mapState('common', {
    //   selected: state => state.selected,
    // }),
    ...mapState({
      userId: state => state.userId,
      taste: state => state.taste,
    })
    // 좋아요 여부 받아와서 isLiked에 저장
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
    ]),
    ...mapActions('video', [
      'deselectAllDetailVideos',
      'selectAllDetailVideos',
      'saveAddedVideos',
    ]),
    ...mapActions('common', [
      'onClickModal',
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
          console.log('삭제되었습니다')
          this.$router.push({ name: 'Home' })
        }).catch((err) => {
          console.log(err)
        })
      }
    },
  },
}
</script>

<style>
</style>
