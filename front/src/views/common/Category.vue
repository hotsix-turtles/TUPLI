<template>
  <div class="container">
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
        <span @click="onChangeTab">{{ item }}</span>
      </v-tab>
      <!-- 플리 탭 -->
      <v-tab-item>
        <div class="row text-center mt-3">
          <div class="col-4 py-2 px-2">
            <div
              class="clickable on-clicked py-3"
              @click="onClick"
            >
              전체
            </div>
          </div>
          <div class="col-4 py-2 px-2">
            <div class="clickable gray-background py-3">
              지금 핫한
            </div>
          </div>
          <div class="col-4 py-2 px-2">
            <div class="clickable gray-background py-3">
              일상
            </div>
          </div>
          <div class="col-4 py-2 px-2">
            <div class="clickable gray-background py-3">
              영화/드라마
            </div>
          </div>
          <div class="col-4 py-2 px-2">
            <div class="clickable gray-background py-3">
              노하우/스타일
            </div>
          </div>
          <div class="col-4 py-2 px-2">
            <div class="clickable gray-background py-3">
              동물
            </div>
          </div>
        </div>
        <playlist-list-item-medium :playlists="playlists" />
        <!-- {{ playlists }} -->
      </v-tab-item>
      <!-- 플레이룸 탭 -->
      <v-tab-item>
        플레이룸
      </v-tab-item>
      <!-- 영상 탭 -->
      <v-tab-item>
        <!-- <video-list-item-small
          :key="rerenderKey"
          :videos="searchedVideos"
          width="100vw"
        /> -->
        <!-- 영상 검색용 무한스크롤 -->
        <!-- <infinite-loading
          v-if="searchedVideos.length > 0"
          spinner="waveDots"
          @infinite="searchVideosByScroll"
        >
          <div slot="no-results" />
        </infinite-loading><br><br> -->
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import PlaylistListItemMedium from '../../components/playlist/PlaylistListItemMedium.vue';
import axiosConnector from '../../utils/axios-connector';
// import InfiniteLoading from "vue-infinite-loading"

export default {
  name: 'Category',
  components: {
    PlaylistListItemMedium
    // VideoListItemSmall,
    // InfiniteLoading,
    // AddButtonBottom,
  },
  data: function () {
    return {
      tab: null, // 0: 플리, 1: 플레이룸, 2: 영상
      items: [
        '플리', '플레이룸', '영상',
      ],
      category: '',
      playlistCategory: '',
      playroomCategory: '',
      videoCategory: '',
      playlists: [],
    }
  },
  computed: {
    ...mapState('video', {
      // searchedVideos: state => state.searchedVideos,
      rerenderKey: state => state.rerenderKey,
      nextPageToken: state => state.nextPageToken,
    })
  },
  created: function () {
    // if (this.searchedVideos) {
    //   this.resetVideoSearchState()
    // }
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
      'searchVideosByScroll',
      'resetVideoSearchState',
    ]),
    onChangeCategory: function(category) {
      console.log('category', category)
      this.category = category
      if (this.tab === 0) {
        this.playlistCategory = category
        // searchPlaylists(Category)
      } else if (this.tab === 1) {
        this.playroomCategory = category
        // searchPlayrooms(Category)
      } else if (this.tab === 3) {
        this.videoCategory = category
        this.searchVideos(category)
      }
    },
    onChangeTab: function() {
      if (this.tab === 0 && this.playlistCategory !== this.category) {
        this.playlistCategory = this.category
        // searchPlaylists(Category)
      } else if (this.tab === 1 && this.playroomCategory !== this.category) {
        this.playroomCategory = this.category
        // searchPlayrooms(Category)
      } else if (this.tab === 3 && this.videoCategory !== this.category) {
        this.videoCategory = this.category
        this.searchVideos(category)
      }
    },
    // 임시용 플리(전체)
    onClick: function () {
      axiosConnector.get(`/playlist/category/all`,
      ).then((res) => {
        this.playlists = res.data
      })
        .catch((err) => {
          console.log(err)
        })
    }
  },
}
</script>

<style>

</style>
