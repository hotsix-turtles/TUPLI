<template>
  <div>
    <!-- playroom 썸네일 -->
    <div class="video-thumbnail">
      <img
        :src="playroom.image"
        alt="썸네일"
        width="390px"
        height="235px"
        @click="$router.push({ name: 'PlayroomDetail', params: { id: playroom.id }})"
      >
      <!-- <div
              v-if="playroom.onPlay"
              class="on-play"
            > 나중에 이걸로 변경-->
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
        <!-- 나중에 src 변경 필요 -->
        <div class="d-flex">
          <img
            style="border-radius: 100px;"
            :src="playroom.profileImg ? playroom.profileImg : 'data:image/jpeg;base64,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'"
            alt="프로필 사진"
            width="50px"
            height="50px"
            class="ml-2 mr-3"
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
