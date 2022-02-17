<template>
  <v-avatar
    size="80"
    class="ml-2 my-2"
  >
    <playlist-cd-small
      :thumbnail="src"
      :playlist-id="playlistId"
    />
  </v-avatar>
  <!-- <v-card
    class="mx-1 my-1 px-1 py-1"
  >
    <v-avatar
      size="80"
    >
      <playlist-cd-small
        :thumbnail="src"
        :playlist-id="playlistId"
      />
    </v-avatar>
  </v-card> -->
</template>

<script>
import PlaylistCdSmall from '../../components/playlist/PlaylistCdSmall.vue'

export default {
  name: 'PlaylistThumbnailItem',
  components: { PlaylistCdSmall },
  props: {
    playlistId: {type: Number, default: 0},
    src: {type: String, default: ''},
  },
  data() {
    return {

    }
  },
  methods: {
  }
}
</script>

<style>

</style>
