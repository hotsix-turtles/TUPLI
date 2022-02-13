<template>
  <div>
    <cancel :page-name="'플레이룸 선택하기'" />
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <v-radio-group v-model="selectedPlayroom">
      <v-radio
        v-for="(playroom, n) in likedPlayroom"
        :key="n"
        :label="`${playroom.title}`"
        :value="playroom"
      />
    </v-radio-group>
    <add-button-bottom :selected="selectedPlayroom" />
    {{ selectedPlayroom }}
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';


export default {
  name: 'BoardFormPlayroom',
  components: {
    Cancel,
    SearchBar,
    AddButtonBottom,

  },
  data: function() {
    return {
      likedPlayroom: null,
      selectedPlayroom: null
    }
  },
  created: function() {
    // 좋아요 표시한 플레이룸만 뜨게하려면 playroom/likes 로 바꾸면 될 것 같다.
    axiosConnector.get(`/playroom/list`
    ).then((res) => {
      this.likedPlayroom = res.data;
    }).catch((err) => {
      console.log(err)
    })
  }
}
</script>

<style>

</style>
