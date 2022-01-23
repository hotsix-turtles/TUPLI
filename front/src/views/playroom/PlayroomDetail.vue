<template>
  <div class="playroom">
    <div class="playerWrapper">
      <div class="player">

      </div>
    </div>
    <div class="playerNav">
      <div class="playroomLike"></div>
      <div class="playroomChat"></div>
      <div class="playroomShare"></div>
      <div class="playroomReport"></div>
    </div>
    <div class="playroomInfo">
      <div class="playroomTitleWrapper">
        <p v-bind:class="{ playroomPublicBadge: roomPublic, playroomPrivateBadge: !roomPublic }">{{ roomPublicLabel }}</p>
        <p class="playroomTitle">{{ roomTitle }}</p>
      </div>
      <div class="playroomAuthorWrapper">
        <div class="authorProfilePic">
          <img v-bind:src="roomAuthorProfilePic" />
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
    <div class="playlistWrapper">
      <div class="playlistThumbnailWrapper">
        <div class="thumbnailItems">

        </div>
      </div>
      <div class="playlistVideoWrapper">
        <div class="playlistVideoNav">

        </div>
        <div class="playlistVideoItems">
          
        </div>
        <div class="playlistNavWrapper">
          <div class="videoCounter">

          </div>
          <div class="navBtnsWrapper">
            <div class="playVideo">

            </div>
            <div class="showMyPlaylist">

            </div>
            <div class="saveVideo">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'PlayroomDetail',
  data() {
    return {
      showReducedContent: false
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
      'roomCurrentVideo'
    ]),
    ...mapGetters('playroom', [
      'roomPlayTime',
      'roomPublicLabel',
      'roomReducedContent'
    ]),
    roomContentReduced: () => {
      return this.roomContent == this.roomReducedContent
    }
  }
}
</script>

<style lang="scss">
@import '~@/assets/scss/common';

.playroomTitleWrapper {
  display: flex;
  flex-direction: row;
  padding: 10px;
  width: 100%;
  height: 100%;
}

.playroomPublicBadge {
  width: 50px;
  height: 20px;
  background: rgb(82, 72, 221);
  text-align: center;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
  margin-right: 10px;
}

.playroomPrivateBadge {
  display: inline-block;
  width: 50px;
  height: 20px;
  background: rgb(163, 160, 209);
  text-align: center;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  font-weight: bold;
  margin-right: 10px;
}

.playroomTitle {
  font-size: 14px;
  font-weight: bold;
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
  background: rgb(82, 72, 221);
  color: white;
  border-radius: 10px;
}
</style>