<template>
  <v-expansion-panel
    id="panelHeader"
    elevation="0"
    aria-expanded="false"
  >
    <!-- 플레이리스트 정보
    <v-expansion-panel-header
      :color="color"
      class="pl-0 py-3"
    >
      <div
        class="d-flex justify-space-between"
        width="100%"
        @click="selectVideo"
      >
        <div class="d-flex">
          <div class="video-thumbnail">
            <img
              :src="video.thumbnail"
              style="width: 35vw; height: 100px; object-fit: cover;"
              class=""
              @click="watchingVideo(video)"
            >
            <span class="duration">{{ video.duration }}</span>
          </div>
          <div class="d-flex-column ml-2">
            <div
              class="h6"
            >
              <div class="font-3 line-height-s">
                {{ video.title }}
              </div>
            </div>
            <div class="font-4 color-dark-gray">
              <span>{{ video.channelTitle }}</span>
            </div>
          </div>
        </div>
        <div class="">
          <v-icon>mdi-dots-vertical</v-icon>
        </div>
      </div>
    </v-expansion-panel-header>
    펼치기 했을 때 나오는 영상 정보
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
    </v-expansion-panel-content> -->
  </v-expansion-panel>
</template>

<script>
// import { mapActions, mapState } from 'vuex'
// import PlaylistCdSmall from './PlaylistCdSmall.vue'
// import PlaylistVideoItemSmall from './PlaylistVideoItemSmall.vue'

export default {
  name: 'PlayroomItemSmall',
  // components: { PlaylistVideoItemSmall, PlaylistCdSmall },
  props: {
    playroom: { type: Object, default() { {} } },
  },
  data() {
    return {
    }
  },
  // computed: {
  //   color() {
  //     return !this.readonly && this.selected ? "#dde" : "white"
  //   },
  //   ...mapState('playlist', ['addedPlaylists', 'selectedPlaylists']),
  // },
  // created() {
  //   this.selected = Boolean(this.selectedPlaylists && this.selectedPlaylists
  //     .find(selectedPlaylist => selectedPlaylist.playlistId == this.playlist.playlistId))
  //   console.log(this.playlist)


  //   const el = document.querySelectorAll(".v-expansion-panel-header__icon")
  //   el.style.transform= " translate(0,10px)"

  //   console.log(el)


  // },
  // methods: {
  //   clickPlaylist() {
  //     if (this.readonly) return;
  //     if (this.selected) {
  //       this.deselectPlaylist(this.playlist)
  //       this.selected = false;
  //     } else {
  //       this.selectPlaylist(this.playlist)
  //       this.selected = true;
  //     }
  //   },
  //   ...mapActions('playlist', [
  //     'watchingVideo',
  //     'selectPlaylist',
  //     'deselectPlaylist',
  //   ]),
  // }
}
</script>

<style>
</style>
