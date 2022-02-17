/* eslint-disable vue/require-default-prop */
<template>
  <v-bottom-navigation
    v-if="selectedVideos.length"
    absolute
    background-color="#5B5C9D"
    height="66px"
    :input-value="selectedVideos.length > 0"
    class="fixed-bottom animate__animated animate__slideInUp"
  >
    <v-badge
      :content="selectedVideos.length"
      color="#F6F7FF"
      offset-x="120"
      offset-y="10"
      overlap
      class="videoCounter"
    />
    <div class="d-flex text-center align-center">
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div>
          <v-icon
            color="white"
            @click="removeVideos"
          >
            mdi-trash-can-outline
          </v-icon>
        </div>
        <div style="color: white;">
          삭제
        </div>
      </div>
      <div class="mx-5" />
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div>
          <v-icon
            color="white"
            @click="watchingVideos(selectedVideos)"
          >
            mdi-play-circle
          </v-icon>
        </div>
        <div style="color: white;">
          영상보기
        </div>
      </div>
    </div>
  </v-bottom-navigation>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'RemoveButtonBottom',
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
      'removeVideos',
      'watchingVideos',
    ])
  }
}
</script>
