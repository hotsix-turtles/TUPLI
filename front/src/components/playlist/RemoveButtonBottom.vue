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
      offset-x="30"
      offset-y="10"
      overlap
      class="videoCounter"
    />
    <div class="d-flex text-center align-center">
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div @click="onClickRemove">
          <v-icon
            color="white"
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
            mdi-play
          </v-icon>
        </div>
        <div style="color: white;">
          영상보기
        </div>
      </div>
    </div>
    <timeout-dialog
      :content="timeoutMsg"
      :show="showTimeoutDialog"
      hide-progress
      :persistent="false"
      timeout="1700"
      @timeout="onTimeout"
      @click="onTimeout"
    />
  </v-bottom-navigation>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import TimeoutDialog from '../common/TimeoutDialog.vue'

export default {
  name: 'RemoveButtonBottom',
  components: {
    TimeoutDialog
  },
  props: {
  },
  data: function () {
    return {
      showTimeoutDialog: false,
    }
  },
  computed: {
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    }),
    timeoutMsg: function () {
      return '총 ' + String(this.selectedVideos.length) + '개의 영상이 삭제되었습니다.'
    }
  },
  methods: {
    ...mapActions('video', [
      'removeVideos',
      'watchingVideos',
    ]),
    onClickRemove () {
      console.log('onClickRemove')
      this.showTimeoutDialog = true
    },
    onTimeout () {
      this.removeVideos()
      this.showTimeoutDialog = false
    }
  }
}
</script>
