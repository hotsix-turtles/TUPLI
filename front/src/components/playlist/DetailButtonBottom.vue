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
      offset-x="30"
      offset-y="10"
      overlap
      class="videoCounter"
    />
    <div class="d-flex text-center align-center">
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div>
          <v-icon color="white">
            mdi-youtube
          </v-icon>
        </div>
        <div style="color: white;">
          플레이룸
        </div>
      </div>
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div>
          <v-icon color="white">
            mdi-play-circle
          </v-icon>
        </div>
        <div style="color: white;">
          내 플레이리스트
        </div>
      </div>
      <!-- <div
        class="d-flex-column justify-center mx-3 font-3 clickable"
      >
        <div>
          <v-icon color="white">
            mdi-bookmark-outline
          </v-icon>
        </div>
        <div style="color: white;">
          저장하기
        </div>
      </div> -->
    </div>
  </v-bottom-navigation>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'DetailButtonBottom',
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
      'removeVideos'
    ])
  }
}
</script>
