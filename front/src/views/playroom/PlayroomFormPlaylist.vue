<template>
  <v-card
    class="playroom mx-auto overflow-hidden"
    height="100%"
  >
    <!-- 하단 네비게이션 (플레이리스트 조작) -->
    <v-bottom-navigation
      absolute
      background-color="#5B5C9D"
      height="66px"
      class="fixed-bottom"
      :input-value="addedPlaylists.length > 0 || selectedPlaylists.length > 0"
    >
      <!-- 선택된 동영상 개수 뱃지 -->
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

    <!-- 플레이룸 페이지 -->
    <v-sheet
      id="scroll-threshold-example"
      class="overflow-y-auto"
    >
      <!-- 뒤로가기/완료 or 수정 -->
      <div class="d-flex justify-space-between fixed-top light-background">
        <div class="d-flex mx-3 my-3">
          <div>
            <v-icon @click="saveAndGoBack">
              mdi-arrow-left
            </v-icon>
          </div>
          <div class="font-2 semi-bold center">
            {{ pageName }}
          </div>
        </div>
      </div>

      <!-- 탭 -->
      <v-tabs
        v-model="tab"
        background-color="transparent"
        grow
        class="mt-14"
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
          v-for="(item, idx) in items"
          :key="idx"
        >
          <playlist-list-item-small
            :playlists="idx ? likedPlaylists : myPlaylists"
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
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import NavButton from '../../components/common/NavButton.vue'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'PlayroomFormPlaylist',
  components: { NavButton, PlaylistListItemSmall },
  data() {
    return {
      pageName: "플레이리스트 추가하기",
      tab: null,
      items: [
        '내 플레이리스트', '좋아요한 플레이리스트',
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
    saveAndGoBack() {
      this.requestAddPlaylists()
    },
    async getUserPlaylistInfo() {
      const token = localStorage.getItem('jwt')

      const myPlaylists = await axiosConnector.get(`/playlist/my`);
      const likedPlaylists = await axiosConnector.get(`/playlist/likes`);
      //const savedPlaylists = await axiosConnector.get(`/playlist/saved`, { headers: { Authorization: token } });
      this.setMyPlaylist(myPlaylists)
      this.setLikedPlaylist(likedPlaylists)
    },
    async requestAddPlaylists() {
      await this.addPlaylists()
      this.$router.go(-1)
    },
    ...mapActions('playlist', ['setMyPlaylist','setLikedPlaylist', 'setSavedPlaylist', 'addPlaylists', 'revokePlaylists'])
  }
}
</script>

<style>

</style>
