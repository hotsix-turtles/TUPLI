<template>
  <div>
    <back :page-name="'플레이리스트 선택하기'" />
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'PlaylistSearch'"
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

      <!-- 작성한 플레이리스트 리스트 -->
      <v-tab-item>
        <playlist-list-item-small
          :playlists="myPlaylists"
          :is-radio-btn="true"
          :playlist-readonly="true"
        />
      </v-tab-item>

      <!-- 좋아한 플레이리스트 리스트 -->
      <v-tab-item class="">
        <playlist-list-item-small
          :playlists="likedPlaylists"
          :is-radio-btn="true"
          :playlist-readonly="true"
        />
      </v-tab-item>
    </v-tabs>
    <add-button-bottom :selected="chosenPlaylist" />
  </div>
</template>

<script>
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import { mapState } from 'vuex';


export default {
  name: 'BoardFormPlaylist',
  components: {
    Back,
    SearchBar,
    PlaylistListItemSmall,
    AddButtonBottom,
  },
  data: function() {
    return {
      myPlaylists: null,
      likedPlaylists: null,
      tab: null,
      items: [
        '작성한 플레이리스트', '좋아한 플레이리스트',
      ],
    }
  },
  computed: {
    ...mapState('board', {
      chosenPlaylist: state => state.chosenPlaylist
    })
  },
  created: function() {
    // 좋아요 표시한 플레이리스트만 뜨게하려면 playlist/likes 로 바꾸면 될 것 같다.
    axiosConnector.get(`/playlist/likes`
    ).then((res) => {
      console.log('/playlist/likes', res.data)
      this.likedPlaylists = res.data;
    }).catch((err) => {
      console.log(err)
    })
    axiosConnector.get(`/playlist/my`
    ).then((res) => {
      console.log('/playlist/my', res.data)
      this.myPlaylists = res.data;
    }).catch((err) => {
      console.log(err)
    })
  }
}
</script>

<style>

</style>
