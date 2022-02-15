<template>
  <div class="d-flex my-2">
    <playlist-cd-small
      :thumbnail="playlist.videos[0].thumbnail"
      :playlist-id="playlist.id"
    />
    <v-list-item-content
      class="ml-4"
    >
      <v-list-item-title
        class="semi-bold"
        @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })"
      >
        {{ playlist.title }}
      </v-list-item-title>
      <!-- 나중에 nickname으로 변경 -->
      <v-list-item-subtitle>{{ playlist.title }}</v-list-item-subtitle>
      <div class="text-center d-flex align-center">
        <div class="mr-1">
          <v-icon
            small
            color="#bbb9b9"
          >
            mdi-heart
          </v-icon>
        </div>
        <div style="color: #bbb9b9;">
          {{ playlist.likesCnt }}
        </div>
      </div>
      <!-- <tags
        v-if="playlist.tags"
        class="mt-2"
        :tags="playlist.tags.split(',').slice(0, 3)"
      /> -->
    </v-list-item-content>
  </div>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapState } from 'vuex'
import PlaylistCdSmall from '../playlist/PlaylistCdSmall.vue'
// import Tags from '../common/Tags.vue'

export default {
  name: 'PlayroomPlaylistItemSmall',
  components: { PlaylistCdSmall },
  props: {
    playlist: { type: Object, default() {} },
  },
  data() {
    return {
    }
  },
  computed: {
  },
  methods: {
    ...mapActions('video', [
      'watchingVideo',
    ])
  },
}
</script>

<style>

</style>
