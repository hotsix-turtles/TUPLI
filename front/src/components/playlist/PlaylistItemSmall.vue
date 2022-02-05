<template>
  <v-expansion-panel
    elevation="0"
  >
    <v-expansion-panel-header
      :color="selected ? '#dde' : 'white'"
      class="pl-0"
    >
      <v-list-item two-line>
        <v-btn
          fab
          small
          text
          @click.stop="addPlaylist"
          >
          <v-icon>
            mdi-plus
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
      :color="selected ? '#dde' : 'white'"
    >
      <video-item-small
        v-for="(video, idx) in playlist.videos"
        :key="idx"
        :video="video"
        readonly
      />
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import VideoItemSmall from '../video/VideoItemSmall.vue'

export default {
  name: 'PlaylistItemSmall',
  components: { VideoItemSmall },
  props: {
    playlist: { type: Object, default() { {} } },
  },
  created() {
    console.log(this.playlist)
  },
  data() {
    return {
      menuItems: [
        { id: 1, title: '메뉴 1' },
        { id: 2, title: '메뉴 2' },
        { id: 3, title: '메뉴 3' },
      ],
    }
  },
  computed: {
    selected() {
      return this.selectedPlaylists.filter(selectedPlaylist => selectedPlaylist.playlistId == this.playlist.playlistId).length > 0
    },
    color() {
      return this.selected ? "#dde" : "white"
    },
    ...mapState('playlist', ['selectedPlaylists']),
  },
  methods: {
    addPlaylist() {
      console.log('addPlaylist', this.playlist)
      if (this.selected) {
        this.removeSelectedPlaylist(this.playlist)
      } else {
        this.addSelectedPlaylist(this.playlist)
      }
    },
    ...mapActions('playlist', [
      'watchingVideo',
      'addSelectedPlaylist',
      'removeSelectedPlaylist',
    ])
  }
}
</script>

<style>

</style>
