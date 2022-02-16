<template>
  <v-avatar
    size="80"
    class="ml-2 my-2"
  >
    <playlist-cd-medium
      :thumbnail="src"
      :playlist-id="playlistId"
    />
  </v-avatar>
</template>

<script>
import { mapMutations } from 'vuex'
import PlaylistCdMedium from '../../components/playlist/PlaylistCdMedium.vue'

export default {
  name: 'PlaylistThumbnailItem',
  components: { PlaylistCdMedium },
  props: {
    playlistId: {type: String, default: ''},
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
