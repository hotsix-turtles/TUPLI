<template>
  <div>
    <back-only />
    <search-bar
      :label="'검색어를 입력해주세요'"
      @input-change="search"
    />
    <!-- 하단 리스트에 추가하기 버튼 -->
    <add-button-bottom />

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
        <span @click="onChangeTab">{{ item }}</span>
      </v-tab>
      <!-- 플리 검색 -->
      <v-tab-item>
        플리
      </v-tab-item>
      <!-- 플레이룸 검색 -->
      <v-tab-item>
        플레이룸
      </v-tab-item>
      <!-- 계정 검색 -->
      <v-tab-item>
        계정
      </v-tab-item>
      <!-- 영상 검색 -->
      <v-tab-item>
        <video-list-item-small
          :key="rerenderKey"
          :videos="searchedVideos"
          width="100vw"
        />
        <!-- 영상 검색용 무한스크롤 -->
        <infinite-loading
          v-if="searchedVideos.length > 0"
          spinner="waveDots"
          @infinite="searchVideosByScroll"
        >
          <div slot="no-results" />
        </infinite-loading><br><br>
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import InfiniteLoading from "vue-infinite-loading"

import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import AddButtonBottom from '../../components/playlist/AddButtonBottom.vue'

export default {
  name: 'SearchDetail',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
  },
  data: function () {
    return {
      tab: null, // 0: 플리, 1: 플레이룸, 2: 계정, 3: 영상
      items: [
        '플리', '플레이룸', '계정', '영상',
      ],
      query: '',
      playlistQuery: '',
      playroomQuery: '',
      accountQuery: '',
      videoQuery: '',
    }
  },
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
      selectedVideos: state => state.selectedVideos,
      rerenderKey: state => state.rerenderKey,
      nextPageToken: state => state.nextPageToken,
    })
  },
  created: function () {
    if (this.searchedVideos) {
      this.resetVideoSearchState()
    }
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
      'searchVideosByScroll',
      'resetVideoSearchState',
    ]),
    search: function(query) {
      console.log('query', query)
      this.query = query
      if (this.tab === 0) {
        this.playlistQuery = query
        // searchPlaylists(query)
      } else if (this.tab === 1) {
        this.playroomQuery = query
        // searchPlayrooms(query)
      } else if (this.tab === 2) {
        this.accountQuery = query
        // searchAccounts(query)
      } else if (this.tab === 3) {
        this.videoQuery = query
        this.searchVideos(query)
      }
    },
    onChangeTab: function() {
      if (this.tab === 0 && this.playlistQuery !== this.query) {
        this.playlistQuery = this.query
        // searchPlaylists(query)
      } else if (this.tab === 1 && this.playroomQuery !== this.query) {
        this.playroomQuery = this.query
        // searchPlayrooms(query)
      } else if (this.tab === 2 && this.accountQuery !== this.query) {
        this.accountQuery = this.query
        // searchAccounts(query)
      } else if (this.tab === 3 && this.videoQuery !== this.query) {
        this.videoQuery = this.query
        this.searchVideos(query)
      }
    }
  },
}
</script>

<style>

</style>
