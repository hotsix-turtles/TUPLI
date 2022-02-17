<template>
  <div>
    <back :page-name="'플레이룸 선택하기'" /><br>
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'PlayroomSearch'"
      class="pt-3"
    />

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
        {{ item }}
      </v-tab>

      <!-- 작성한 플레이룸 리스트 -->
      <v-tab-item>
        <playroom-list-item-small
          :playrooms="myPlayrooms"
          :is-radio-btn="true"
        />
      </v-tab-item>
      <!-- 좋아한 플레이룸 리스트 -->
      <v-tab-item class="">
        <playroom-list-item-small
          :playrooms="likedPlayrooms"
          :is-radio-btn="true"
        />
      </v-tab-item>
    </v-tabs>
    <add-button-bottom :chosen="chosenPlayroom" />
  </div>
</template>

<script>
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';
import PlayroomListItemSmall from '../../components/playroom/PlayroomListItemSmall.vue'
import { mapState, mapActions } from 'vuex';


export default {
  name: 'BoardFormPlayroom',
  components: {
    Back,
    SearchBar,
    PlayroomListItemSmall,
    AddButtonBottom,
  },
  data: function() {
    return {
      tab: null,
      items: [
        '작성한 플레이룸', '좋아한 플레이룸',
      ],
    }
  },
  computed: {
    ...mapState('board', {
      chosenPlayroom: state => state.chosenPlayroom,
      myPlayrooms: state => state.myPlayrooms,
      likedPlayrooms: state => state.likedPlayrooms,
    })
  },
  created: function() {
    this.getMyPlayrooms()
    this.getLikedPlayrooms()
  },
  methods: {
    ...mapActions('board', [
      'getMyPlayrooms',
      'getLikedPlayrooms',
    ]),
  }
}
</script>

<style>

</style>
