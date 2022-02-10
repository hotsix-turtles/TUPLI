<template>
  <v-expansion-panel
    id="panelHeader"
    elevation="0"
    aria-expanded="false"
  >
    <!-- 플레이리스트 정보 -->
    <v-expansion-panel-header
      :color="color"
      class="pl-0 py-3"
    >
      <v-list-item two-line>
        <v-btn
          v-if="!readonly"
          fab
          small
          text
          @click.stop="clickPlaylist"
        >
          <v-icon>
            {{ selected ? 'mdi-check' : 'mdi-plus' }}
          </v-icon>
        </v-btn>
        <playlist-cd-small
          v-if="!readonly"
          :thumbnail="playlist.videos[0].thumbnail"
          @click="watchingVideo(playlist)"
        />
        <playlist-cd-small
          v-else
          :thumbnail="playlist.videos[0].thumbnail"
          :playlist-id="playlist.id"
        />
        <v-list-item-content
          class="ml-3"
        >
          <v-list-item-title @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })">
            {{ playlist.title }}
          </v-list-item-title>
          <v-list-item-subtitle>{{ playlist.title }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-expansion-panel-header>
    <!-- 펼치기 했을 때 나오는 영상 정보 -->
    <v-expansion-panel-content
      :color="color"
      style="overflow:scroll;  height: 50vh;"
      class="px-0"
    >
      <div
        v-for="(video, idx) in playlist.videos"
        :key="idx"
      >
        <playlist-video-item-small
          :playlist-id="playlist.playlistId"
          :playlist-selected="selected"
          :video="video"
          :readonly="videoReadonly"
        />
      </div>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import PlaylistCdSmall from './PlaylistCdSmall.vue'
import PlaylistVideoItemSmall from './PlaylistVideoItemSmall.vue'

export default {
  name: 'PlaylistItemSmall',
  components: { PlaylistVideoItemSmall, PlaylistCdSmall },
  props: {
    playlist: { type: Object, default() { {} } },
    readonly: { type: Boolean, default: false },
    videoReadonly: { type: Boolean, default: false },
  },
  data() {
    return {
      selected: false,
      menuItems: [
        { id: 1, title: '메뉴 1' },
        { id: 2, title: '메뉴 2' },
        { id: 3, title: '메뉴 3' },
      ],
    }
  },
  computed: {
    color() {
      return !this.readonly && this.selected ? "#dde" : "white"
    },
    ...mapState('playlist', ['addedPlaylists', 'selectedPlaylists']),
  },
  created() {
    this.selected = Boolean(this.selectedPlaylists && this.selectedPlaylists
      .find(selectedPlaylist => selectedPlaylist.playlistId == this.playlist.playlistId))
    console.log(this.playlist)
  },
  methods: {
    clickPlaylist() {
      if (this.readonly) return;
      if (this.selected) {
        this.deselectPlaylist(this.playlist)
        this.selected = false;
      } else {
        this.selectPlaylist(this.playlist)
        this.selected = true;
      }
    },
    ...mapActions('playlist', [
      'watchingVideo',
      'selectPlaylist',
      'deselectPlaylist',
    ]),
  }
}
</script>

<style scoped>
</style>
