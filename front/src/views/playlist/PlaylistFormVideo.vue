<template>
  <div>
    <cancel :page-name="pageName" />
    <search-bar
      :label="'새로운 영상을 검색해주세요'"
      :router-page="'VideoSearch'"
    />
    <!-- @click="$router.push({ name: 'VideoSearch' })" -->

    <!-- 탭 -->
    <v-tabs
      v-model="tab"
      background-color="transparent"
      grow
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
      :liked-videos="likedVideos"
    />
    <video-list-item-small
      :saved-videos="savedVideos"
    />
  </div>
</template>

<script>
import { mapState } from 'vuex'

import Cancel from '../../components/common/Cancel.vue'
import SearchBar from '../../components/common/SearchBar.vue'
export default {
  name: 'PlaylistFormVideo',
  components: { Cancel, SearchBar },
  data: function() {
    return {
      pageName: "영상 추가하기",
      tab: null,
      items: [
        '좋아한 영상', '저장한 영상',
      ],
    }
  },
  computed: {
    ...mapState('video', {
      likedVideos: state => state.likedVideos,
      savedVideos: state => state.savedVideos,
    })
  },
  created: {
    // setLikedVideos()
    // setSavedVideos()
  },
}
</script>

<style>

</style>
