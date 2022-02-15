<template>
  <div>
    <!-- 플레이룸 -->
    <div
      v-if="content.type == 'playroom'"
    >
      <!-- playroom 썸네일 -->
      <img
        :src="thumbnailImage"
        alt="썸네일"
        width="390"
        height="219"
        @click="goPlayroom"
      >
      <!-- playroom 정보 -->
      <div class="d-flex flex-column">
        <p v-if="content.isRecommend == true">
          {{ nickname }}님을 위한 추천 플레이룸
        </p>
        <div class="d-flex">
          <img
            style="border-radius: 100px; margin: 10px;"
            :src="ImgUrl(content.userProfileImg)"
            alt="프로필 사진"
            width="50"
            height="50"
            @click="setProfile"
          >
          <div
            class="d-flex flex-column align-start"
          >
            <h4
              class="mb-0 mt-2"
              @click="goPlayroom"
            >
              {{ content.videos.title }}
            </h4>
            <p
              class="mb-0"
              @click="setProfile"
            >
              {{ content.nickName }}
            </p>
            <div
              class="d-flex"
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
          <div class="d-flex mr-2 align-center">
            <div class="d-flex flex-column align-center mx-1">
              <!-- 좋아요 -->
              <div
                v-if="content.userLikesYN === 'Y'"
                @click="onClickPlayroomUnLike"
              >
                <v-icon color="#5B5C9D">
                  mdi-cards-heart
                </v-icon>
              </div>
              <div
                v-else-if="content.userLikesYN === 'N'"
                @click="onClickPlayroomLike"
              >
                <v-icon color="#5B5C9D">
                  mdi-cards-heart-outline
                </v-icon>
              </div>
              <div
                v-else
                @click="goLogin"
              >
                <v-icon color="#5B5C9D">
                  mdi-cards-heart-outline
                </v-icon>
              </div>
              <p>{{ content.likesCnt }}</p>
            </div>
            <div class="d-flex flex-column align-center mx-1">
              <v-icon>
                mdi-album
              </v-icon>
              <p>PLI</p>
            </div>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>

    <!-- 플레이리스트 -->
    <div
      v-else-if="content.type == 'playlist'"
      class="mt-5"
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
      <div class="d-flex flex-column">
        <p v-if="content.isRecommend == true">
          {{ nickname }}님을 위한 추천 플레이리스트
        </p>
        <!-- playlist 정보 -->
        <div class="d-flex">
          <img
            style="border-radius: 100px; margin: 10px;"
            :src="ImgUrl(content.userProfileImg)"
            alt="프로필 사진"
            width="50"
            height="50"
            @click="setProfile"
          >
          <div class="d-flex flex-column align-start">
            <h4
              class="mb-0 mt-2"
              @click="goPlaylist"
            >
              {{ content.title }}
            </h4>
            <p
              class="mb-0"
              @click="setProfile"
            >
              {{ content.nickName }}
            </p>
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
              <p>{{ content.likesCnt }}</p>
            </div>
            <div
              v-else-if="content.userLikesYN === 'N'"
              class="d-flex flex-column align-center mx-1"
              @click="onClickPlaylistLike"
            >
              <v-icon>
                mdi-cards-heart-outline
              </v-icon>
              <p>{{ content.likesCnt }}</p>
            </div>
            <div
              v-else-if="content.userLikesYN === null"
              class="d-flex flex-column align-center mx-1"
              @click="goLogin"
            >
              <v-icon>
                mdi-cards-heart-outline
              </v-icon>
              <p>{{ content.likesCnt }}</p>
            </div>
            <div
              class="d-flex flex-column align-center mx-1"
              @click="addPlayroom"
            >
              <v-icon>
                mdi-video-plus-outline
              </v-icon>
              <p>PRM</p>
            </div>
          </div>
        </div>
      </div>
      <div class="border-line" />
    </div>

    <!-- 게시글 -->
    <div
      v-else-if="content.type == 'board'"
      class="d-flex flex-column mt-5"
    >
      <!-- playlist 정보 -->
      <div class="d-flex">
        <div class="d-flex align-start">
          <img
            style="border-radius: 100px; margin: 10px;"
            :src="ImgUrl(image)"
            alt="프로필 사진"
            width="50"
            height="50"
            @click="setProfile"
          >
        </div>
        <div class="d-flex flex-column align-start">
          <p class="mb-0">
            {{ content.content }}
          </p>
          <p
            class="mb-0"
            @click="setProfile"
          >
            {{ content.nickName }}
          </p>
          <div
            v-if="content.userLikesYN === 'Y'"
            class="d-flex align-center"
            @click="onClickBoardUnlike"
          >
            <div class="d-flex align-center">
              <v-icon>
                mdi-cards-heart
              </v-icon>
              <p class="mx-2 mb-0">
                {{ content.likesCnt }}
              </p>
            </div>
          </div>
          <div
            v-if="content.userLikesYN === 'N'"
            class="d-flex align-center"
            @click="onClickBoardLike"
          >
            <div class="d-flex align-center">
              <v-icon>
                mdi-cards-heart-outline
              </v-icon>
              <p class="mx-2 mb-0">
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
import { mapActions, mapState } from 'vuex'
import { getImage } from '../../utils/utils'

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
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname']),
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
      console.log( '타인 프로필', this.content.userId )
      this.$router.push({ name: 'Profile', params: { userId : this.content.userId }})
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

    // 로그인 페이지로 이동
    goLogin: function() {
      this.$router.push({ name: 'Login' })
    }
  },
}

</script>

<style >


</style>
