<template>
  <div>
    <!-- 플레이룸 -->
    <div v-if="content.type == 'playroom'">
      <playroom-item-big :playroom="content" />
      <div class="border-line" />
    </div>

    <!-- 플레이리스트 -->
    <div
      v-else-if="content.type == 'playlist'"
      class="d-flex flex-column align-center mt-5"
    >
      <!-- playlist 썸네일 -->
      <span
        class="playlist-cd-case"
        @click="goPlaylist"
      >
        <div id="case" />
        <img
          :src="thumbnailImage"
          alt="썸네일"
        >
        <div />
      </span>
      <!-- playlist 정보 -->
      <div
        class="d-flex flex-column justify-center mt-2"
        style="width:100%;"
      >
        <!-- playlist 정보 -->
        <div class="d-flex cont">
          <img
            class="profile-img-main"
            :src="ImgUrl(content.userProfileImg)"
            alt="프로필 사진"
            @click="setProfile"
          >
          <div
            class="d-flex flex-column align-start"
            style="width:60%"
          >
            <p
              v-if="content.isRecommend == true"
              class="main-recommend-icon mt-2"
            >
              추천
            </p>
            <p
              class="main-title-playlist twolines txt-1"
              @click="goPlaylist"
            >
              {{ content.title }}
            </p>
            <p
              class="mb-0 main-username"
              @click="setProfile"
            >
              {{ content.nickName }}
            </p>
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

          <!-- 좋아요/댓글 -->
          <div class="d-flex mr-5 align-center">
            <div
              v-if="content.userLikesYN === 'Y'"
              class="d-flex flex-column align-center mx-1"
              @click="onClickPlaylistUnlike"
            >
              <v-icon color="#5B5C9D">
                mdi-cards-heart
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
            <div
              v-else-if="content.userLikesYN === 'N'"
              class="d-flex flex-column align-center mx-1"
              @click="onClickPlaylistLike"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <div class="main-icon-text">
                {{ content.likesCnt }}
              </div>
            </div>
            <div
              v-else-if="content.userLikesYN === null"
              class="d-flex flex-column align-center mx-1"
              @click="onClickPlaylistLike"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <div class="main-icon-text">
                {{ content.likesCnt }}
              </div>
            </div>
            <div
              class="ml-1"
              @click="onClickPlaylistComment"
            >
              <v-icon
                color="black"
                size="22"
              >
                mdi-comment-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.commentCnt }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>

    <!-- 게시글 -->
    <div
      v-else-if="content.type == 'board'"
    >
      <div
        class="d-flex flex-column mt-1 align-center"
        style="width:100%;"
      >
        <div
          class="d-flex align-center"
          style="width:96%;"
        >
          <img
            style="margin: 10px;"
            :src="ImgUrl(image)"
            class="profile-img-main"
            alt="프로필 사진"
            @click="setProfile"
          >

          <div
            class="d-flex flex-column align-start"
            style="width:67%;"
          >
            <div>
              <span
                class="main-username-board"
                @click="setProfile"
              >
                {{ content.nickName }}
              </span>
              <span
                v-if="content.contentType"
                style="font-size: 13px; padding-top: 2px;"
              >
                님의 공유 게시물
              </span>
            </div>
            <div
              class="main-content mt-1 txt-2"
              style="width:96%;"
              @click="goBoard"
            >
              <span>{{ content.content }}</span>
            </div>
          </div>
          <div>
            <!-- 리액션 아이콘 -->
            <div class="d-flex align-end mt-2">
              <!-- 좋아요 -->
              <div
                v-if="content.userLikesYN === 'Y'"
                class="d-flex flex-column justify-start mx-1"
                @click="onClickBoardUnlike"
              >
                <v-icon color="#5B5C9D">
                  mdi-cards-heart
                </v-icon>
                <div class="main-icon-text">
                  {{ content.likesCnt }}
                </div>
              </div>
              <div
                v-else-if="content.userLikesYN === 'N'"
                class="d-flex flex-column justify-start mx-1"
                @click="onClickBoardLike"
              >
                <v-icon color="#000000">
                  mdi-cards-heart-outline
                </v-icon>
                <div class="main-icon-text">
                  {{ content.likesCnt }}
                </div>
              </div>
              <div
                v-else
                class="d-flex flex-column align-center mx-1"
                @click="onClickBoardLike"
              >
                <v-icon color="#000000">
                  mdi-cards-heart-outline
                </v-icon>
                <div class="main-icon-text">
                  {{ content.likesCnt }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div>
          <!-- 공유한 게시물이 있을 때 - 플레이룸 -->
          <div
            v-if="content.contentType === 'playroom'"
            @click="goBoard(content.contents.id)"
          >
            <div
              class="d-flex flex-column align-center"
            >
              <div class="main-playroom-content mx-auto pl-2">
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
                        :src="ImgUrl(content.contents.userProfileImg)"
                        alt="userImg"
                        style="border-radius: 100%; width: 30px; height: 30px; margin-left: 16px;"
                      >
                    </div>
                    <span
                      class="history-nickname"
                      style="margin-left: 22px; margin-bottom: 15px;"
                    >{{ content.contents.nickName }}</span>
                  </div>

                  <!-- playlist 썸네일 -->
                  <img
                    :src="ImgUrl(content.contents.videos.thumbnail)"
                    alt="썸네일"
                    width="390"
                    height="219"
                  >
                  <!-- playroom 정보 -->
                  <div class="d-flex justify-space-between">
                    <div class="d-flex flex-column align-start ml-4 txt-2">
                      <span
                        class="history-content-title"
                      >
                        {{ content.contents.videos.title }}
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
          <div v-else-if="content.contentType === 'playlist'">
            <div class="d-flex flex-column align-center">
              <div class="d-flex justify-center main-playlist-content">
                <!-- 플레이리스트 -->
                <div
                  class="mt-3"
                >
                  <!-- playlist 썸네일 -->
                  <span
                    class="playlist-cd-case-small"
                  >
                    <div
                      id="case"
                      class="mx-3 mt-2"
                      @click="goPlaylist"
                    >
                      <img
                        :src="ImgUrl(content.contents.videos.thumbnail)"
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
                        style="font-size: 16px; font-weight: 600; text-align: left;"
                        class="txt-2"
                        @click="goPlaylist"
                      >
                        {{ content.contents.videos.title }}
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
      <div class="border-line" />
    </div>
    <div>
      <login-dialog
        :show="showLoginDialog"
        @on-click="showLoginDialog = false"
      />
    </div>
  </div>
</template>

<script>
import axiosConnector from '@/utils/axios-connector.js'

import PlayroomItemBig from '@/components/playroom/PlayroomItemBig.vue'
import LoginDialog from '@/components/common/LoginDialog.vue'
import { mapActions, mapState } from 'vuex'
import { getImage } from '@/utils/utils'
import { timeConverter } from '@/utils/utils';
import { playtimeConverter } from '@/utils/utils';


export default {
  name: 'MainItem',
  components: {
    LoginDialog,
    PlayroomItemBig,
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    content: {type: Object}
  },
  data: function() {
    return {
      thumbnailImage: '',
      userProfileImg: '',
      allTags: [],
      allBoardTags: [],
      time: [],
      time_playroom: [],

      showLoginDialog: false,

      playroomlistnum: '',

    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname', 'mainPlayrooms']),
    ...mapState('account', ['image']),
    ...mapState({
      isLogin: state => state.isLogin,
    }),
  },
  created: function() {
    console.log( '플레이리스트 상세', this.content.playlists )

    console.log('로그인 여부', this.isLogin)
    // console.log('content like', this.content.likesCnt)

    this.getThumbnailImage()
    this.getProfileImage()
    this.getTag()
    this.getPlayroomListNum()

  },
  methods: {
    ...mapActions(
      'playlist', [
        'likePlaylist',
        'unlikePlaylist',
        'getPlaylistComments',
      ]),
    ...mapActions(
      'playroom', [
        'likePlayroom',
        'unlikePlayroom',
      ]),
    ...mapActions(
      'board', [
        'likeBoard',
        'unlikeBoard'
      ]),
    // 태그
    // getTag: function() {
    //   if (this.content.tags) {
    //     // console.log('태그 있을 때', this.content.tags, typeof(this.content.tags))
    //     this.allTags = this.content.tags.split(',')
    //   }
    //   else {
    //     this.allTags = []
    //   }

    // },

    getTag: function() {
      if (this.content.type === 'board') {
        if (this.content.contents.tags) {
          this.allBoardTags = this.content.contents.tags.split(',')
        }
        else {
          this.allBoardTags = []
        }
      }
      else {

        if (this.content.tags) {
        // console.log('태그 있을 때', this.content.tags, typeof(this.content.tags))
          this.allTags = this.content.tags.split(',')
        }
        else {
          this.allTags = []
        }
      }
    },
    // 썸네일 이미지
    getThumbnailImage: function() {
      if (this.content.type === 'playlist'){
        this.thumbnailImage = this.content.videos.thumbnail
        // console.log('플레이리스트 섬네일', this.thumbnailImage)
      }
      else if (this.content.type === 'playroom'){
        this.thumbnailImage = this.content.videos.thumbnail
        // console.log('플레이룸 섬네일', this.thumbnailImage)
      }
      else if (this.content.type === 'board'){
        // this.thumbnailImage = this.content.videos[0].thumbnail
      }
    },
    // 프로필 이미지
    getProfileImage: function() {
      if (this.content.userProfileImg === null) {
        this.userProfileImg = 'src/assets/tupli_logo2_dark.png'
      }
      else {
        this.userProfileImg = this.content.userProfileImg
      }

      // this.content.userProfileImg === null? this.userProfileImg = '@/assets/tupli_logo2_dark.png' : this.userProfileImg = this.content.userProfileImg;
      // console.log('유저 프로필', this.userProfileImg)
    },
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 타 유저 프로필로 가기
    setProfile: function() {
      // 만약 클릭한 사람이 나라면
      console.log('나야 나', this.content)
      axiosConnector.get(`userinfo/${this.content.userId}`)
        .then((res) => {
          console.log('비로그인', res.data.meCheck)
          if (res.data.meCheck === false) {  // 내가 아니라면, 프로필로 !
            console.log( '타인 프로필')
            this.$router.push({ name: 'Profile', params: { userId : this.content.userId }})
          }
          else if (res.data.meCheck === true) {  // 나라면
            console.log( '내 프로필')
            this.$router.push({ name: 'MyProfile'})
          }
          else {  // 로그인
            this.$router.push({ name: 'Login'})
          }
        })
        .catch((err) => {
          console.log('에러', err)
        })
    },

    // 플레이룸 상세로 가기
    goPlayroom: function() {
      console.log( '플레이룸 상세', this.content.id )
      this.$router.push({ name: 'PlayroomDetail', params: { id: this.content.id }})
    },
    // 플레이리스트 상세로 가기
    goPlayroomPlaylist: function() {
      console.log( '플레이리스트 상세', this.content.playlists )
      this.$router.push({ name: 'PlaylistDetail', params: { playlistId : this.playroomlistnum }})
    },
    getPlayroomListNum: function() {
      for (let key in this.content.playlists) {
        this.playroomlistnum = key
      }
    },
    // 플레이리스트 상세로 가기
    goPlaylist: function() {
      console.log( '플레이리스트 상세', this.content.id )
      this.$router.push({ name: 'PlaylistDetail', params: { playlistId : this.content.id }})
    },
    // 플레이리스트 댓글로 가기
    onClickPlaylistComment: function () {
      this.getPlaylistComments(this.content.id)
      this.$router.push({ name: 'PlaylistComment', params: { playlistId: this.content.id }})
    },
    // 게시물 상세로 가기
    goBoard: function() {
      console.log( '게시물 상세', this.content.userId )
      this.$router.push({ name: 'BoardDetail', params: { boardId : this.content.id }})
    },
    // 플레이룸 생성
    addPlayroom: function() {
      console.log( '플레이룸 상세', this.content.id )
      this.$router.push({ name: 'PlayroomForm', params: this.content.id })
    },
    // 좋아요
    // 좋아요 - 플레이룸
    onClickPlayroomLike: function () {
      if (this.isLogin) {
        console.log('좋아요 누름', this.content.userLikesYN)
        this.content.userLikesYN = 'Y'
        this.content.likesCnt++
        this.likePlayroom(this.content.id)
      }
      else {
        console.log("showLoginDialog")
        this.showLoginDialog = true
      }
    },
    onClickPlayroomUnlike: function () {
      console.log('좋아요 취소', this.content.userLikesYN)
      this.content.userLikesYN = 'N'
      this.content.likesCnt--
      this.unlikePlayroom(this.content.id)
    },
    // 좋아요 - 플레이리스트
    onClickPlaylistLike: function () {
      if (this.isLogin) {
        console.log('좋아요 플리 누름', this.content.userLikesYN)
        this.content.userLikesYN = 'Y'
        this.content.likesCnt++
        this.likePlaylist(this.content.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickPlaylistUnlike: function () {
      console.log('좋아요 취소 플리 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'N'
      this.content.likesCnt--
      this.unlikePlaylist(this.content.id)
    },
    // 좋아요 - 게시물
    onClickBoardLike: function () {
      if (this.isLogin) {
        console.log('좋아요 게시글 누름', this.content.userLikesYN)
        this.content.userLikesYN = 'Y'
        this.content.likesCnt++
        this.likeBoard(this.content.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickBoardUnlike: function () {
      console.log('좋아요 취소 게시글 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'N'
      this.content.likesCnt--
      this.unlikeBoard(this.content.id)
    },

    // 작성 시간
    createdAt: function() {
      if (this.content.type === 'board'){
        this.time = timeConverter(this.content.createdAt)
      // console.log('시간 잘 나오나', this.time)
      }
      else if (this.content.type === 'playlist'){
        this.time = timeConverter(this.content.createdAt)
      // console.log('시간 잘 나오나', this.time)
      }
    },
    // 플레이룸 작성 시간
    createdAt_playroom: function() {
      if (this.content.type === 'playroom'){
        this.time_playroom = playtimeConverter(this.content.startTime, this.content.endTime)
      // console.log('시간 잘 나오나', this.time)
      }
    },

    // 로그인 페이지로 이동
    goLogin: function() {
      this.$router.push({ name: 'Login' })
    }
  },
}

</script>

<style >


</style>
