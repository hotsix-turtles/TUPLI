<template>
  <v-expansion-panel
    elevation="0"
  >
    <v-expansion-panel-header
      :color="color"
      class="pl-0"
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
        <img
          :src="playlist.thumbnail"
          style="width: 100px; height: 66px"
          class=""
          @click="watchingVideo(playlist)"
        >
        <v-list-item-content
          class="ml-2"
        >
          <v-list-item-title>
            <p>{{ playlist.title }}</p>
          </v-list-item-title>
          <v-list-item-subtitle>{{ playlist.title }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-expansion-panel-header>
    <v-expansion-panel-content
      :color="color"
    >
      <playlist-video-item-small
        v-for="(video, idx) in playlist.videos"
        :key="idx"
        :playlist-id="playlist.playlistId"
        :playlist-selected="selected"
        :video="video"
        :readonly="videoReadonly"
      />
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import PlaylistVideoItemSmall from './PlaylistVideoItemSmall.vue'

export default {
  name: 'PlaylistItemSmall',
  components: { PlaylistVideoItemSmall },
  props: {
    playlist: { type: Object, default() { {} } },
    readonly: { type: Boolean, default: false },
    videoReadonly: { type: Boolean, default: false }
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
    console.log(this.readonly, this.selected)
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
    ])
  }
}
</script>

<style>

</style>
