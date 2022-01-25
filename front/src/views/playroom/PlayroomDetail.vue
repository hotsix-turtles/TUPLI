<template>
  <div class="playroom">
    <div class="playerWrapper">
      <youtube :video-id="videoId" :player-vars="playerVars" 
        width="100%" height="200" ref="youtube" 
        @ready="onVideoReady" @ended="onVideoEnded" @playing="onVideoPlaying" 
        @paused="onVideoPaused" @buffering="onVideoBuffering" @cued="onVideoCued" />
    </div>
    <div class="playerNav">
      <v-bottom-navigation :value="value" grow class="elevation-2">
        <v-btn class="playroomLike">
          <span>좋아요</span>
          <v-icon>mdi-thumb-up</v-icon>
        </v-btn>

        <v-btn class="playroomChat">
          <span>채팅</span>
          <v-icon>mdi-message</v-icon>
        </v-btn>

        <v-btn class="playroomShare">
          <span>공유</span>
          <v-icon>mdi-share</v-icon>
        </v-btn>

        <v-btn class="playroomReport">
          <span>신고</span>
          <v-icon>mdi-alert</v-icon>
        </v-btn>
      </v-bottom-navigation>
    </div>
    <div class="playroomInfo">
      <div class="playroomTitleWrapper">
        <p v-bind:class="{ playroomPublicBadge: roomPublic, playroomPrivateBadge: !roomPublic }">{{ roomPublicLabel }}</p>
        <p class="playroomTitle">{{ roomTitle }}</p>
      </div>
      <div class="playroomAuthorWrapper">
        <div class="authorProfilePic">
          <v-avatar>
            <img v-bind:src="roomAuthorProfilePic" alt="John">
          </v-avatar>
        </div>
        <span class="authorName">{{ roomAuthorName }}</span>
        <div class="authorFollowerWrapper">
          <span class="authorFollower">{{ roomAuthorFollowerCount }}</span>
        </div>
      </div>
      <div class="playroomPlaytimeWrapper">
        <p class="playtime">{{ roomPlayTime }}</p>
      </div>
      <div class="playroomContentWrapper">
        <p v-if="!showReducedContent && roomReducedContent != roomContent" class="playroomReducedContent" v-on:click="showReducedContent = !showReducedContent">{{ roomReducedContent }}</p>
        <p v-else class="playroomContent">{{ roomContent }}</p>
      </div>
      <div class="playroomTagWrapper">
        <p v-for="roomTag in roomTags" v-bind:key="roomTag" class="playroomTagItem">{{ roomTag }}</p>
      </div>
    </div>
    <div class="playlistWrapper mx-3 mb-5">
      <p>현재 재생중인 <b>플레이리스트</b></p>
      <div class="playlistThumbnailWrapper">
        <v-card outlined style="overflow-x:auto">
          <v-list-item>
            <PlaylistThumbnailItem :thumb-url="playlistItem.thumbnail_url" v-for="playlistItem in playlistItems" v-bind:key="playlistItem.id"/>
          </v-list-item>
        </v-card>
      </div>
    </div>
    <div class="playlistVideoWrapper">
      <div class="playlistVideoNav mx-3">
        <div style="color:grey; font-size: 11px;">
          <v-icon class="mdi-18px">mdi-check</v-icon>
          <span class="ml-1">전체 선택</span>
        </div>
        <div>
          <v-icon>mdi-play-circle</v-icon>
        </div>
      </div>
      <div class="playlistVideoItems">
      </div>
      <div class="playlistNavWrapper">
        <v-card class="mx-auto overflow-hidden" height="300">
          <v-bottom-navigation absolute background-color="#5B5C9D" hide-on-scroll
            scroll-target="#scroll-threshold-example" scroll-threshold="500">
            <v-badge
              :content="1"
              color="#EAEAEA"
              offset-x=20
              offset-y=20
              overlap 
              class="videoCounter"
            >
            </v-badge>
            <v-btn>
              <span style="color: white;">영상보기</span>
              <v-icon color="white">mdi-play</v-icon>
            </v-btn>

            <v-btn>
              <span style="color: white;">내 플레이리스트</span>
              <v-icon color="white">mdi-disc</v-icon>
            </v-btn>

            <v-btn>
              <span style="color: white;">저장하기</span>
              <v-icon color="white">mdi-bookmark</v-icon>
            </v-btn>
          </v-bottom-navigation>
        </v-card>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import Vue from 'vue'
import VueYoutube from 'vue-youtube'
import PlaylistThumbnailItem from './PlaylistThumbnailItem.vue'

Vue.use(VueYoutube)

