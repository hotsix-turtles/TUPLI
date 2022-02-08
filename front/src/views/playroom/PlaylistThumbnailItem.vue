<template>
  <v-avatar
    size="80"
    class="ml-2 my-2"
    :class="{ selected: selected, not_selected: !selected }"
    @click="SET_ROOM_CURRENT_PLAYLIST_ID(parseInt(id))"
  >
    <v-img
      :src="src"
      contain
    />
  </v-avatar>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  name: 'PlaylistThumbnailItem',
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
