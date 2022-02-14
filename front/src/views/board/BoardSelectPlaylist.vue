<template>
  <div>
    <cancel :page-name="'플레이리스트 선택하기'" />
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <v-radio-group v-model="selectedPlaylist">
      <v-radio
        v-for="(playlist, n) in likedPlaylist"
        :key="n"
        :label="`${playlist.title}`"
        :value="playlist"
      />
    </v-radio-group>
    <add-button-bottom :selected="selectedPlaylist" />
    {{ typeof(selectedPlaylist) }}
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector';
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';


export default {
  name: 'BoardFormPlaylist',
  components: {
    Cancel,
    SearchBar,
    AddButtonBottom,
  },
  data: function() {
    return {
      likedPlaylist: '',
      selectedPlaylist: null,
    }
  },
  created: function() {
    // playlist/likes 로 바꾸면 자신이 좋아요 한 게시글에 대해서만 출력될 듯..
    axiosConnector.get(`/playlist/list`
    ).then((res) => {
      this.likedPlaylist = res.data;
      // console.log("머쉬맘", res.data)
    }).catch((err) => {
      console.log(err)
    })
  }
}
</script>

<style>

</style>
