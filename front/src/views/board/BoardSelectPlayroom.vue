<template>
  <div>
    <back :page-name="'플레이룸 선택하기'" />
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'PlayroomSearch'"
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
    <add-button-bottom :selected="chosenPlayroom" />
  </div>
</template>

<script>
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';
import PlayroomListItemSmall from '../../components/playroom/PlayroomListItemSmall.vue'
import { mapState } from 'vuex';


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
      myPlayrooms: null,
      likedPlayrooms: null,
      tab: null,
      items: [
        '작성한 플레이룸', '좋아한 플레이룸',
      ],
    }
  },
  computed: {
    ...mapState('board', {
      chosenPlayroom: state => state.chosenPlayroom
    })
  },
  created: function() {
    // 좋아요 표시한 플레이룸만 뜨게하려면 playroom/likes 로 바꾸면 될 것 같다.
    axiosConnector.get(`/playroom/likes`
    ).then((res) => {
      console.log('/playroom/likes', res.data)
      this.likedPlayrooms = res.data;
    }).catch((err) => {
      console.log(err)
    })
    axiosConnector.get(`/playroom/my`
    ).then((res) => {
      console.log('/playroom/my', res.data)
      this.myPlayrooms = res.data;
    }).catch((err) => {
      console.log(err)
    })
  }
}
</script>

<style>

</style>
