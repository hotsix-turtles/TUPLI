<template>
  <v-card
    class="mx-1 my-1 px-1 py-1"
    @click="$emit('click', id)"
  >
    <v-avatar
      size="80"
      :class="{ selected: selected, not_selected: !selected }"
    >
      <playlist-cd-medium :thumbnail="src" :playlist-id="parseInt(id)" readonly />
    </v-avatar>
  </v-card>
</template>

<script>
import { mapMutations } from 'vuex'
import PlaylistCdMedium from '../../components/playlist/PlaylistCdMedium.vue'
export default {
  name: 'PlaylistThumbnailItem',
  components: { PlaylistCdMedium },
  props: {
    id: {type: String, default: ''},
    src: {type: String, default: ''},
    selected: {type: Boolean, default: false},
  },
  data() {
    return {

    }
  },
  methods: {
    ...mapMutations('playroom', ['SET_ROOM_CURRENT_PLAYLIST_ID'])
  }
}
</script>

<style>
.selected {
    border: 4px solid #5B5C9D;
}
.not_selected {
    border: 2px solid #bbb;
}

</style>
