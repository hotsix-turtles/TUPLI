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
import { mapActions } from 'vuex'

export default {
  name: 'AddButtonBottom',
  components: {
  },
  props: {
    selected: {
      type: Object, default() {}
    }
  },
  methods: {
    ...mapActions('board', [
      'selectPlaylist'
    ]),
    runSelectPlaylist() {
      console.log("you selected", this.selected)
      this.selectPlaylist(this.selected)
      this.$router.push({ name: 'BoardForm'})
    }
  }
}
</script>
