<template>
  <div>
    <back :page-name="'플레이리스트 선택하기'" /><br>
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'PlaylistSearch'"
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
    <add-button-bottom :chosen="chosenPlaylist" />
  </div>
</template>

<script>
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector'
import AddButtonBottom from '../../components/board/AddButtonBottom.vue';
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import { mapState, mapActions } from 'vuex';


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
      tab: null,
      items: [
        '작성한 플레이리스트', '좋아한 플레이리스트',
      ],
    }
  },
  computed: {
    ...mapState('board', {
      chosenPlaylist: state => state.chosenPlaylist,
      myPlaylists: state => state.myPlaylists,
      likedPlaylists: state => state.likedPlaylists,
    })
  },
  created: function() {
    this.getMyPlaylists()
    this.getLikedPlaylists()
  },
  methods: {
    ...mapActions('board', [
      'getMyPlaylists',
      'getLikedPlaylists',
    ]),
  }
}
</script>

<style>

</style>
