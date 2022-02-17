/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <v-bottom-navigation
      v-if="selectedVideos.length"
      absolute
      background-color="#5B5C9D"
      height="75px"
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
          @click="watchingVideos(selectedVideos)"
        >
          <div>
            <v-icon
              color="white"
            >
              mdi-play-circle
            </v-icon>
          </div>
          <div style="color: white;">
            영상보기
          </div>
        </div>
        <div
          class="d-flex-column justify-center mx-5 font-3 clickable"
          @click="onClickMyPlaylists"
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
      </div>
      <!-- 내 플레이리스트 목록 -->
      <!-- <div
      v-if="showMyPlaylists"
      class="my-playlist animate__animated animate__slideInUp"
    >
      <playlist-list-item-small
        :playlists="myPlaylists"
        :is-radio-btn="true"
        :playlist-readonly="true"
      />
    </div> -->
    </v-bottom-navigation>
    <v-row justify="center">
      <v-dialog
        v-model="dialog"
        scrollable
        max-width="300px"
      >
        <v-card>
          <v-card-title>내 플레이리스트</v-card-title>
          <v-divider />
          <v-card-text style="height: 300px;">
            <v-radio-group

              v-model="selectedPlaylist"
              column
            >
              <v-radio
                v-for="(playlist, idx) in myPlaylists"
                :key="idx"
                :label="playlist.title"
                :value="playlist"
                class="my-2 semi-bold"
                color="#5B5C9D"
              />
            </v-radio-group>
          </v-card-text>
          <v-divider />
          <v-card-actions>
            <v-btn
              color="#5B5C9D"
              text
              style="font-size: 16px;"
              @click="onClickSelect"
            >
              내 플레이리스트에 담기
            </v-btn>
            <v-btn
              color="#8B8B8B"
              text
              style="font-size: 16px;"
              @click="dialog = false"
            >
              취소
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
    <login-dialog
      :show="showLoginDialog"
      @on-click="showLoginDialog = false"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import LoginDialog from '../../components/common/LoginDialog.vue';

export default {
  name: 'DetailButtonBottom',
  components: {
    LoginDialog,
  },
  props: {
  },
  data: function () {
    return {
      selectedPlaylist: '',
      dialog: false,
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState({
      isLogin: state => state.isLogin,
    }),
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    }),
    ...mapState('board', {
      myPlaylists: state => state.myPlaylists,
    }),
  },
  methods: {
    ...mapActions('video', [
      'removeVideos',
      'watchingVideos',
      'selectVideos',
      'resetVideoSelectState',
    ]),
    ...mapActions('playlist', [
      'addVideoInPlaylist',
    ]),
    ...mapActions('board', [
      'getMyPlaylists',
    ]),
    onClickMyPlaylists: function () {
      console.log(this.isLogin)
      if (this.isLogin) {
        this.getMyPlaylists()
        this.dialog = true
      } else {
        this.showLoginDialog = true
      }
    },
    onClickSelect: function () {
      const formData = this.selectedPlaylist
      for (let selectedVideo of this.selectedVideos) {
        const idx = formData.videos.findIndex(i => i.videoId === selectedVideo.videoId)
        if (idx === -1) {
          formData.videos.push(selectedVideo)
        }
      }
      const params = {
        formData: formData,
        id: formData.id,
      }
      this.addVideoInPlaylist(params)
      this.dialog = false
      this.resetVideoSelectState()
    },
  }
}
</script>

<style scoped>
</style>