export default {
  name: 'PlayroomDetail',
  components: {
    PlaylistThumbnailItem
  },
  data() {
    return {
      showReducedContent: false,
      videoId: 'lG0Ys-2d4MA',
      playerVars: {
        autoplay: 1,
        mute: 1
      },
      playlistItems: [
        { id: 1, thumbnail_url: 'http://www.naver.com' },
        { id: 2, thumbnail_url: 'http://www.naver.com' },
        { id: 3, thumbnail_url: 'http://www.naver.com' },
      ]
    }
  },
  metaInfo () {
    return { 
      //title: this.roomTitle,
      titleTemplate: `${this.roomTitle} | Tupli`,
      htmlAttrs: {
        lang: 'ko-KR'
      },
      meta: [
        { charset: 'utf-8' },
        //{ name: 'description', content: ''},
        { name: 'viewport', content: 'width=device-width, initial-scale=1' }
      ]
    }
  },
  methods: {
    playVideo() {
      this.player.playVideo()
    },
    playing() {
      //const playTime = this.roomCurrentPlayTime.split(':')
      //this.player.seekTo(parseInt(playTime[0]) * 60 + parseInt(playTime[1]));
      const playTime = this.roomCurrentPlayTime.split(':').map(v => parseInt(v))
      this.player.seekTo(playTime[0] * 60 + playTime[1]);
    },
    syncSeek() {
      const playTime = this.roomCurrentPlayTime.split(':').map(v => parseInt(v))
      this.player.seekTo(playTime[0] * 60 + playTime[1]);
    },
    onVideoReady() {
      console.log('ready')
    },
    onVideoEnded() {
      console.log('ended')
    },
    onVideoPlaying() {
      console.log('playing')
    },
    onVideoPaused() {
      console.log('paused')
    },
    onVideoBuffering() {
      console.log('buffering')
    },
    onVideoCued() {
      console.log('cued')
    }
  },
  computed: {
    ...mapState('playroom', [
      'roomTitle',
      'roomPublic',
      'roomAuthorProfilePic',
      'roomAuthorName',
      'roomAuthorFollowerCount',
      'roomStartTime',
      'roomEndTime',
      'roomContent',
      'roomTags',
      'roomPlaylists',
      'roomCurrentPlaylist',
      'roomVideos',
      'roomCurrentVideo',
      'roomCurrentPlayTime'
    ]),
    ...mapGetters('playroom', [
      'roomPlayTime',
      'roomPublicLabel',
      'roomReducedContent'
    ]),
    player() {
      return this.$refs.youtube.player
    },
    roomContentReduced: () => {
      return this.roomContent == this.roomReducedContent
    }
  }
}
</script>

<style lang="scss">
@import '~@/scss/common';

iframe {
  width: 100%;
  max-width: 650px; /* Also helpful. Optional. */
}

.playroomInfo {
  padding: 10px;
}

.playroomTitleWrapper {
  display: flex;
  flex-direction: row;
  height: 20px;
}

.playroomPublicBadge {
  width: 50px;
  height: 20px;
  background: #5B5C9D;
  text-align: center;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
  margin-right: 5px;
}

.playroomPrivateBadge {
  display: inline-block;
  width: 50px;
  height: 20px;
  background: #a3a0d1;
  text-align: center;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
  margin-right: 5px;
}

.playroomTitle {
  font-size: 12px;
  font-weight: bold;
  word-spacing: -0.1em;
}

.playroomAuthorWrapper {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100%;
  padding: 5px;
  line-height: 45px;
}

.authorProfilePic {
  width: 40px;
  height: 40px;
  border: 1px #bbb solid;
  border-radius: 100%;
  margin: 5px;
}

.authorName {
  font-size: 16px;
  font-weight: bold;
  margin-right: 3px;
}

.authorFollower:before {
  content: '팔로워';
  margin-right: 3px;
}

.authorFollower {
  color: #bbb;
  font-size: 12px;
}

.playroomPlaytimeWrapper {
  padding: 5px;
}

.playtime:before {
  content: 'PLAY TIME';
  margin-right: 3px;
  font-weight: bold;
}

.playroomContentWrapper {
  padding: 5px;
}

.playroomContent {
  white-space: pre-line;
}

.playroomReducedContent {
  white-space: pre-line;
}

.playroomReducedContent:after {
  content: '더 보기';
  margin-left: 10px;
  color: #bbb;
  font-size: 14px;
}

.playroomTagWrapper {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.playroomTagItem {
  padding: 3px 10px;
  margin-left: 5px;
  background: #5B5C9D;
  color: white;
  border-radius: 10px;
  font-size: 8px;
}

.playlistVideoNav {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
</style>