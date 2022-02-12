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
        />
        <video-item-big :videos="categoryVideos[videoTab]" />
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
import PlayroomListItemBig from '../../components/playroom/PlayroomListItemBig.vue';
import Tabs from '../../components/common/Tabs.vue';
import axiosConnector from '../../utils/axios-connector';
// import InfiniteLoading from "vue-infinite-loading"

export default {
  name: 'Category',
  components: {
    PlaylistListItemMedium,
    PlayroomListItemBig,
    Tabs,
    // VideoListItemSmall,
    // InfiniteLoading,
  },
  data: function () {
    return {
      tab: null, // 0: 플리, 1: 플레이룸, 2: 영상
      items: [
        '플리', '플레이룸', '영상',
      ],
      categoryTypes: ['여행', '게임', '일상', '노하우/스타일', '동물', '엔터테인먼트', '영화/드라마', '음악', '교육/시사', '스포츠', '기타'],
      tastes: [],

      category: '',
      playlistCategory: '',
      playroomCategory: '',
      videoCategory: '',
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
      videoTab: state => state.videoTab,
      categoryVideos: state => state.categoryVideos,
      // rerenderKey: state => state.rerenderKey,
      // nextPageToken: state => state.nextPageToken,
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
        console.log('this.tastes', this.tastes)
      }
    })
      .catch((err) => {
        console.log(err)
      })
    this.resetVideoCategoryState()
    // for (let category of this.categoryTypes) {
    //   if (!this.tastes.includes(category)) {
    //     this.tastes.push(category)
    //   }
    //   if (this.tastes.length === 5) {
    //     break
    //   }
    // }
  },
  methods: {
    ...mapActions('playlist', [
      'getCategoryPlaylists',
    ]),
    ...mapActions('video', [
      'resetVideoCategoryState',
      // 'searchVideos',
      // 'searchVideosByScroll',
      // 'resetVideoSearchState',
    ]),
    // onChangeCategory: function(category) {
    //   console.log('category', category)
    //   this.category = category
    //   if (this.tab === 0) {
    //     this.playlistCategory = category
    //     // searchPlaylists(Category)
    //   } else if (this.tab === 1) {
    //     this.playroomCategory = category
    //     // searchPlayrooms(Category)
    //   } else if (this.tab === 3) {
    //     this.videoCategory = category
    //     this.searchVideos(category)
    //   }
    // },
    // onChangeTab: function() {
    //   if (this.tab === 0 && this.playlistCategory !== this.category) {
    //     this.playlistCategory = this.category
    //     // searchPlaylists(Category)
    //   } else if (this.tab === 1 && this.playroomCategory !== this.category) {
    //     this.playroomCategory = this.category
    //     // searchPlayrooms(Category)
    //   } else if (this.tab === 3 && this.videoCategory !== this.category) {
    //     this.videoCategory = this.category
    //     this.searchVideos(category)
    //   }
    // },
    // 임시용 플리(전체)
    // onClick: function () {
    //   axiosConnector.get(`/playlist/category/all`,
    //   ).then((res) => {
    //     this.playlists = res.data
    //   })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // }
  },
}
</script>

<style>

</style>
