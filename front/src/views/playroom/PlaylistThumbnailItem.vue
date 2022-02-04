<template>
  <v-avatar
    size="80"
    class="ml-2 my-2"
    :class="{ selected: selected, not_selected: !selected }"
    @click="setRoomCurrentPlaylistOffset(id)"
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
<<<<<<< HEAD
    // eslint-disable-next-line vue/require-default-prop
    src: String,
    selected: Boolean
=======
    id: {type: Number, default: 0},
    src: {type: String, default: ''},
    selected: {type: Boolean, default: false},
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
  },
  data() {
    return {

    }
  },
  methods: {
    ...mapMutations('playroom', ['setRoomCurrentPlaylistOffset'])
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
