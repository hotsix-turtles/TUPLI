/* eslint-disable vue/require-default-prop */
<template>
  <v-bottom-navigation
    v-if="chosen != 0"
    absolute
    background-color="#5B5C9D"
    height="60px"
    class="fixed-bottom"
  >
    <div
      class="d-flex align-center"
      @click="runChoosePlaylist"
    >
      <div>
        <v-icon color="white">
          mdi-plus-box
        </v-icon>
      </div>
      <div style="color: white;">
        선택
      </div>
    </div>
  </v-bottom-navigation>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  name: 'AddButtonBottom',
  components: {
  },
  props: {
    chosen: {
      type: Object, default() {}
    }
  },
  computed: {
    ...mapState('board', {
      playlistOrPlayroom: state => state.playlistOrPlayroom,
    })
  },
  created: function() {
    console.log("ABBP에서", this.playlistOrPlayroom)
  },
  methods: {
    ...mapActions('board', [
      'choosePlaylist',
      'choosePlayroom'
    ]),
    runChoosePlaylist() {
      if(this.playlistOrPlayroom == "playlist") {
        console.log("you chosen", this.chosen)
        this.choosePlaylist(this.chosen)
        this.$router.push({ name: 'BoardForm'})
      }
      else if(this.playlistOrPlayroom == "playroom") {
        console.log("you chosen playroom, and ", this.chosen)
        this.choosePlayroom(this.chosen)
        this.$router.push({ name: 'BoardForm'})
      }
    }
  },

}
</script>
