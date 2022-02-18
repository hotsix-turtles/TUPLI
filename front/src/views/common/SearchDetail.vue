<template>
  <div>
    <!-- 뒤로가기/검색바 -->
    <div class="d-flex">
      <v-icon
        class="ml-2"
        @click="$router.go(-1)"
      >
        mdi-arrow-left
      </v-icon>
      <search-bar
        :label="'검색어를 입력해주세요'"
        :is-detail="true"
        :query="query"
        @input-change="search"
      />
    </div>
    <!-- 하단 리스트에 추가하기 버튼 -->
    <add-button-bottom />

    <!-- 내용물 -->
    <div class="">
      <v-tabs
        v-model="tab"
        background-color="transparent"
        grow
        color="#5B5C9D"
      >
        <!-- 탭 이거 가운데 정렬 필요 ㅠ -->
        <v-tab
          v-for="item in items"
          :key="item"
          class="d-flex justify-center"
        >
          <div @click="onChangeTab(item)">
            {{ item }}
          </div>
        </v-tab>

        <!-- 플리 검색 -->
        <v-tab-item>
          <!-- 플리 정렬 필터 -->
          <div
            class="text-right clickable mt-1 mb-2"
            mt-1
            mb-3
          >
            <span
              :key="rerenderKey"
              @click="onClickModal"
            >
              {{ convertSelect[playlistOrder] }}
            </span>
            <v-icon @click="onClickModal">
              mdi-menu-down
            </v-icon>
          </div>
          <modal
            :items="playlistSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
          <!-- 검색한 플레이리스트 -->
          <playlist-list-item-small
            :playlists="searchedPlaylists"
            :playlist-readonly="true"
            :video-readonly="true"
            :likeable="true"
          />
        </v-tab-item>

        <!-- 플레이룸 검색 -->
        <v-tab-item>
          <!-- 플레이룸 정렬 필터 -->
          <div
            class="text-right clickable mt-1 mb-2"
          >
            <span
              :key="rerenderKey + 1"
              @click="onClickModal"
            >
              {{ convertSelect[playroomOrder] }}
            </span>
            <v-icon @click="onClickModal">
              mdi-menu-down
            </v-icon>
          </div>
          <modal
            :items="playroomSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
          <playroom-list-item-small :playrooms="searchedPlayrooms" />
        </v-tab-item>

        <!-- 계정 검색 -->
        <v-tab-item>
          <!-- 계정 정렬 필터 -->
          <div
            class="text-right clickable mt-1 mb-2"
            mt-1
            mb-3
          >
            <span
              :key="rerenderKey + 2"
              @click="onClickModal"
            >
              {{ convertSelect[accountOrder] }}
            </span>
            <v-icon>mdi-menu-down</v-icon>
          </div>
          <modal
            :items="accountSelectList"
            :modal-name="'정렬 필터 변경'"
            :modal-type="'order'"
            @on-select="onSelect"
          />
          <account-list-item-small
            :accounts="searchedAccounts"
            readonly
            underline
          />
          <!-- {{ searchedAccounts }} -->
        </v-tab-item>

        <!-- 영상 검색 -->
        <v-tab-item>
          <!-- 영상 정렬 필터 -->
          <div
            class="text-right clickable mt-1 mb-2"
            mt-1
            mb-3
          >
            <span
              :key="rerenderKey + 3"
              @click="onClickModal"
            >
              {{ convertSelect[videoOrder] }}
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
            <div slot="no-more" />
          </infinite-loading><br><br>
        </v-tab-item>
      </v-tabs>
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
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import PlayroomListItemSmall from '../../components/playroom/PlayroomListItemSmall.vue'
import AccountListItemSmall from '../../components/account/AccountListItemSmall.vue'

