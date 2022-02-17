<template>
  <div>
    <!-- 플레이룸 -->
    <div
      v-if="activity.type === 'playroom'"
    >
      <div
        class="d-flex flex-column align-center"
      >
        <div
          class="d-flex align-start"
          style="width:98%"
        >
          <div class="d-flex align-center">
            <img
              :src="ImgUrl(activity.userProfileImg)"
              alt="userImg"
              class="profile-img-history"
            >
          </div>
          <div class="d-flex flex-column align-start">
            <span class="history-content"><span class="history-nickname">{{ activity.nickName }}</span>님이 새로운 플레이룸를 작성하였습니다.</span>
            <span class="history-date">{{ time_playroom }}</span>
          </div>
        </div>
        <div class="history-playroom-content">
          <!-- 플레이룸 -->
          <div
            class="mt-5"
          >
            <!-- playlist 썸네일 -->
            <img
              :src="ImgUrl(activity.videos.thumbnail)"
              alt="썸네일"
              width="390"
              height="219"
              @click="goPlayroom"
            >
            <!-- playroom 정보 -->
            <div class="d-flex justify-space-between">
              <div class="d-flex flex-column align-start ml-4">
                <span
                  class="history-content-title"
                >
                  {{ activity.title }}
                </span>

                <div class="d-flex">
                  <p
                    v-for="tag in allTags"
                    :key="tag.id"
                    class="mb-0 main-tag"
                  >
                    {{ tag }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 좋아요 -->
      <div
        v-if="activity.userLikesYN === 'N'"
        class="d-flex justify-end mr-5"
        @click="onClickPlayroomLike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart-outline
        </v-icon>
        <p class="history-icon-text">
          {{ activity.likesCnt }}
        </p>
      </div>
      <!-- 좋아요 취소 -->
      <div
        v-if="activity.userLikesYN === 'Y'"
        class="d-flex justify-end mr-5"
        @click="onClickPlayroomUnlike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart
        </v-icon>
        <p class="history-icon-text">
          {{ activity.likesCnt }}
        </p>
      </div>
    </div>
    <!-- 플레이리스트 -->
    <div
      v-else-if="activity.type === 'playlist'"
    >
      <div
        class="d-flex flex-column align-center"
      >
        <div
          class="d-flex align-start"
          style="width:98%"
          @click="goPlaylist"
        >
          <div class="d-flex align-center">
            <img
              :src="ImgUrl(activity.userProfileImg)"
              alt="userImg"
              class="profile-img-history"
            >
          </div>
          <div class="d-flex flex-column align-start">
            <span class="history-content"><span class="history-nickname">{{ activity.nickName }}</span>님이 새로운 플레이리스트를 작성하였습니다.</span>
            <span class="history-date mb-1">{{ time }}</span>
          </div>
        </div>
        <div class="d-flex flex-column align-center">
          <div class="d-flex justify-center history-playlist-content">
            <!-- 플레이리스트 -->
            <div
              class="mt-3"
            >
              <!-- playlist 썸네일 -->
              <span
                class="playlist-cd-case-medium"
              >
                <div
                  id="case"
                  class="mx-3 mt-2"
                  @click="goPlaylist"
                >
                  <img
                    :src="ImgUrl(activity.videos.thumbnail)"
                    alt="썸네일"
                  >
                  <!-- cd 구멍 -->
                  <div />
                </div>
              </span>
              <!-- playlist 정보 -->
              <div class="mx-10 mt-2 mb-1">
                <div class="d-flex flex-column align-start">
                  <span
                    class="history-content-title "
                    @click="goPlaylist"
                  >
                    {{ activity.title }}
                  </span>

                  <div class="d-flex">
                    <p
                      v-for="tag in allTags"
                      :key="tag.id"
                      class="mb-0 main-tag"
                    >
                      {{ tag }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- playlist 좋아요 -->
      <!-- 좋아요 -->
      <div
        v-if="activity.userLikesYN === 'N'"
        class="d-flex justify-end mr-5"
        @click="onClickPlaylistLike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart-outline
        </v-icon>
        <p
          class="history-icon-text"
        >
          {{ activity.likesCnt }}
        </p>
      </div>
      <!-- 좋아요 취소 -->
      <div
        v-if="activity.userLikesYN === 'Y'"
        class="d-flex justify-end mr-5"
        @click="onClickPlaylistUnlike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart
        </v-icon>
        <p
          class="history-icon-text"
        >
          {{ activity.likesCnt }}
        </p>
      </div>
    </div>
    <!-- 게시물 -->
    <div
      v-else-if="activity.type === 'board'"
    >
      <div
        class="d-flex flex-column align-center"
        @click="goBoard"
      >
        <div
          class="d-flex flex-column align-start"
          style="width:98%"
        >
          <div class="d-flex align-start">
            <img
              :src="ImgUrl(activity.userProfileImg)"
              class="profile-img-history"
              alt="userImg"
            >
            <div class="d-flex flex-column align-start">
              <span class="history-content"><span class="history-nickname">{{ activity.nickName }}</span>님이 새로운 게시물을 작성하였습니다.</span>
              <span class="history-date">{{ time }}</span>
            </div>
          </div>
        </div>
        <div
          class="d-flex history-board-content"
        >
          <div>
            <span>{{ activity.content }}</span>
          </div>
        </div>
      </div>
      <!-- 좋아요 -->
      <div
        v-if="activity.userLikesYN === 'N'"
        class="d-flex justify-end mr-5"
        @click="onClickBoardLike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart-outline
        </v-icon>
        <p
          class="history-icon-text"
        >
          {{ activity.likesCnt }}
        </p>
      </div>
      <!-- 좋아요 취소 -->
      <div
        v-if="activity.userLikesYN === 'Y'"
        class="d-flex justify-end mr-5"
        @click="onClickBoardUnlike"
      >
        <v-icon
          color="#5B5C9D"
          size="18"
        >
          mdi-heart
        </v-icon>
        <p
          class="history-icon-text"
        >
          {{ activity.likesCnt }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'
import { mapState } from 'vuex'
import { timeConverter } from '@/utils/utils';


export default {
  name: 'HistoryItem',
  props: {
    activity: { type: Object, default() { {} } }
  },
  data: function() {
    return {
      allTags: [],
      time: [],
      time_playroom: [],

    }
  },
  computed: {
    ...mapState(['authToken', 'nickname', 'image'])
  },
  created: function() {
    // console.log('활동', this.activity)
    this.getTag()
    this.createdAt()
    this.createdAt_playroom()
  },
  methods: {
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 태그
    getTag: function() {
      if (this.activity.tags) {
        this.allTags = this.activity.tags.split(',')
      }
      else {
        this.allTags = []
      }
    },
    // 플레이룸 상세로 가기
    goPlayroom: function() {
      this.$router.push({ name: 'PlayroomDetail', params: { id: this.activity.id }})
    },
    // 플레이리스트 상세로 가기
    goPlaylist: function() {
      this.$router.push({ name: 'PlaylistDetail', params: { playlistId : this.activity.id }})
    },
    // 게시물 상세로 가기
    goBoard: function() {
      this.$router.push({ name: 'BoardDetail', params: { boardId : this.activity.id }})
    },
    // 작성 시간
    createdAt: function() {
      this.time = timeConverter(this.activity.createdAt)
      // console.log('시간 잘 나오나', this.time)
    },
    // 작성 시간
    createdAt_playroom: function() {
      this.time_playroom = timeConverter(this.activity.startTime)
      // console.log('시간 잘 나오나', this.time)
    },

  }

}
</script>

<style>

</style>
