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
      class=""
    >
      <v-tab
        v-for="item in items"
        :key="item"
      >
        {{ item }}
      </v-tab>

      <!-- 좋아한 영상 리스트 -->
      <v-tab-item class="mt-3">
        <video-list-item-small
          :videos="likedVideos"
          width="100vw"
        />
      </v-tab-item>

      <!-- 시청한 영상 리스트 -->
      <v-tab-item class="mt-3">
        <video-list-item-small
          :videos="watchedVideos"
          width="100vw"
        />
      </v-tab-item>
    </v-tabs>
    <add-button-bottom />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import AddButtonBottom from '../../components/playlist/AddButtonBottom.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
export default {
  name: 'PlaylistFormVideo',
  components: { SearchBar, VideoListItemSmall, Back, AddButtonBottom },
  data: function() {
    return {
      pageName: "영상 추가하기",
      tab: null,
      items: [
        '좋아한 영상', '시청한 영상',
      ],
    }
  },
  computed: {
    ...mapState('video', {
      likedVideos: state => state.likedVideos,
      watchedVideos: state => state.watchedVideos,
    })
  },
  created: function() {
    this.getLikedVideos()
    this.getWatchedVideos()
  },
  methods: {
    ...mapActions('video', [
      'getLikedVideos',
      'getWatchedVideos',
    ])
  }
}
</script>

<style>

</style>
