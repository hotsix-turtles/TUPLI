/* eslint-disable vue/require-default-prop */
<template>
  <v-bottom-navigation
    v-if="selectedVideos.length"
    absolute
    background-color="#5B5C9D"
    height="50px"
    :input-value="selectedVideos.length > 0"
    class="fixed-bottom"
  >
    <v-badge
      :content="selectedVideos.length"
      color="#8B8B8B"
      offset-x="120"
      offset-y="10"
      overlap
      class="videoCounter"
    />
    <v-btn @click="addVideos">
      <span style="color: white;">추가</span>
      <v-icon color="white">
        mdi-plus-box
      </v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'AddButtonBottom',
  components: {
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    cntClickedItems: {
      type: Number,
    }
  },
  computed: {
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    })
  },
  methods: {
    ...mapActions('video', [
      'addVideos'
    ])
  }
}
</script>
