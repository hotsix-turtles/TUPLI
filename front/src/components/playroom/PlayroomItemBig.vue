<template>
  <div>
    <!-- playroom 썸네일 -->
    <div class="video-thumbnail">
      <img
        :src="playroom.image ? playroom.image : playroom.videos[0].thumbnail"
        alt="썸네일"
        width="100%"
        height="auto"
        @click="$router.push({ name: 'PlayroomDetail', params: { id: playroom.id }})"
      >
      <div
        v-if="playroom.onPlay"
        class="on-play-big"
      >
        ON PLAY
      </div>
      <span class="duration-big d-flex">
        <div>
          <v-icon
            color="white"
            class="mr-1"
            style="padding-right: 6px; padding-left: 2px: padding-bottom:4px;"
            small
          >
            mdi-account-multiple
          </v-icon>
        </div>
        <span>{{ playroom.userCount }}</span>
      </span>
    </div>
    <div class="d-flex-column mb-5">
      <!-- playroom 정보 -->
      <div class="d-flex justify-space-between">
        <!-- 프로필 이미지 -->
        <div class="d-flex">
          <img
            style="border-radius: 100px;"
            :src="ImgUrl(playroom.profileImg)"
            alt="프로필 사진"
            width="50px"
            height="50px"
            class="ml-2 mr-3"
            @click="$router.push({ name: 'Profile', params: { userId : playroom.userId }})"
          >
          <!-- 제목, 방장, 플레이시간 -->
          <div class="d-flex-column mr-1">
            <div
              class="text-left semi-bold txt-1"
              @click="$router.push({ name: 'PlayroomDetail', params: { id: playroom.id }})"
            >
              {{ playroom.title }}
            </div>
            <div class="d-flex font-3 color-dark-gray">
              <div @click="$router.push({ name: 'Profile', params: { userId : playroom.userId }})">
                {{ playroom.nickname ? playroom.nickname : playroom.user.nickname }}
              </div>
              <v-icon color="#8B8B8B">
                mdi-circle-small
              </v-icon>
              <div>
                {{ playroom.playTime }}
              </div>
            </div>
            <!-- 태그 -->
            <tags
              v-if="playroom.tags"
              :tags="playroom.tags.split(',')"
            />
          </div>
        </div>
        <!-- 좋아요 -->
        <div class="d-flex-column text-center mr-2">
          <div
            v-if="playroom.isLiked"
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
            {{ playroom.likesCnt }}
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
import Tags from '../common/Tags.vue'
import LoginDialog from '../../components/common/LoginDialog.vue';
import { getImage } from '../../utils/utils'

export default {
  name: 'PlayroomItemBig',
  components: {
    Tags,
    LoginDialog,
  },
  props: {
    playroom: { type: Object, default() { {} } },
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
    ...mapActions('playroom', [
      'likePlayroom',
      'unlikePlayroom',
    ]),
    onClickLike: function () {
      if (this.isLogin) {
        this.playroom.isLiked = true
        this.playroom.likesCnt++
        this.likePlayroom(this.playroom.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickUnlike: function () {
      if (this.isLogin) {
        this.playroom.isLiked = false
        this.playroom.likesCnt--
        this.unlikePlayroom(this.playroom.id)
      } else {
        this.showLoginDialog = true
      }
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
  }
}
</script>

<style scoped>

</style>
