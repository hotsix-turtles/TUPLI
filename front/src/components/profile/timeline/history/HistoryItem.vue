<template>
  <div>
    <!-- 플레이룸 -->
    <div
      v-if="activity.type === 'playroom'"
      class="d-flex flex-column"
    >
      <div class="d-flex align-start">
        <div class="d-flex align-center profile-img-medium">
          <img
            :src="ImgUrl(activity.userProfileImg)"
            alt="userImg"
          >
        </div>
        <div class="d-flex flex-column align-start">
          <p>{{ activity.nickName }}님이 새로운 플레이룸를 작성하였습니다.</p>
          <p>{{ time_playroom }}</p>
        </div>
      </div>
      <div>
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
            @click="playroomClick"
          >
          <!-- playroom 정보 -->
          <div class="d-flex justify-space-between">
            <div class="d-flex flex-column align-start">
              <h4
                class="mb-0 mt-2"
              >
                {{ activity.title }}
              </h4>

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
            <div class="d-flex">
              <v-icon color="#5B5C9D">
                mdi-heart
              </v-icon>
              <p class="mb-0">
                {{ activity.likesCnt }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 플레이리스트 -->
    <div
      v-else-if="activity.type === 'playlist'"
      class="d-flex flex-column"
      @click="goPlaylist"
    >
      <div class="d-flex align-start">
        <div class="d-flex align-center profile-img-medium">
          <img
            :src="ImgUrl(activity.userProfileImg)"
            alt="userImg"
          >
        </div>
        <div class="d-flex flex-column align-start">
          <p>{{ activity.nickName }}님이 새로운 플레이리스트를 작성하였습니다.</p>
          <p>{{ time }}</p>
        </div>
      </div>
      <div>
        <!-- 플레이리스트 -->
        <div
          class="mt-5"
        >
          <!-- playlist 썸네일 -->
          <span
            class="playlist-cd-case"
          >
            <div
              id="case"
              @click="goPlaylist"
            >
              <img
                :src="ImgUrl(activity.videos.thumbnail)"
                alt="썸네일"
              >
              <div />

            </div>
          </span>
          <!-- playlist 정보 -->
          <div class="d-flex justify-space-between">
            <div class="d-flex flex-column align-start">
              <h4
                class="mb-0 mt-2"
                @click="goPlaylist"
              >
                {{ activity.title }}
              </h4>

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
            <div class="d-flex">
              <v-icon color="#5B5C9D">
                mdi-heart
              </v-icon>
              <p class="mb-0">
                {{ activity.likesCnt }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 게시물 -->
    <div
      v-else-if="activity.type === 'board'"
      class="d-flex align-center"
      @click="goBoard"
    >
      <div class="d-flex align-start">
        <div class="d-flex align-center profile-img-medium">
          <img
            :src="ImgUrl(activity.userProfileImg)"
            alt="userImg"
          >
        </div>
        <div class="d-flex flex-column align-start">
          <p>{{ activity.nickName }}님이 새로운 게시글을 작성하였습니다.</p>
          <p>{{ time }}</p>
          <p>{{ activity.content }}</p>
          <div class="d-flex">
            <v-icon color="#5B5C9D">
              mdi-heart
            </v-icon>
            <p class="mb-0">
              {{ activity.likesCnt }}
            </p>
          </div>
        </div>
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
    playroomClick: function() {
      // if 이미 끝났으면, 알림만 주기
      // 아직 안끝났으면 접속
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
