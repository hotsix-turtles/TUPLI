<template>
  <div>
    <!-- playroom 썸네일 -->
    <div class="video-thumbnail">
      <img
        :src="playroom.image"
        alt="썸네일"
        width="390px"
        height="235px"
      >
      <!-- <div
              v-if="playroom.onPlay"
              class="on-play"
            > 나중에 이걸로 변경-->
      <div
        class="on-play-big"
      >
        ON PLAY
      </div>
      <span class="duration-big d-flex">
        <div>
          <v-icon
            color="white"
            class="mr-1"
            small
          >
            mdi-account-multiple
          </v-icon>
        </div>
        <span>132</span>
        <!-- {{ playroom.userCount }} 나중에 바꿀 부분 -->
      </span>
    </div>
    <div class="d-flex-column mb-5">
      <!-- playroom 정보 -->
      <div class="d-flex justify-space-between">
        <!-- 프로필 이미지 -->
        <div class="d-flex">
          <img
            style="border-radius: 100px;"
            src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
            alt="프로필 사진"
            width="46"
            height="46"
            class="ml-2 mr-3"
          >
          <!-- 제목 내용 -->
          <div class="d-flex-column">
            <div class="text-left semi-bold">
              {{ playroom.title }}
            </div>
            <div class="d-flex font-3 color-dark-gray">
              <div>
                {{ playroom.nickname }}
              </div>
              <v-icon color="#8B8B8B">
                mdi-circle-small
              </v-icon>
              <div>
                {{ playroom.startTime }} ~ {{ playroom.endTime }}
              </div>
            </div>
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
      <!-- 태그 -->
      <tags
        v-if="playroom.tags"
        :tags="playroom.tags.split(',')"
        class="mx-2 my-1"
      />
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import Tags from '../common/Tags.vue'

export default {
  name: 'PlayroomItemBig',
  components: { Tags },
  props: {
    playroom: { type: Object, default() { {} } },
  },
  data() {
    return {
    }
  },
  computed: {
  },
  methods: {
    ...mapActions('playroom', [
      'likePlayroom',
      'unlikePlayroom',
    ]),
    onClickLike: function () {
      this.playroom.isLiked = true
      this.playroom.likesCnt++
      this.likePlayroom(this.playroom.id)
    },
    onClickUnlike: function () {
      this.playroom.isLiked = false
      this.playroom.likesCnt--
      this.unlikePlayroom(this.playroom.id)
    },
  }
}
</script>

<style scoped>

</style>
