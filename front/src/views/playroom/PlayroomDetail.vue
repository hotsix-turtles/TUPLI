<template>
  <v-card
    class="playroom mx-auto overflow-hidden"
    height="100vh"
    max-width="640"
  >
    <!-- 하단 네비게이션 (플레이리스트 조작) -->
    <v-bottom-navigation
      absolute
      background-color="#5B5C9D"
      height="65px"
      :input-value="selectedItem.length > 0"
    >
      <!-- 선택된 동영상 개수 뱃지 -->
      <v-badge
        :content="selectedItem.length"
        color="#EAEAEA"
        offset-x="20"
        offset-y="20"
        overlap
        class="videoCounter"
      />

      <!-- 영상보기 버튼 -->
      <NavButton
        color="white"
        content="영상보기"
        icon="mdi-play"
      />

      <!-- 내 플레이리스트 버튼 -->
      <NavButton
        color="white"
        content="내 플레이리스트"
        icon="mdi-disc"
      />

      <!-- 저장하기 버튼 -->
      <NavButton
        color="white"
        content="저장하기"
        icon="mdi-bookmark"
      />
    </v-bottom-navigation>

    <!-- 플레이룸 페이지 -->
    <v-sheet
      id="scroll-threshold-example"
      class="overflow-y-auto"
      :class="{ 'pb-16': selectedItem.length > 0 }"
      max-height="100%"
    >
      <!-- 유튜브 동영상 플레이어 Wrapper (필요없음) -->
      <div class="playerWrapper">
        <!-- 유튜브 동영상 플레이어 -->
        <youtube
          ref="youtube"
          :video-id="videoId"
          :player-vars="playerVars"
          width="100%"
          height="200"
          @ready="onVideoReady"
          @ended="onVideoEnded"
          @playing="onVideoPlaying"
          @paused="onVideoPaused"
          @buffering="onVideoBuffering"
          @cued="onVideoCued"
        />
      </div>
      <!-- 유튜브 동영상 플레이어 끝 -->

      <!-- 유튜브 동영상 플레이어 하단 네비게이션 -->
      <div class="playerNav">
        <v-bottom-navigation
          grow
          class="elevation-2"
        >
          <!-- 플레이룸 좋아요 -->
          <v-btn
            class="playroomLike"
            @click="playroomLike"
          >
            <span>좋아요</span>
            <v-icon :color="roomLiked ? 'blue' : undefined">
              mdi-thumb-up
            </v-icon>
          </v-btn>

          <!-- 플레이룸 댓글 -->
          <v-btn
            class="playroomChat"
            @click="isChatting = true"
          >
            <span>채팅</span>
            <v-icon>mdi-message</v-icon>
          </v-btn>

          <!-- 플레이룸 공유 -->
          <v-btn class="playroomShare">
            <span>공유</span>
            <v-icon>mdi-share</v-icon>
          </v-btn>

          <!-- 플레이룸 신고 -->
          <v-btn class="playroomReport">
            <span>신고</span>
            <v-icon>mdi-alert</v-icon>
          </v-btn>
        </v-bottom-navigation>
      </div>
      <!-- 유튜브 동영상 플레이어 하단 네비게이션(플레이룸 SNS 활동 네비게이션) 끝 -->

      <!-- 플레이룸 정보 Wrapper 시작 -->
      <div class="playroomInfo">
        <!-- 플레이룸 타이틀 Wrapper -->
        <div class="playroomTitleWrapper">
          <!-- 플레이룸 공개 여부 뱃지 -->
          <p :class="{ playroomPublicBadge: roomPublic, playroomPrivateBadge: !roomPublic }">
            {{ roomPublicLabel }}
          </p>
          <!-- 플레이룸 타이틀 -->
          <p class="playroomTitle">
            {{ roomTitle }}
          </p>
        </div>

        <!-- 플레이룸 작성자 Wrapper -->
        <div class="playroomAuthorWrapper">
          <!-- 플레이룸 작성자 프로필 사진 -->
          <div class="authorProfilePic">
            <v-img
              :src="roomAuthorProfilePic"
              alt="John"
              class="rounded-circle"
              style="width: 100%; height: auto;"
            />
          </div>

          <!-- 플레이룸 작성자 이름 -->
          <span class="authorName">{{ roomAuthorName }}</span>

          <!-- 플레이룸 팔로워 수 -->
          <div class="authorFollowerWrapper">
            <span class="authorFollower">{{ roomAuthorFollowerCount }}</span>
          </div>
        </div>

        <!-- 플레이룸 운영 시간(?) Wrapper -->
        <div class="playroomPlaytimeWrapper">
          <p class="playtime">
            {{ roomPlayTime }}
          </p>
        </div>

        <!-- 플레이룸 설명 Wrapper -->
        <div class="playroomContentWrapper">
          <!-- 플레이룸 요약 설명 -->
          <p
            v-if="!showReducedContent && roomReducedContent != roomContent"
            class="playroomReducedContent"
            @click="showReducedContent = !showReducedContent"
          >
            {{ roomReducedContent }}
          </p>

          <!-- 플레이룸 상세 설명 (더보기) -->
          <p
            v-else
            class="playroomContent"
          >
            {{ roomContent }}
          </p>
        </div>

        <!-- 플레이룸 태그 Wrapper -->
        <div class="playroomTagWrapper">
          <TagItem
            v-for="roomTag in roomTags"
            :key="roomTag"
            :content="roomTag"
          />
        </div>
      </div>
      <!-- 플레이룸 정보 Wrapper 끝 -->

      <!-- 플레이룸 플레이리스트 목록 Wrapper 시작 -->
      <div class="playlistWrapper mx-3 mb-5">
        <p>현재 재생중인 <b>플레이리스트</b></p>
        <!-- 플레이리스트 목록 -->
        <v-card
          outlined
          style="display:flex; flex-wrap: nowrap; overflow-x: auto"
          class="playlistThumbnailWrapper"
        >
          <PlaylistThumbnailItem
            v-for="(playlistItem, playlistIdx) in roomPlaylists"
            :id="playlistIdx"
            :key="playlistIdx"
            :src="playlistItem.thumbnailUrl"
            :selected="playlistIdx == roomCurrentPlaylistOffset"
          />
        </v-card>
      </div>
      <!-- 플레이룸 플레이리스트 목록 끝 -->

      <!-- 현재 플레이리스트 비디오 목록 Wrapper 시작 -->
      <div class="playlistVideoWrapper">
        <!-- 현재 플레이리스트 비디오 목록 상단 메뉴 -->
        <div class="playlistVideoNav d-flex justify-space-between align-center mx-3">
          <v-btn
            small
            elevation="0"
            color="white"
            @click="selectAllVideo"
          >
            <v-icon class="mdi-18px">
              mdi-check
            </v-icon>
            <span class="ml-1">전체 선택</span>
          </v-btn>
          <v-btn
            small
            elevation="0"
            color="white"
            fab
            @click="playThisVideo"
          >
            <v-icon>mdi-play-circle</v-icon>
          </v-btn>
        </div>

        <!-- 현재 플레이리스트 비디오 목록 -->
        <div
          v-if="roomPlaylists"
          class="playlistVideoItems d-flex flex-column overflow-y-auto"
        >
          <PlaylistVideoItem
            v-for="video in roomCurrentPlaylistVideos"
            :id="video.id"
            :key="video.id"
            :title="video.title"
            :thumbnail="video.thumbnailUrl"
            :playtime="video.playtime"
            :selected="isSelectedVideo(video.id)"
            :visible="video.included"
            @click="onPlaylistVideoSelected"
          />
        </div>
      </div>

      <!-- 플레이룸 채팅창 -->
      <v-dialog
        v-model="isChatting"
        fullscreen
        hide-overlay
        transition="dialog-bottom-transition"
        scrollable
      >
        <v-card tile>
          <v-card-title>
            <v-toolbar-title>채팅</v-toolbar-title>
            <v-btn
              icon
              class="ml-auto"
              @click="isChatting = false"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text>
            <v-container>
              <ChatItem
                v-for="chat in roomChats"
                :id="chat.id"
                :key="chat.id"
                :name="chat.author.name"
                :profile="chat.author.thumbnail"
                :content="chat.content"
                :timestamp="chat.timestamp"
                :blocked-user="chat.blockedUser"
                :blocked-message="chat.blockedMessage"
              />
            </v-container>
          </v-card-text>
          <v-spacer />
          <v-card-actions>
            <ChatInput />
          </v-card-actions>
          <div style="flex: 1 1 auto;" />
        </v-card>
      </v-dialog>
    </v-sheet>
  </v-card>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import Vue from 'vue'
