<template>
  <div>
    <!-- 뒤로가기/검색바 -->
    <div class="d-flex fixed-top light-background">
      <v-icon
        class="ml-2"
        @click="$router.go(-1)"
      >
        mdi-arrow-left
      </v-icon>
      <search-bar
        :label="'검색어를 입력해주세요'"
        :is-detail="true"
        @input-change="onEnterSearch"
      />
    </div><br><br><br>
    <div class="container">
      <!-- 정렬 필터 -->
      <div
        class="text-right clickable mb-3"
        @click="onClickModal"
      >
        <span>
          {{ selected }}
        </span>
        <v-icon>mdi-menu-down</v-icon>
      </div>
      <modal
        :items="selectList"
        :modal-name="'정렬 필터 변경'"
        :modal-type="'order'"
        @on-select="onSelect"
      />
      <!-- 영상 리스트 -->
      <video-list-item-small
        :key="rerenderKey"
        :videos="searchedVideos"
        width="100vw"
      />
      <!-- 하단 리스트에 추가하기 버튼 -->
      <add-button-bottom />
      <!-- 무한스크롤 -->
      <infinite-loading
        v-if="searchedVideos.length > 0"
        :identifier="infiniteId"
        spinner="waveDots"
        @infinite="searchVideosByScroll"
      >
        <div slot="no-results" />
      </infinite-loading><br><br>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import InfiniteLoading from "vue-infinite-loading"

import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import AddButtonBottom from '../../components/playlist/AddButtonBottom.vue'
import Modal from '../../components/common/Modal.vue'

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
    Modal,
  },
  data: function () {
    return {
      selectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '조회순': 'viewCount',
      },
      query: '',
      newsType: 'story',
      infiniteId: +new Date(),
    }
  },
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
      selectedVideos: state => state.selectedVideos,
      // rerenderKey: state => state.rerenderKey,  // video는 API 두번 요청해서 변경내용 반영하기 위한 key
      nextPageToken: state => state.nextPageToken,
    }),
    ...mapState('common', {
      selected: state => state.selected,
    })
  },
  created: function () {
    // this.$router.beforeEach((to, from, next) => {
    //   console.log(from.path)
    //   console.log(from.path != '/video/watch')
    //   console.log(typeof(from.path),typeof('/video/watch'))
    //   if (from.path != '/video/watch') {
    //     this.resetVideoSearchState()
    //   }
    //   next()
    // })
    this.resetVideoSearchState()
  },
  methods: {
    ...mapActions('video', [
      'searchVideos',
      'searchVideosByScroll',
      'resetVideoSearchState',
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    onSelect: function (order) {
      console.log('VideoSearch', order)
      if (this.query) {
        const query = this.query
        const params = {
          query,
          order,
        }
        this.resetVideoSearchState()
        this.infiniteId += 1;
        this.searchVideos(params)
      }
    },
    onEnterSearch: function (query) {
      console.log(this.selected)
      const order = this.selectList[this.selected]
      const params = {
        query,
        order,
      }
      this.resetVideoSearchState()
      this.infiniteId += 1;
      this.query = query
      this.searchVideos(params)
    },
  },
}
</script>

<style>

</style>
