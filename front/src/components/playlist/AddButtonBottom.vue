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
      class="videoCounter shadow-s"
    />
    <div class="d-flex text-center align-center">
      <div
        class="d-flex-column justify-center mx-5 font-3 clickable"
      >
        <div @click="onClickAdd">
          <v-icon
            color="white"
          >
            mdi-plus-box
          </v-icon>
        </div>
        <div style="color: white;">
          추가
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
    <timeout-dialog-k
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
import TimeoutDialogK from '../common/TimeoutDialogK.vue'

export default {
  name: 'AddButtonBottom',
  components: {
    TimeoutDialogK
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
      return '총 ' + String(this.selectedVideos.length) + '개의 영상이 추가되었습니다.\n 중복된 영상은 제거됩니다.'
    }
  },
  methods: {
    ...mapActions('video', [
      'addVideos',
      'watchingVideos',
    ]),
    onClickAdd () {
      this.showTimeoutDialog = true
    },
    onTimeout () {
      this.addVideos()
      this.showTimeoutDialog = false
    }
  }
}
</script>
