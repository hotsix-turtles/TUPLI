<template>
  <div>
    <!-- 플레이룸 -->
    <div
      v-if="content.type == 'playroom'"
      class="d-flex flex-column align-center"
    >
      <div
        class="video-thumbnail"
        style="width: 100%;"
      >
        <!-- playroom 썸네일 -->
        <img
          :src="thumbnailImage"
          alt="썸네일"
          width="100%"
          height="auto"
          @click="goPlayroom"
        >
        <div
          v-if="mainPlayrooms"
          class="d-flex align-center"
        >
          <div class="d-flex align-center duration-main">
            <v-icon
              size="16"
              color="#f1f1f5"
            >
              mdi-account-multiple
            </v-icon>
            <p
              style="margin-left:2px;"
            >
              {{ content.userCount }}
            </p>
          </div>
          <div
            class="d-flex align-center on-play mt-1"
          >
            <v-icon
              size="16"
              color="#f1f1f5"
            >
              mdi-play
            </v-icon>
            <p
              style="margin-left:2px; padding-right:4px;"
            >
              PLAY
            </p>
          </div>
        </div>
      </div>
      <!-- playroom 정보 -->
      <div
        class="d-flex flex-column justify-center mt-1"
        style="width:96%;"
      >
        <div class="d-flex">
          <img
            class="profile-img-main"
            :src="ImgUrl(content.userProfileImg)"
            alt="프로필 사진"
            @click="setProfile"
          >
          <div
            class="d-flex flex-column align-start"
            style="width:67%"
          >
            <!-- 추천 -->
            <p
              v-if="content.isRecommend == true"
              class="main-recommend-icon mt-2"
            >
              추천
            </p>
            <p
              class="main-title txt-2"
              @click="goPlayroom"
            >
              {{ content.title }}
            </p>
            <p
              class="mb-0 main-username"
              @click="setProfile"
            >
              {{ content.nickName }}
            </p>
            <div
              class="d-flex flex-wrap"
            >
              <p
                v-for="tag in allTags
                "
                :key="tag.id"
                class="mb-0 main-tag"
              >
                {{ tag }}
              </p>
            </div>
          </div>
          <!-- 리액션 아이콘 -->
          <div class="d-flex mr-2 align-start mt-4">
            <!-- 좋아요 -->
            <div
              v-if="content.userLikesYN === 'Y'"
              class="d-flex flex-column align-center mx-1"
              @click="onClickPlayroomUnlike"
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
              @click="onClickPlayroomLike"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
            <div
              v-else
              class="d-flex flex-column align-center mx-1"
              @click="goLogin"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>

            <div
              class="d-flex flex-column align-center mx-1"
            >
              <v-icon
                color="#000000"
                style="padding-top: 1px;"
              >
                mdi-album
              </v-icon>
              <p
                class="main-icon-text"
                style="padding-left: 1px;"
              >
                PLI
              </p>
            </div>
          </div>
        </div>
      </div>
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
        style="width:96%;"
      >
        <!-- playlist 정보 -->
        <div class="d-flex">
          <img
            class="profile-img-main"
            :src="ImgUrl(content.userProfileImg)"
            alt="프로필 사진"
            @click="setProfile"
          >
          <div
            class="d-flex flex-column align-start"
            style="width:67%"
          >
            <p
              v-if="content.isRecommend == true"
              class="main-recommend-icon mt-2"
            >
              추천
            </p>
            <p
              class="main-title-playlist twolines"
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

          <!-- 좋아요 -->
          <div class="d-flex mr-2 align-center">
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
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
            <div
              v-else-if="content.userLikesYN === null"
              class="d-flex flex-column align-center mx-1"
              @click="goLogin"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
            <div
              class="mx-1"
              @click="$router.push({ name: 'BoardComment', params: { boardId: content.id }})"
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
      class="d-flex flex-column mt-1 align-center"
    >
      <div
        class="d-flex mb-2"
        style="width:96%;"
      >
        <div
          class="d-flex align-start"
          width="95%"
        >
          <div class="d-flex">
            <img
              style="border-radius: 100px; margin: 10px;"
              :src="ImgUrl(image)"
              class="profile-img-main"
              alt="프로필 사진"
              @click="setProfile"
            >
          </div>
          <div
            class="d-flex flex-column align-start"
            style="margin-top: 13px;"
            width="100%"
          >
            <p
              class="main-username"
              @click="setProfile"
            >
              {{ content.nickName }}
            </p>
            <p
              class="main-content txt-3"
              style="margin-top: 13px;"

              @click="goBoard"
            >
              {{ content.content }}
            </p>
          </div>
          <!-- 리액션 아이콘 -->
          <div class="d-flex mr-2 align-start mt-4 pt-2">
            <!-- 좋아요 -->
            <div
              v-if="content.userLikesYN === 'Y'"
              class="d-flex flex-column align-center mx-1"
              @click="onClickBoardUnlike"
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
              @click="onClickBoardLike"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
            <div
              v-else
              class="d-flex flex-column align-center mx-1"
              @click="goLogin"
            >
              <v-icon color="#000000">
                mdi-cards-heart-outline
              </v-icon>
              <p class="main-icon-text">
                {{ content.likesCnt }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>
  </div>
</template>

<script>
import axiosConnector from '@/utils/axios-connector.js'

import { mapActions, mapState } from 'vuex'
import { getImage } from '@/utils/utils'
import { timeConverter } from '@/utils/utils';
import { playtimeConverter } from '@/utils/utils';


export default {
  name: 'MainItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    content: {type: Object}
  },
  data: function() {
    return {
      thumbnailImage: '',
      userProfileImg: '',
      allTags: [],
      time: [],
      time_playroom: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname', 'mainPlayrooms']),
    ...mapState('account', ['image'])
  },
  created: function() {
    // console.log('content', this.content)
    // console.log('content like', this.content.likesCnt)

    this.getThumbnailImage()
    this.getProfileImage()
    this.getTag()

  },
  methods: {
    ...mapActions(
      'playlist', [
        'likePlaylist',
        'unlikePlaylist',
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
    getTag: function() {
      if (this.content.tags) {
        // console.log('태그 있을 때', this.content.tags, typeof(this.content.tags))
        this.allTags = this.content.tags.split(',')
      }
      else {
        this.allTags = []
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
    goPlaylist: function() {
      console.log( '플레이리스트 상세', this.content.id )
      this.$router.push({ name: 'PlaylistDetail', params: { playlistId : this.content.id }})
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
      console.log('좋아요 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'Y'
      this.content.likesCnt++
      this.likePlayroom(this.content.id)
    },
    onClickPlayroomUnlike: function () {
      console.log('좋아요 취소', this.content.userLikesYN)
      this.content.userLikesYN = 'N'
      this.content.likesCnt--
      this.unlikePlayroom(this.content.id)
    },
    // 좋아요 - 플레이리스트
    onClickPlaylistLike: function () {
      console.log('좋아요 플리 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'Y'
      this.content.likesCnt++
      this.likePlaylist(this.content.id)
    },
    onClickPlaylistUnlike: function () {
      console.log('좋아요 취소 플리 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'N'
      this.content.likesCnt--
      this.unlikePlaylist(this.content.id)
    },
    // 좋아요 - 게시물
    onClickBoardLike: function () {
      console.log('좋아요 게시글 누름', this.content.userLikesYN)
      this.content.userLikesYN = 'Y'
      this.content.likesCnt++
      this.likeBoard(this.content.id)
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
