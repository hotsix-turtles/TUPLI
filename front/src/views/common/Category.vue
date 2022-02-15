<template>
  <div class="">
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
        <span>{{ item }}</span>
      </v-tab>
      <!-- 플리 탭 -->
      <v-tab-item>
        <tabs
          :tab-type="'playlist'"
          :tabs="tastes"
        />
        <div class="container">
          <playlist-list-item-medium
            :playlists="categoryPlaylists"
          />
        </div>
        <!-- {{ playlists }} -->
      </v-tab-item>
      <!-- 플레이룸 탭 -->
      <v-tab-item>
        <tabs
          :tab-type="'playroom'"
          :tabs="tastes"
        />
        <playroom-list-item-big
          :playrooms="categoryPlayrooms"
        />
      </v-tab-item>
      <!-- 영상 탭 -->
      <v-tab-item>
        <tabs
          :tab-type="'video'"
          :tabs="tastes"
          @on-click-category="rerender"
        />
        <video-list-item-big
          :key="rerenderKeyList"
          :videos="categoryVideos[videoCategory]"
        />
        <!-- 영상 검색용 무한스크롤 -->
        <infinite-loading
          v-if="tab === 2 && categoryVideos[videoCategory].length > 0"
          :key="rerenderKeyInfinite"
          spinner="waveDots"
          @infinite="getCategoryVideosByScroll"
        >
          <div slot="no-results" />
        </infinite-loading><br><br>
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import PlaylistListItemMedium from '../../components/playlist/PlaylistListItemMedium.vue';
import PlayroomListItemBig from '../../components/playroom/PlayroomListItemBig.vue';
import VideoListItemBig from '../../components/video/VideoListItemBig.vue';
import Tabs from '../../components/common/Tabs.vue';
import axiosConnector from '../../utils/axios-connector';
import InfiniteLoading from "vue-infinite-loading"

export default {
  name: 'Category',
  components: {
    PlaylistListItemMedium,
    PlayroomListItemBig,
    VideoListItemBig,
    Tabs,
    InfiniteLoading,
  },
  data: function () {
    return {
      tab: null, // 0: 플리, 1: 플레이룸, 2: 영상
      items: [
        '플리', '플레이룸', '영상',
      ],
      categoryTypes: ['일상', '노하우/스타일', '동물', '엔터테인먼트', '게임', '영화/드라마', '음악', '교육/시사', '스포츠', '기타', '여행'],
      tastes: ['영화/드라마', '일상', '노하우/스타일', '동물', '엔터테인먼트'],
      // category: '',
      // playlistCategory: '',
      // playroomCategory: '',
      // videoCategory: '',
      rerenderKeyInfinite: 999,
      rerenderKeyList: 0,
    }
  },
  computed: {
    ...mapState('playlist', {
      categoryPlaylists: state => state.categoryPlaylists,
    }),
    ...mapState('playroom', {
      categoryPlayrooms: state => state.categoryPlayrooms,
    }),
    ...mapState('video', {
      videoCategory: state => state.videoCategory,
      categoryVideos: state => state.categoryVideos,
      categoryNextPageToken: state => state.categoryNextPageToken,
      // rerenderKey: state => state.rerenderKey,
    }),
  },
  created: function () {
    // 유저 취향 기반으로 탭 셋팅
    axiosConnector.get(`/account/userInfo`,
    ).then((res) => {
      this.tastes = res.data.taste.slice()
      let i = 0
      while (this.tastes.length < 5) {
        if (!this.tastes.includes(this.categoryTypes[i])) {
          this.tastes.push(this.categoryTypes[i])
        }
        i++
      }
    })
      .catch((err) => {
        console.log(err)
      })
    this.resetVideoCategoryState()
    console.log('this.tastes', this.tastes)
    // console.log('categoryVideos[videoCategory]', this.categoryVideos[this.videoCategory].length)
  },
  methods: {
    ...mapActions('playlist', [
      'getCategoryPlaylists',
    ]),
    ...mapActions('video', [
      'resetVideoCategoryState',
      'getCategoryVideosByScroll',
    ]),
    rerender: function () {
      setTimeout(() => { // 비동기 처리 문제 때문에 0.8초 후 리렌더
        this.rerenderKeyInfinite++
        this.rerenderKeyList++
        console.log('this.rerenderKeyInfinite', this.rerenderKeyInfinite)
        console.log('this.rerenderKeyList', this.rerenderKeyList)
        console.log('this.videoCategory', this.videoCategory)
        console.log('this.categoryVideos', this.categoryVideos)
      }, 800)
    }
  },
}
</script>

<style>

</style>
