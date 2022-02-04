<template>
  <div>
    <back-only /><br><br>
    <p>제목: {{ playlistDetail.title }}</p>
    <img
      :src="playlistDetail.videos[0].thumbnail"
      style="width: 35vw; height: 100px;"
      class=""
    >
    <p>소개글: {{ playlistDetail.content }}</p>
    <p>태그: {{ playlistDetail.tags }}</p>
    <p>공개여부: {{ playlistDetail.isPublic }}</p>
    <div @click="onClickSelectAll">
      <v-icon>mdi-check</v-icon>
      <span class="clickable">전체 선택</span>
    </div>
    <video-list-item-small
      :videos="playlistDetail.videos"
    /><br><br>
    <!-- 플레이룸생성/내 플레이리스트에 넣기/저장하기 -->
    <detail-button-bottom />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

import BackOnly from '../../components/common/BackOnly.vue'
import DetailButtonBottom from '../../components/common/detailButtonBottom.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
export default {
  name: 'PlaylistFormVideo',
  components: { BackOnly, DetailButtonBottom, VideoListItemSmall },
  data: function() {
    return {
      isSelectedAll: false,
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistDetail: state => state.playlistDetail,
    })
  },
  created: function() {
    this.getPlaylistDetail(this.$route.params.playlistId)
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistDetail',
    ]),
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        // this.unselectAllAddedVideos()
      } else {
        // this.selectAllAddedVideos()
      }
      this.isSelectedAll = !this.isSelectedAll
    },
  },
}
</script>

<style>

</style>
