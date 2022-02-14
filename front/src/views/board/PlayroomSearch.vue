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
        @input-change="search"
      />
    </div><br><br><br>
    <div class="container">
      <!-- 정렬 필터 -->
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
        :items="selectList"
        :modal-name="'정렬 필터 변경'"
        :modal-type="'order'"
        @on-select="onSelect"
      />
      <!-- 플레이룸 리스트 -->
      <playroom-list-item-small
        :playrooms="searchedPlayrooms"
        :is-radio-btn="true"
      />
      <!-- 하단 리스트에 추가하기 버튼 -->
      <add-button-bottom :selected="chosenPlayroom" />
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

import SearchBar from '@/components/common/SearchBar.vue'
import PlayroomListItemSmall from '../../components/playroom/PlayroomListItemSmall.vue'
import Modal from '../../components/common/Modal.vue'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';

export default {
  name: 'VideoSearch',
  components: {
    SearchBar,
    PlayroomListItemSmall,
    Modal,
    AddButtonBottom,
  },
  data: function () {
    return {
      selectList: {
        '관련순': 'relevance',
        '최근순': 'date',
        '시청자순': 'participantsCnt',
      },
      query: '',
      order: '',
    }
  },
  computed: {
    ...mapState('playroom', {
      searchedPlayrooms: state => state.searchedPlayrooms,
    }),
    ...mapState('common', {
      selected: state => state.selected,
    }),
    ...mapState('board', {
      chosenPlayroom: state => state.chosenPlayroom,
    })
  },
  created: function () {
    // this.resetVideoSearchState()
  },
  methods: {
    ...mapActions('playroom', [
      'searchPlayrooms',
      'resetPlayroomSearchState',
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    search: function (query) {
      this.query = query
      const keyword = this.query
      const order = this.selectList[this.selected]
      const params = {
        keyword,
        order,
      }
      this.searchPlayrooms(params)
    },
    onSelect: function (order) {
      const temp = this.order
      this.order = order
      if (this.query && temp !== order) {
        console.log('실행됨')
        const keyword = this.query
        const params = {
          keyword,
          order,
        }
        this.searchPlayrooms(params)
      }
    },
  },
}
</script>

<style>

</style>