import VueYoutube from 'vue-youtube'
import PlaylistThumbnailItem from './PlaylistThumbnailItem.vue'
import TagItem from './TagItem.vue'
import PlaylistVideoItem from './PlaylistVideoItem.vue'
import ChatItem from './ChatItem.vue'
import axiosConnector from '../../utils/axios-connector';
import wsConnector from '../../utils/ws-connector';
import ChatInput from './ChatInput.vue'
import NavButton from '../../components/common/NavButton.vue'

Vue.use(VueYoutube)

export default {
  name: 'PlayroomDetail',
  components: {
    PlaylistThumbnailItem,
    PlaylistVideoItem,
    ChatItem,
    ChatInput,
    NavButton,
    TagItem
  },
  data() {
    return {
      showReducedContent: false,
      videoId: '',
      playerVars: {
        mute: 1
      },
      selectedItem: [],
      isChatting: false,
      lastPlaytime: 0
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
      'roomId',
      'roomTitle',
      'roomPublic',
      'roomLiked',
      'roomAuthorProfilePic',
      'roomAuthorName',
      'roomAuthorFollowerCount',
      'roomStartTime',
      'roomEndTime',
      'roomContent',
      'roomTags',
      'roomPlaylists',
      'roomCurrentPlaylistOffset',
      'roomCurrentVideoOffset',
      'roomCurrentVideoPlaytime',
      'roomChats',
      'roomSendingMessage',
      'chatroomId',
      'chatBlockedId',
      'chatBlockedUid',
    ]),
    ...mapGetters('playroom', [
      'roomPlayTime',
      'roomPublicLabel',
      'roomReducedContent',
      'roomCurrentPlaylistVideos'
    ]),
    player() {
      return this.$refs.youtube.player
    },
    roomContentReduced() {
      return this.roomContent == this.roomReducedContent
    }
  },
  created() {
    this.getRoomInfo()
  },
  mounted() {
    this.$watch('roomCurrentPlaylistVideos', (newVal, oldVal) =>
    {
      this.updateVideoId()
    });
    this.$watch('roomCurrentPlaylistOffset', (newVal, oldVal) => {
      this.updateVideoId()
    });
    this.$watch('roomCurrentVideoOffset', (newVal, oldVal) => {
      this.updateVideoId()
    });
    this.$watch('roomCurrentVideoPlaytime', (newVal, oldVal) => {
      if (Math.abs(newVal - oldVal) < 0.5) return;
      this.seekTo()
    });

  },
  methods: {
    getRoomInfo() {
      axiosConnector.post('/echo', {
        id: 1,
        title: '3일만에 다이어트 포기 선언하게 만든 영상들',
        isPublic: false,
        isLiked: false,
        authorProfilePic: 'https://picsum.photos/100/100',
        authorName: '춘식이',
        authorFollowerCount: 456,
        startTime: new Date(2022, 2, 5, 18, 30),
        endTime: new Date(2022, 2, 5, 20, 30),
        content: '같이 치맥하면서 먹방 보실분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n',
        tags: ['먹방', '쯔양', '고기먹방' ],
        currentPlaylistOffset: 1,
        playlists: [
          {
            title: '다이어트 안해',
            thumbnailUrl: 'https://picsum.photos/90/90',
            videos: [
              { id: 1, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/161/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 2, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/162/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 3, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/163/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 4, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/164/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 5, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/165/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 6, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/166/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 7, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/167/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 8, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/168/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
              { id: 9, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/169/90', title: "먹물파스타 먹방", playtime: '01:30', included: true },
            ]
          },
          {
            title: '다이어트 안해 2',
            thumbnailUrl: 'https://picsum.photos/90/90',
            videos: [
              { id: 1, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/161/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 2, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/162/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 3, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/163/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 4, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/164/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 5, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/165/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 6, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/166/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 7, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/167/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 8, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/168/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
              { id: 9, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/169/90', title: "해물파스타 먹방", playtime: '01:30', included: true },
            ]
          },
          {
            title: '다이어트 안해 3',
            thumbnailUrl: 'https://picsum.photos/90/90',
            videos: [
              { id: 1, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/161/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 2, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/162/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 3, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/163/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 4, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/164/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 5, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/165/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 6, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/166/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 7, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/167/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 8, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/168/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
              { id: 9, videoId: 'lG0Ys-2d4MA', thumbnailUrl: 'https://picsum.photos/169/90', title: "보물파스타 먹방", playtime: '01:30', included: true },
            ]
          },
        ],
        currentVideoOffset: 0,
        currentVideoPlaytime: 300,
        chatroomId: '731f3b99-8257-4eae-86b2-ed38ea36ccff'
      }).then(response => {
        this.$store.dispatch('playroom/setRoomInfo', response).then(
          () => {
            this.initChatRoom()
          }
        )
      });
      // axiosConnector.get(`/playroom/${this.$route.params.id}`).then(response => {
      //   this.$store.dispatch('setRoomInfo', response)
      // });
    },
    initChatRoom() {
      const token = localStorage.getItem('jwt')
      wsConnector.connect(
        token ? { Authorization: this.token } : { },
        () => wsConnector.subscribe(`/sub/chat/room/${this.chatroomId}`, this.onReceiveMessage, token ? { Authorization: token } : undefined),
        () => alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.")
      )
    },
    // syncSeek() {
    //   const playTime = parseFloat(this.roomCurrentVideoPlaytime)
    //   console.log(playTime)
    //   this.player.seekTo(playTime);
    // },
    onPlaylistVideoSelected({ id, selected }) {
      if (selected)
      {
        const idx = this.selectedItem.findIndex(el => el == id)
        this.selectedItem.splice(idx, 1)
      }
      else
      {
        this.selectedItem.push(id)
      }
    },
    onVideoReady() {

    },
    onVideoEnded() {
      console.log('ended')
      this.loadNextVideo()
    },
    async onVideoPlaying() {
      const currentPlaytime = await this.player.getCurrentTime()
      console.log('sync event', {
        state: 'play',
        timestamp: new Date().getTime(),
        playlistId: this.roomCurrentPlaylistOffset,
        videoOffset: this.roomCurrentVideoOffset,
        videoPlaytime: currentPlaytime
      })
      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(currentPlaytime)
    },
    async onVideoPaused() {
      const currentPlaytime = await this.player.getCurrentTime()
      console.log('sync event', {
        state: 'pause',
        timestamp: new Date().getTime(),
        playlistId: this.roomCurrentPlaylistOffset,
        videoOffset: this.roomCurrentVideoOffset,
        videoPlaytime: currentPlaytime
      })
      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(currentPlaytime)
    },
    onVideoBuffering() {

    },
    onVideoCued() {
      this.seekTo()
    },
    async onReceiveMessage(payload) {
      const id = payload.headers['message-id']
      const body = JSON.parse(payload.body);

      if (body.type == 'SYNC')
      {
        this.$store.commit('playroom/SEEK_VIDEO', parseFloat(body.message));
      }
      else// if (body.type == 'TALK')
      {
        const profile = await axiosConnector.post('/echo', {
          nickname: '시스템',
          profilePictureUrl: 'https://picsum.photos/80/80'
        })
        //const profile = await axiosConnector.get(`/profile/${body.id}`)
        const author = { id: body.id, name: profile.data.nickname, thumbnail: profile.data.profilePictureUrl };
        const content = body.message;
        const timestamp = new Date().getTime();
        const blockedUser = ( this.chatBlockedUid.find((v) => v == author.id) != undefined );
        const blockedMessage = ( this.chatBlockedId.find((v) => v == id) != undefined );
        this.$store.commit('playroom/RECEIVE_MESSAGE', { id, author, content, timestamp, blockedUser, blockedMessage });
      }
    },
    selectAllVideo() {
      this.selectedItem = (this.selectedItem.length == this.roomCurrentPlaylistVideos.length) ?
        [] :
        this.roomCurrentPlaylistVideos.map(v => parseInt(v.id));
    },
    isSelectedVideo(id) {
      return this.selectedItem.findIndex(el => el == id) > -1
    },
    loadNextVideo() {
      if (this.roomCurrentPlaylistVideos.filter(v => v.included).length < this.roomCurrentVideoOffset + 1)
      {
        if (Object.keys(this.roomPlaylists).length <= this.roomCurrentPlaylistOffset + 1)
          this.SET_ROOM_CURRENT_PLAYLIST_OFFSET(0)
        else
          this.SET_ROOM_CURRENT_PLAYLIST_OFFSET(this.roomCurrentPlaylistOffset + 1)
        this.SET_ROOM_CURRENT_VIDEO_OFFSET(0)
        this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      }
      else
      {
        this.SET_ROOM_CURRENT_VIDEO_OFFSET(this.roomCurrentVideoOffset + 1)
        this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      }
    },
    updateVideoId() {
      this.videoId = this.roomCurrentPlaylistVideos.filter(v => v.included).map(v => v.videoId)[this.roomCurrentVideoOffset];
    },
    playVideo() {
      this.player.playVideo()
    },
    seekTo() {
      this.player.seekTo(this.roomCurrentVideoPlaytime)
    },
    playThisVideo() {
      if (this.selectedItem.length != 1) {
        alert('바로 재생할 영상을 1개만 선택해주세요')
        return;
      }
      this.SET_ROOM_CURRENT_VIDEO_OFFSET(this.selectedItem[0])
      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      this.selectedItem = []
      this.seekTo()
    },
    playroomLike() {
      this.SET_ROOOM_LIKED(!this.roomLiked)
      axiosConnector.post(this.roomLiked ? '/playroom/like' : '/playroom/dislike', JSON.stringify({ id: this.roomId }));
    },
    ...mapMutations('playroom', ['SET_ROOOM_LIKED', 'SET_ROOM_CURRENT_PLAYLIST_OFFSET', 'SET_ROOM_CURRENT_VIDEO_OFFSET', 'SET_ROOM_CURRENT_VIDEO_PLAYTIME'])
  }
}
</script>

<style lang="scss">

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

.playlistVideoNav {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.videoCounter {
  color: black;
}
</style>
