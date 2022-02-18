<template>
  <div>
    <!-- video 썸네일 -->
    <div class="video-thumbnail">
      <img
        :src="video.thumbnail"
        alt="썸네일"
        width="100%"
        height="225px"
        style="object-fit: cover;"
        @click="watchingVideo(video)"
      >
    </div>
    <div class="d-flex-column mb-5">
      <!-- video 정보 -->
      <div class="d-flex justify-space-between">
        <div class="d-flex">
          <!-- 제목, 채널명, 날짜 -->
          <div class="d-flex-column ml-3 mr-2">
            <div
              class="text-left semi-bold txt-2"
              @click="watchingVideo(video)"
            >
              {{ video.title }}
            </div>
            <div class="d-flex font-3 color-dark-gray">
              <div>
                {{ video.channelTitle }}
              </div>
              <v-icon color="#8B8B8B">
                mdi-circle-small
              </v-icon>
              <div>
                {{ video.date.slice(0,10) }}
              </div>
            </div>
          </div>
        </div>
        <!-- 좋아요 -->
        <div class="d-flex-column text-center mr-2">
          <div
            v-if="video.isLiked"
            class="animate__animated animate__heartBeat"
            @click="onClickUnlike"
          >
            <v-icon color="#5B5C9D">
              mdi-cards-heart
            </v-icon>
          </div>
          <div
            v-else
            @click="onClickLike"
          >
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
          <div class="font-4">
            {{ video.likesCnt }}
          </div>
        </div>
      </div>
    </div>
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
  name: 'VideoItemBig',
  components: {
    LoginDialog,
  },
  props: {
    video: { type: Object, default() { {} } },
  },
  data() {
    return {
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState({
      isLogin: state => state.isLogin,
    }),
  },
  methods: {
    ...mapActions('video', [
      'watchingVideo',
      'likeVideo',
      'unlikeVideo',
    ]),
    onClickLike: function () {
      if (this.isLogin) {
        this.video.isLiked = true
        this.video.likesCnt++
        this.likeVideo(this.video)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickUnlike: function () {
      if (this.isLogin) {
        this.video.isLiked = false
        this.video.likesCnt--
        this.unlikeVideo(this.video.id)
      } else {
        this.showLoginDialog = true
      }
    },
  }
}
</script>

<style scoped>

</style>
