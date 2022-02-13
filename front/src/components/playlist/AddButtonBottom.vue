/* eslint-disable vue/require-default-prop */
<template>
  <v-bottom-navigation
    v-if="selectedVideos.length"
    absolute
    background-color="#5B5C9D"
    height="60px"
    :input-value="selectedVideos.length > 0"
    class="fixed-bottom animate__animated animate__slideInUp"
  >
    <v-badge
      :content="selectedVideos.length"
      color="#F6F7FF"
      offset-x="120"
      offset-y="10"
      overlap
      class="videoCounter shadow-s"
    />
    <div
      class="d-flex align-center"
      @click="addVideos"
    >
      <div>
        <v-icon color="white">
          mdi-plus-box
        </v-icon>
      </div>
      <div style="color: white;">
        추가
      </div>
    </div>
  </v-bottom-navigation>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'AddButtonBottom',
  components: {
  },
  props: {
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
