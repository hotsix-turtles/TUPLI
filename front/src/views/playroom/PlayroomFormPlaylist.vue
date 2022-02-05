<template>
  <div>
    <back :page-name="pageName" />
    <search-bar
      :label="'새로운 플레이리스트를 검색해주세요'"
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
          :playlists="item == '저장한 플레이리스트' ? savedPlaylists : myPlaylists"
        />
      </v-tab-item>
    </v-tabs-items>

    <v-card-text style="height: 100px; position: relative">
      <v-fab-transition>
        <v-btn
          v-show="!hidden"
          color="pink"
          dark
          absolute
          top
          right
          fab
          @click="addPlaylists && $router.go(-1)"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-fab-transition>
    </v-card-text>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import SearchBar from '../../components/common/SearchBar.vue'
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
export default {
  name: 'PlayroomFormPlaylist',
  components: { SearchBar, PlaylistListItemSmall, Back },
  data() {
    return {
      pageName: "플레이리스트 추가하기",
      tab: null,
      items: [
        '내 플레이리스트', '저장한 플레이리스트',
      ],
    }
  },
  created: function () {
    if (this.addedPlaylists) {
      this.revokePlaylists()
    }
  },
  computed: {
    ...mapState('playlist', ['addedPlaylists', 'myPlaylists', 'savedPlaylists']),
    ...mapState('video', ['selectedVideos'])
  },
  methods: {
    ...mapActions('playlist', ['addPlaylists', 'revokePlaylists']),
    ...mapActions('video', ['addVideos'])
  }
}
</script>

<style>

</style>