export default {
  name: 'SearchDetail',
  components: {
    SearchBar,
    VideoListItemSmall,
    InfiniteLoading,
    AddButtonBottom,
    Modal,
    PlaylistListItemSmall,
    PlayroomListItemSmall,
    AccountListItemSmall,
  },
  data: function () {
    return {
      // 탭 관련
      tab: null, // 0: 플리, 1: 플레이룸, 2: 계정, 3: 영상
      items: [
        '플리', '플레이룸', '계정', '영상',
      ],
      tabName: '플리',
      rerenderKey: 0,
      // 전체 검색어
      query: '',

      // 플레이리스트
      playlistQuery: '',
      playlistOrder: 'relevance',
      playlistSelectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '인기순': 'likesCnt',
      },

      // 플레이룸
      playroomQuery: '',
      playroomOrder: 'relevance',
      playroomSelectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '시청자순': 'participantsCnt',
      },

      // 계정
      accountQuery: '',
      accountOrder: 'relevance',
      accountSelectList: {
        '관련순': 'relevance',
        '팔로워순': 'followersCnt',
      },

      // 영상
      videoQuery: '',
      videoOrder: 'relevance',
      videoSelectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '조회순': 'viewCount',
      },
      infiniteId: +new Date(),

      // 정렬 표시용
      convertSelect: {
        'relevance': '관련순',
        'date': '최근순',
        'viewCount': '조회순',
        'followersCnt': '팔로워순',
        'participantsCnt': '시청자순',
        'likesCnt': '인기순',
      }
    }
  },
  computed: {
    ...mapState('playlist', {
      searchedPlaylists: state => state.searchedPlaylists,
    }),
    ...mapState('playroom', {
      searchedPlayrooms: state => state.searchedPlayrooms,
    }),
    ...mapState('account', {
      searchedAccounts: state => state.searchedAccounts,
    }),
    ...mapState('video', {
      searchedVideos: state => state.searchedVideos,
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
    if (this.$route.params.tab !== undefined && this.$route.params.keyword) {
      this.query = this.$route.params.keyword
      this.tab = this.$route.params.tab
      this.tabName = this.items[this.$route.params.tab]
      this.search(this.$route.params.keyword)
    }
  },
  methods: {
    ...mapActions('playlist', [
      'searchPlaylists',
      'resetPlaylistSearchState',
    ]),
    ...mapActions('playroom', [
      'searchPlayrooms',
      'resetPlayroomSearchState',
    ]),
    ...mapActions('account', [
      'searchAccounts',
      'resetAccountSearchState',
    ]),
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
        const keyword = this.playlistQuery
        const order = this.playlistOrder
        const params = {
          keyword,
          order,
        }
        this.searchPlaylists(params)
      } else if (this.tabName === '플레이룸') {
        this.playroomQuery = query
        const keyword = this.playroomQuery
        const order = this.playroomOrder
        const params = {
          keyword,
          order,
        }
        this.searchPlayrooms(params)
      } else if (this.tabName === '계정') {
        this.accountQuery = query
        const keyword = this.accountQuery
        const order = this.accountOrder
        const params = {
          keyword,
          order,
        }
        this.searchAccounts(params)
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
      this.tabName = tabName
      if (this.tabName === '플리' && this.playlistQuery !== this.query) {
        this.playlistQuery = this.query
        const keyword = this.playlistQuery
        const order = this.playlistOrder
        const params = {
          keyword,
          order,
        }
        this.searchPlaylists(params)
      } else if (this.tabName === '플레이룸' && this.playroomQuery !== this.query) {
        this.playroomQuery = this.query
        const keyword = this.playroomQuery
        const order = this.playroomOrder
        const params = {
          keyword,
          order,
        }
        this.searchPlayrooms(params)
      } else if (this.tabName === '계정' && this.accountQuery !== this.query) {
        this.accountQuery = this.query
        const keyword = this.accountQuery
        const order = this.accountOrder
        const params = {
          keyword,
          order,
        }
        this.searchAccounts(params)
      } else if (this.tabName === '영상' && this.videoQuery !== this.query) {
        this.videoQuery = this.query
        const query = this.videoQuery
        const order = this.videoOrder
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
      if (this.tabName === '플리') {
        const temp = this.playlistOrder
        this.playlistOrder = order
        console.log('temp, this.playlistOrder', temp, this.playlistOrder)
        if (this.playlistQuery && temp !== order) {
          console.log('실행됨')
          const keyword = this.playlistQuery
          const params = {
            keyword,
            order,
          }
          this.searchPlaylists(params)
        }
      } else if (this.tabName === '플레이룸') {
        const temp = this.playroomOrder
        this.playroomOrder = order
        if (this.playroomQuery && temp !== order) {
          console.log('실행됨')
          const keyword = this.playroomQuery
          const params = {
            keyword,
            order,
          }
          this.searchPlayrooms(params)
        }
      } else if (this.tabName === '계정') {
        const temp = this.accountOrder
        this.accountOrder = order
        if (this.accountQuery && temp !== order) {
          console.log('실행됨')
          const keyword = this.accountQuery
          const params = {
            keyword,
            order,
          }
          this.searchAccounts(params)
        }
      } else if (this.tabName === '영상') {
        const temp = this.videoOrder
        this.videoOrder = order
        if (this.videoQuery && temp !== order) {
          console.log('실행됨')
          const query = this.videoQuery
          const params = {
            query,
            order,
          }
          this.resetVideoSearchState()
          this.infiniteId += 1;
          this.searchVideos(params)
        }
      }
      this.rerenderKey++
    },
  },
}
</script>

<style>

</style>
