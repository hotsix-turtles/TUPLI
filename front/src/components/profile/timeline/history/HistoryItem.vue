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

                <div class="d-flex flex-wrap">
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

                  <div class="d-flex flex-wrap">
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
          class="d-flex flex-column history-board-playlist align-center"
        >
          <div
            class="mx-3 history-board-content-new"
            style=""
          >
            <span class="">{{ activity.content }}</span>
          </div>

          <!-- 공유한 게시물이 있을 때 - 플레이룸 -->
          <div
            v-if="activity.contentType === 'playroom'"
            @click="goBoard(activity.contents.id)"
          >
            <div
              class="d-flex flex-column align-center"
            >
              <div class="history-playroom-content">
                <!-- 플레이룸 -->
                <div
                  class="mt-5"
                >
                  <div
                    class="d-flex align-center"
                  >
                    <div
                      class="d-flex align-center"
                      style="width: 30px; height: 30px; margin-bottom: 8px;"
                    >
                      <img
                        :src="ImgUrl(activity.contents.userProfileImg)"
                        alt="userImg"
                        style="border-radius: 100%; width: 30px; height: 30px; margin-left: 16px;"
                      >
                    </div>
                    <span
                      class="history-nickname"
                      style="margin-left: 22px; margin-bottom: 15px;"
                    >{{ activity.contents.nickName }}</span>
                  </div>

                  <!-- playlist 썸네일 -->
                  <img
                    :src="ImgUrl(activity.contents.videos.thumbnail)"
                    alt="썸네일"
                    width="390"
                    height="219"
                  >
                  <!-- playroom 정보 -->
                  <div class="d-flex justify-space-between">
                    <div class="d-flex flex-column align-start ml-4">
                      <span
                        class="history-content-title"
                      >
                        {{ activity.contents.videos.title }}
                      </span>

                      <div class="d-flex flex-wrap">
                        <p
                          v-for="tag in allBoardTags"
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

          <!-- 공유한 게시물이 있을 때 - 플레이리스트 -->
          <div v-else-if="activity.contentType === 'playlist'">
            <div class="d-flex flex-column align-center">
              <div class="d-flex justify-center history-playlist-content">
                <!-- 플레이리스트 -->
                <div
                  class="mt-3"
                >
                  <div class="d-flex align-center pl-8">
                    <div class="profile-img-small-history">
                      <img
                        :src="ImgUrl(activity.contents.userProfileImg)"
                        alt=""
                      >
                    </div>
                    <span class="history-nickname ml-3">{{ activity.contents.nickName }}</span>
                  </div>
                  <!-- playlist 썸네일 -->
                  <span
                    class="playlist-cd-case-small"
                  >
                    <div
                      id="case"
                      class="mx-3 mt-3"
                      @click="goPlaylist"
                    >
                      <img
                        :src="ImgUrl(activity.contents.videos.thumbnail)"
                        alt="썸네일"
                      >
                      <!-- cd 구멍 -->
                      <div />
                    </div>
                  </span>
                  <!-- playlist 정보 -->
                  <div
                    class="ml-12 mt-2 mb-1"
                    style="width:240px;"
                  >
                    <div class="d-flex flex-column align-start">
                      <span
                        style="font-size: 14px; font-weight: 600; text-align: left; line-height:120%;"
                        @click="goPlaylist"
                      >
                        {{ activity.contents.videos.title }}
                      </span>

                      <div class="d-flex flex-wrap">
                        <p
                          v-for="tag in allBoardTags"
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
      allBoardTags: [],
      time: [],
      time_playroom: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'nickname', 'image'])
  },
  created: function() {
    this.getTag()
    // this.getBoardTag()
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
      if (this.activity.type === 'board') {
        if (this.activity.contents) {
          this.allBoardTags = this.activity.contents.tags.split(',')
        }
        else {
          this.allBoardTags = []
        }
      }
      else {

        if (this.activity.tags) {
          this.allTags = this.activity.tags.split(',')
        }
        else {
          this.allTags = []
        }
      }
    },

    // 태그
    // getBoardTag: function() {
    //   if (this.activity.contents.tags) {
    //     this.allBoardTags = this.activity.contents.tags.split(',')
    //   }
    //   else {
    //     this.allBoardTags = []
    //   }
    // },

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
    // 좋아요
    // 좋아요 - 플레이룸
    onClickPlayroomLike: function () {
      console.log('좋아요 누름', this.activity.userLikesYN)
      this.activity.userLikesYN = 'Y'
      this.activity.likesCnt++
      this.likePlayroom(this.activity.id)
    },
    onClickPlayroomUnlike: function () {
      console.log('좋아요 취소', this.activity.userLikesYN)
      this.activity.userLikesYN = 'N'
      this.activity.likesCnt--
      this.unlikePlayroom(this.activity.id)
    },
    // 좋아요 - 플레이리스트
    onClickPlaylistLike: function () {
      console.log('좋아요 플리 누름', this.activity.userLikesYN)
      this.activity.userLikesYN = 'Y'
      this.activity.likesCnt++
      this.likePlaylist(this.activity.id)
    },
    onClickPlaylistUnlike: function () {
      console.log('좋아요 취소 플리 누름', this.activity.userLikesYN)
      this.activity.userLikesYN = 'N'
      this.activity.likesCnt--
      this.unlikePlaylist(this.activity.id)
    },
    // 좋아요 - 게시물
    onClickBoardLike: function () {
      console.log('좋아요 게시글 누름', this.activity.userLikesYN)
      this.activity.userLikesYN = 'Y'
      this.activity.likesCnt++
      this.likeBoard(this.activity.id)
    },
    onClickBoardUnlike: function () {
      console.log('좋아요 취소 게시글 누름', this.activity.userLikesYN)
      this.activity.userLikesYN = 'N'
      this.activity.likesCnt--
      this.unlikeBoard(this.activity.id)
    },

  }

}
</script>

<style>

</style>
