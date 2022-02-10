<template>
  <div>
    <back-only />
    <search-bar
      :label="'검색어를 입력해주세요'"
      @input-change="search"
    />
    <!-- 하단 리스트에 추가하기 버튼 -->
    <add-button-bottom />

    <!-- 내용물 -->
    <div class="container">
      <v-tabs
        v-model="tab"
        background-color="transparent"
        grow
      >
        <!-- 탭 -->
        <v-tab
          v-for="item in items"
          :key="item"
        >
          <span @click="onChangeTab(item)">{{ item }}</span>
        </v-tab>

        <!-- 플리 검색 -->
        <v-tab-item>
          <!-- 플리 정렬 필터 -->
          <div
            class="text-right clickable"
            @click="onClickModal"
          >
            <span>
              {{ selected }}
            </span>
            <v-icon>mdi-menu-down</v-icon>
          </div>
          <modal
            :items="playlistSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
        </v-tab-item>

        <!-- 플레이룸 검색 -->
        <v-tab-item>
          <!-- 플레이룸 정렬 필터 -->
          <div
            class="text-right clickable"
            @click="onClickModal"
          >
            <span>
              {{ selected }}
            </span>
            <v-icon>mdi-menu-down</v-icon>
          </div>
          <modal
            :items="playroomSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
        </v-tab-item>

        <!-- 계정 검색 -->
        <v-tab-item>
          <!-- 계정 정렬 필터 -->
          <div
            class="text-right clickable"
            @click="onClickModal"
          >
            <span>
              {{ selected }}
            </span>
            <v-icon>mdi-menu-down</v-icon>
          </div>
          <modal
            :items="accountSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
        </v-tab-item>

        <!-- 영상 검색 -->
        <v-tab-item>
          <!-- 영상 정렬 필터 -->
          <div
            class="text-right clickable"
            @click="onClickModal"
          >
            <span>
              {{ selected }}
            </span>
            <v-icon>mdi-menu-down</v-icon>
          </div>
          <modal
            :items="videoSelectList"
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
          <!-- 영상 검색용 무한스크롤 -->
          <infinite-loading
            v-if="searchedVideos.length > 0"
            :identifier="infiniteId"
            spinner="waveDots"
            @infinite="searchVideosByScroll"
          >
            <div slot="no-results" />
          </infinite-loading><br><br>
        </v-tab-item>
      </v-tabs>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import InfiniteLoading from "vue-infinite-loading"

import BackOnly from '@/components/common/BackOnly.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import AddButtonBottom from '../../components/playlist/AddButtonBottom.vue'
import Modal from '../../components/common/Modal.vue'

export default {
  name: 'SearchDetail',
  components: {
    SearchBar,
    BackOnly,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
    Modal
  },
  data: function () {
    return {
      // 탭 관련
      tab: null, // 0: 플리, 1: 플레이룸, 2: 계정, 3: 영상
      items: [
        '플리', '플레이룸', '계정', '영상',
      ],
      tabName: '플리',
      query: '',

      playlistQuery: '',
      playlistOrder: '',
      playlistSelectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '조회순': 'viewCount',
      },

      playroomQuery: '',
      accountQuery: '',

      // 영상 검색
      videoQuery: '',
      videoOrder: '',
      videoSelectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '조회순': 'viewCount',
      },
      infiniteId: +new Date(),
    }
  },
  computed: {
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
      selectedVideos: state => state.selectedVideos,
      rerenderKey: state => state.rerenderKey,
      nextPageToken: state => state.nextPageToken,
    }),
    ...mapState('common', {
      selected: state => state.selected,
    })
  },
  created: function () {
    if (this.searchedVideos) {
      this.resetVideoSearchState()
    }
    this.videoOrder = this.videoSelectList[Object.keys(this.videoSelectList)[0]]
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
    // 검색어 입력 시 해당 탭으로 검색 진행 + 해당 탭 검색어 저장
    search: function(query) {
      console.log('query', query)
      this.query = query
      if (this.tabName === '플리') {
        this.playlistQuery = query
        // searchPlaylists(query)
      } else if (this.tabName === '플레이룸') {
        this.playroomQuery = query
        // searchPlayrooms(query)
      } else if (this.tabName === '계정') {
        this.accountQuery = query
        // searchAccounts(query)
      } else if (this.tabName === '영상') {
        this.videoQuery = query
        const order = this.videoOrder
        const params = {
          query,
          order,
        }
        this.resetVideoSearchState()
        this.infiniteId += 1;
        this.searchVideos(params)
      }
    },
    // 탭 변경 + 해당 탭 해당 검색어로 입력한 적 없을시
    onChangeTab: function(tabName) {
      console.log('onChangeTab', tabName)
      console.log('this.videoQuery', this.videoQuery)
      console.log('this.query', this.query)
      this.tabName = tabName
      if (this.tabName === '플리' && this.playlistQuery !== this.query) {
        this.playlistQuery = this.query
        // searchPlaylists(query)
      } else if (this.tabName === '플레이룸' && this.playroomQuery !== this.query) {
        this.playroomQuery = this.query
        // searchPlayrooms(query)
      } else if (this.tabName === '계정' && this.accountQuery !== this.query) {
        this.accountQuery = this.query
        // searchAccounts(query)
      } else if (this.tabName === '영상' && this.videoQuery !== this.query) {
        this.videoQuery = this.query
        const query = this.videoQuery
        const order = this.videoSelectList[this.videoOrder]
        const params = {
          query,
          order,
        }
        console.log('params', params)
        this.resetVideoSearchState()
        this.infiniteId += 1;
        this.searchVideos(params)
      }
    },
    // 영상 정렬 필터 선택 + 해당 정렬 새로 선택한 거일때
    onSelect: function (order) {
      if (this.tabName === '플리' && this.playlistQuery && this.playlistOrder !== order) {
        this.playlistOrder = order
        // searchPlaylists(query)
      } else if (this.tabName === '플레이룸' && this.playroomQuery && this.playroomOrder !== order) {
        this.playroomOrder = order
        // searchPlayrooms(query)
      } else if (this.tabName === '계정' && this.accountQuery && this.accountOrder !== order) {
        this.accountOrder = order
        // searchAccounts(query)
      } else if (this.tabName === '영상' && this.videoQuery && this.videoOrder !== order) {
        this.videoOrder = order
        const query = this.videoQuery
        const params = {
          query,
          order,
        }
        this.resetVideoSearchState()
        this.infiniteId += 1;
        this.searchVideos(params)
      }
    },
  },
}
</script>

<style>

</style>
