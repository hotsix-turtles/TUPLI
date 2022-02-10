<template>
  <v-card
    class="playroom mx-auto overflow-hidden"
    height="100vh"
    max-width="640"
  >
    <!-- 하단 네비게이션 (친구 조작) -->
    <v-bottom-navigation
      absolute
      background-color="#5B5C9D"
      height="65px"
      class="fixed-bottom"
      :input-value="addedPlaylists.length > 0 || selectedPlaylists.length > 0"
    >
      <!-- 선택된 친구 명수 뱃지 -->
      <v-badge
        :content="selectedPlaylists.length"
        color="#EAEAEA"
        offset-x="20"
        offset-y="20"
        overlap
        class="videoCounter"
      />

      <!-- 추가하기 버튼 -->
      <NavButton
        color="white"
        content="추가"
        icon="mdi-plus"
        @click="requestAddPlaylists"
      />
    </v-bottom-navigation>

    <!-- 친구추가 페이지 -->
    <v-sheet
      id="scroll-threshold-example"
      class="overflow-y-auto"
      :class="{ 'pb-16': selectedPlaylists.length > 0 }"
      max-height="100%"
    >
      <back :page-name="pageName" />
      <search-bar
        :label="'추가할 친구를 검색해주세요'"
      />

      <!-- 탭 -->
      <v-tabs
        v-model="tab"
        background-color="transparent"
        grow
      >
        <v-tab
          v-for="item in items"
          :key="item"
        >
          {{ item }}
        </v-tab>
      </v-tabs>

      <!-- 탭에 따른 결과물 -->
      <v-tabs-items v-model="tab">
        <v-tab-item
          v-for="item in items"
          :key="item"
        >
          <playlist-list-item-small
            :playlists="item == '저장한 플레이리스트' ? savedPlaylists : likedPlaylists"
            :playlist-readonly="false"
            :video-readonly="true"
          />
        </v-tab-item>
      </v-tabs-items>
    </v-sheet>
  </v-card>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import NavButton from '../../components/common/NavButton.vue'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'PlayroomFormFriend',
  components: { SearchBar, NavButton, PlaylistListItemSmall, Back },
  data() {
    return {
      pageName: "친구 추가하기",
      tab: null,
      items: [
        '팔로우한 친구', '맞팔로우한 친구',
      ],
    }
  },
  computed: {
    ...mapState('playlist', ['addedPlaylists', 'selectedPlaylists', 'myPlaylists', 'likedPlaylists', 'savedPlaylists']),
  },
  created: function () {
    if (this.addedPlaylists.length) {
      this.revokePlaylists()
    }
    console.log(this.addedPlaylists, this.selectedPlaylists)
  },
  mounted () {
    this.$nextTick(() => {
      this.getUserPlaylistInfo();
    });
  },
  methods: {
    async getUserPlaylistInfo() {
      const token = localStorage.getItem('jwt')

      const likedPlaylists = await axiosConnector.get(`/playlist/likes`);
      //const savedPlaylists = await axiosConnector.get(`/playlist/saved`, { headers: { Authorization: token } });
      console.log('liked', likedPlaylists.data)
      this.setLikedPlaylist(likedPlaylists)
    },
    async requestAddPlaylists() {
      await this.addPlaylists()
      this.$router.go(-1)
    },
    ...mapActions('playlist', ['setLikedPlaylist', 'setSavedPlaylist', 'addPlaylists', 'revokePlaylists'])
  }
}
</script>

<style>

</style>
