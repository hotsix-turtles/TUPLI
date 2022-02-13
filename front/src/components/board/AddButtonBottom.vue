/* eslint-disable vue/require-default-prop */
<template>
  <v-bottom-navigation
    v-if="selected != 0"
    absolute
    background-color="#5B5C9D"
    height="50px"
    class="fixed-bottom"
  >
    <div
      class="d-flex align-center"
      @click="runSelectPlaylist"
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
    selected: {
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
      'selectPlaylist',
      'selectPlayroom'
    ]),
    runSelectPlaylist() {
      if(this.playlistOrPlayroom == "playlist") {
        console.log("you selected", this.selected)
        this.selectPlaylist(this.selected)
        this.$router.push({ name: 'BoardForm'})
      }
      else if(this.playlistOrPlayroom == "playroom") {
        console.log("you selected playroom, and ", this.selected)
        this.selectPlayroom(this.selected)
        this.$router.push({ name: 'BoardForm'})
      }
    }
  },

}
</script>
