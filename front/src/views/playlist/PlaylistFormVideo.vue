<template>
  <div>
    <back :page-name="pageName" />
    <search-bar
      :label="'새로운 영상을 검색해주세요'"
      :router-page="'VideoSearch'"
    />

    <!-- 탭 -->
    <v-tabs
      v-model="tab"
      background-color="transparent"
      grow
      color="#5B5C9D"
    >
      <v-tab
        v-for="item in items"
        :key="item"
      >
        {{ item }}
      </v-tab>
    </v-tabs>

    <!-- 탭에 따른 결과물 -->
    <video-list-item-small
      :videos="likedVideos"
    />
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
export default {
  name: 'PlaylistFormVideo',
  components: { SearchBar, VideoListItemSmall, Back },
  data: function() {
    return {
      pageName: "영상 추가하기",
      tab: null,
      items: [
        '좋아한 영상', '최근 시청한 영상',
      ],
    }
  },
  computed: {
    ...mapState('video', {
      likedVideos: state => state.likedVideos,
    })
  },
  created: function() {
    // setLikedVideos()
  },
}
</script>

<style>

</style>
