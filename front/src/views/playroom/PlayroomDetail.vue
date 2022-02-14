<template>
  <v-card
    class="mx-auto mb-10 overflow-hidden"
    height="100%"
  >
    <!-- 하단 네비게이션 (플레이리스트 조작) -->
    <v-bottom-navigation
      absolute
      class="fixed-bottom"
      background-color="#5B5C9D"
      height="60px"
      :input-value="selectedVideoItem.length > 0"
    >
      <!-- 선택된 동영상 개수 뱃지 -->
      <v-badge
        :content="selectedVideoItem.length"
        color="#EAEAEA"
        overlap
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
      class="overflow-y-auto playroom-sheet"
    >
      <!-- 유튜브 동영상 플레이어 Wrapper (필요없음) -->
      <div class="playerWrapper">
        <!-- 유튜브 동영상 플레이어 -->
        <youtube
          ref="youtube"
          :video-id="videoId"
          :player-vars="playerVars"
          width="100%"
          height="200vh"
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

          <!-- 플레이룸 반복 -->
          <v-btn
            v-if="roomAuthorId == userInfo.userSeq"
            class="playroomReport"
            @click="playroomRepeat"
          >
            <span>반복</span>
            <v-icon :color="roomRepeat ? 'blue' : undefined">mdi-repeat</v-icon>
          </v-btn>

          <!-- 플레이룸 신고 -->
          <v-btn
            v-else
            class="playroomReport"
          >
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
            <img
              :src="roomAuthorProfilePic"
              alt=""
              class="rounded-circle"
              style="width: 100%; height: auto;"
              @click="$router.push(`/profile/${roomAuthorId}`)"
            >
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
            {{ roomPlaytime }}
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
          <!-- <tags
            v-for="roomTag in roomTags"
            :key="roomTag"
            :content="roomTag"
          /> -->
          <tags :tags="roomTags ? roomTags.split(',') : []" />
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
            v-for="(videos, playlistId, playlistIdx) in roomPlaylists"
            :id="playlistId"
            :key="playlistId"
            :src="playlistThumbnails[playlistIdx]"
            :selected="playlistId == roomCurrentPlaylistId"
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
          <!-- <v-btn
            small
            elevation="0"
            color="white"
            fab
            @click="playThisVideo"
          >
            <v-icon>mdi-play-circle</v-icon>
          </v-btn> -->
        </div>

        <!-- 현재 플레이리스트 비디오 목록 -->
        <div
          v-if="roomPlaylists"
          class="playlistVideoItems d-flex flex-column overflow-y-auto"
        >
          <PlaylistVideoItem
            v-for="video in roomCurrentPlaylistVideos"
            :key="video.id"
            :video="video"
            :selected="isSelectedVideo(video.id)"
            :visible="video.included"
            @click="onPlaylistVideoSelected"
          />
        </div>
      </div>

      <!-- 플레이룸 채팅창 -->
      <v-dialog
        v-model="isChatting"
        content-class="chat-dialog"
        hide-overlay
        transition="dialog-bottom-transition"
        scrollable
      >
        <v-card
          height="d-flex flex-column"
        >
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
          <v-card-text
            ref="chat_messages"
            class="my-1"
          >
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
              @kick-user="sendKick"
            />
          </v-card-text>
          <v-spacer />
          <v-card-actions
            class="mx-1 mb-0 align-item-bottom"
          >
            <!-- 채팅 입력창 -->
            <v-row>
              <v-text-field
                ref="chat_input"
                v-model="message"
                label="메시지를 입력하세요"
                solo
                dense
                :disabled="!canChat"
                :error="errorOnSend"
                @keydown.enter="sendChat"
                @click:append-outer="sendMessage"
              >
                <template v-slot:append>
                  <v-menu
                    v-model="showEmoji"
                    rounded="lg"
                    top
                    left
                    offset-x
                    offset-y
                  >
                    <template v-slot:activator="{ on, attrs }">
                      <v-icon
                        v-if="showEmoji"
                        v-bind="attrs"
                        v-on="on"
                        @click="showEmoji = !showEmoji"
                      >
                        mdi-emoticon
                      </v-icon>
                      <v-icon
                        v-else
                        v-bind="attrs"
                        v-on="on"
                        @click="showEmoji = !showEmoji"
                      >
                        mdi-emoticon-outline
                      </v-icon>
                    </template>
                    <v-card>
                      <v-list>
                        <v-list-item>
                          이모지
                        </v-list-item>
                      </v-list>
                    </v-card>
                  </v-menu>
                </template>
                <template v-slot:append-outer>
                  <!-- <v-fade-transition leave-absolute> -->
                  <v-progress-circular
                    v-if="sending"
                    size="24"
                    indeterminate
                  />
                  <v-icon
                    v-else
                    @click="sendChat"
                  >
                    mdi-send
                  </v-icon>
                  <!-- </v-fade-transition> -->
                </template>
              </v-text-field>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!--
        방장 전환시 팝업
      -->
      <v-dialog
        v-model="isAuthorChangedInfo"
        persistent
        width="300"
      >
        <v-card
          color="#5B5C9D"
          dark
          height="100%"
          class="py-1"
        >
          <v-card-text>
            방장 전환 중...
            <v-progress-linear
              indeterminate
              color="white"
              class="mb-0"
            />
          </v-card-text>
        </v-card>
      </v-dialog>

      <!--
        미운영중 접속시 팝업
        하영님 팝업으로 교체 예정
      -->
      <normal-dialog
        title="오류"
        content-html="현재 운영중이 아닌 플레이룸입니다."
        max-width="290"
        :show="isOperationTimeError"
        :buttons="[{name: '확인'}]"
        button-spacing
        persistent
        @button-click="errorPromptHandler"
      />
      <normal-dialog
        title="오류"
        content-html="비공개 플레이룸입니다."
        max-width="290"
        :show="isNotInvitedError"
        :buttons="[{name: '확인'}]"
        button-spacing
        persistent
        @button-click="errorPromptHandler"
      />
      <normal-dialog
        title="오류"
        content-html="방장에게 강퇴당했습니다."
        max-width="290"
        :show="isKickedError"
        :buttons="[{name: '확인'}]"
        button-spacing
        persistent
        @button-click="errorPromptHandler"
      />
      <normal-dialog
        content-html="플레이룸을 종료할까요?"
        max-width="290"
        :show="exitPrompt"
        :buttons="[{name: '나가기'}, {name: '취소'},]"
        button-spacing
        persistent
        @button-click="exitPromptHandler"
      />
    </v-sheet>
  </v-card>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import Vue from 'vue'
import VueYoutube from 'vue-youtube'
import PlaylistThumbnailItem from './PlaylistThumbnailItem.vue'
import PlaylistVideoItem from './PlaylistVideoItem.vue'
import ChatItem from './ChatItem.vue'
import axiosConnector from '../../utils/axios-connector';
// import wsConnector from '../../utils/ws-connector';
import NavButton from '../../components/common/NavButton.vue'
import Stomp from "webstomp-client"
import SockJS from "sockjs-client"
import Tags from '../../components/common/Tags.vue';
import NormalDialog from '../../components/common/NormalDialog.vue';

Vue.use(VueYoutube)

export default {
  name: 'PlayroomDetail',
  components: {
    PlaylistThumbnailItem,
    PlaylistVideoItem,
    ChatItem,
    NavButton,
    Tags,
    NormalDialog,
  },
  data() {
    return {
      showReducedContent: false,
      videoId: '',
      playerVars: {
        mute: 1
      },
      selectedVideoItem: [],
      isChatting: false,
      lastPlaytime: 0,
      wsConnector: null,
      showEmoji: false,
      sending: false,
      message: '',
      canChat: true,
      errorOnSend: false,
      playlistThumbnails: [],
      userInfo: {
        userSeq: null
      },
      heartbeat: 0,
      isOperationTimeError: false,
      isNotInvitedError: false,
      isAuthorChangedInfo: false,
      isKickedError: false,
      exitPrompt: false,
      roomPlaytime: null,
      certification: false,
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
      'roomRepeat',
      'roomAuthorId',
      'roomAuthorProfilePic',
      'roomAuthorName',
      'roomAuthorFollowerCount',
      'roomStartTime',
      'roomEndTime',
      'roomUserStartTime',
      'roomUserEndTime',
      'roomInviteIds',
      'roomContent',
      'roomTags',
      'roomPlaylists',
      'roomVideos',
      'roomCurrentPlaylistId',
      'roomCurrentVideoId',
      'roomCurrentVideoPlaytime',
      'roomChats',
      'roomSendingMessage',
      'chatroomId',
      'chatBlockedId',
      'chatBlockedUid',
    ]),
    ...mapGetters('playroom', [
      'roomPublicLabel',
      'roomReducedContent',
      'roomCurrentPlaylistVideos',
      'roomFirstVideo',
      'roomPrevVideo',
      'roomNextVideo'
    ]),
    roomContentReduced() {
      return this.roomContent == this.roomReducedContent
    }
  },
  async created() {
    const isValid = await this.validateToken();
    if (!isValid)
    {
      // 토큰 만료시 현재 vuex 정보를 초기화하고 로그인 페이지로 이동
      localStorage.clear();
      this.$router.push('/login')
    }
  },
  mounted() {
    this.$nextTick(async () => {
      await this.RESET_VUEX_DATA();

      this.player = this.$refs.youtube.player;
      await this.getRoomInfo();
    });

    this.$watch('roomCurrentPlaylistVideos', (newVal, oldVal) =>
    {
      this.playlistThumbnails = Object.keys(this.roomPlaylists).reduce((prevPlaylistIds, curPlaylistId) => {
        if (this.roomPlaylists[curPlaylistId])
          prevPlaylistIds.push(this.roomVideos.find(roomVideo => roomVideo.id == this.roomPlaylists[curPlaylistId][0]).thumbnail);

        return prevPlaylistIds;
      }, []);
    });
    this.$watch('roomCurrentPlaylistId', (newVal, oldVal) => {
      //this.updateVideoId()
    });
    this.$watch('roomCurrentVideoId', (newVal, oldVal) => {
      this.updateVideoId()
    });
    this.$watch('roomCurrentVideoPlaytime', async (newVal, oldVal) => {
      if (newVal - oldVal < 2 && Math.abs(newVal - await this.player.getCurrentTime()) < 1) return;
      if (this.userInfo.userSeq == this.roomAuthorId) return;
      if (document.hidden) return;
      this.seekTo()
    });

    this.$watch('isChatting', (newVal, oldVal) => {
      if (newVal)
      {
        this.$nextTick(() => {
          let messages = this.$refs.chat_messages;
          if (!messages) return;
          messages.scrollTo({ top: messages.scrollHeight, behavior: 'smooth' });
        });
      }
      // TODO: 채팅방에서 모바일 뒤로가기버튼 누르면 채팅방만 꺼지게 하고 싶었지만... 실-패
      // if (newVal && !oldVal) {
      //   document.addEventListener("backbutton", this.closeChatting, false);
      //   window.addEventListener("popstate", this.closeChatting, false);
      // }
      // if (!newVal && oldVal) {
      //   document.removeEventListener("backbutton", this.closeChatting);
      //   window.removeEventListener("popstate", this.closeChatting);
      // }
    })

    this.$watch('roomStartTime', (newVal, oldVal) => {
      this.loadRoomPlaytime();
    });

    this.$watch('roomEndTime', (newVal, oldVal) => {
      this.loadRoomPlaytime();
    });

    this.$watch('roomChats', (newVal, oldVal) => {
      this.$nextTick(() => {
        let messages = this.$refs.chat_messages;
        if (!messages) return;
        messages.scrollTo({ top: messages.scrollHeight, behavior: 'smooth' });
      });
    });
  },
  beforeDestroy() {
    if (this.wsConnector) this.releaseChatroom();
  },
  async beforeRouteLeave(to, from, next) {
    if (this.isChatting)
    {
      this.isChatting = false;
      next(false);
      return;
    }

    if (this.certification)
    {
      if (from.name == to.name) await this.releaseChatroom();
      next();
      return;
    } else {
      this.exitPrompt = true;
      next(false);
      return;
    }
  },
  methods: {
    errorPromptHandler() {
      this.certification = true;
      this.$router.go(-1)
    },
    exitPromptHandler(idx) {
      if (idx == 0)
      {
        this.certification = true;
        this.$router.go(-1);
      }
      this.exitPrompt = false;
    },
    loadRoomPlaytime() {
      const roomStartTime = this.roomStartTime
      const roomEndTime = this.roomEndTime
      const timezoneOffset = new Date().getTimezoneOffset() * 60 * 1000;

      const roomStartDate = new Date(roomStartTime - timezoneOffset);
      const roomEndDate = new Date(roomEndTime - timezoneOffset);

      if (roomStartDate.getDate() == roomEndDate.getDate())
        this.roomPlaytime = `${roomStartDate.toISOString().substr(11, 5)} - ${roomEndDate.toISOString().substr(11, 5)}`
      else if (roomStartDate.getMonth() == roomEndDate.getMonth())
        this.roomPlaytime = `${roomStartDate.getDate()}일 ${roomStartDate.toISOString().substr(11, 5)} - ${roomEndDate.getDate()}일 ${roomEndDate.toISOString().substr(11, 5)}`
      else if (roomStartDate.getFullYear() == roomEndDate.getFullYear())
        this.roomPlaytime = `${roomStartDate.getMonth()}월 ${roomStartDate.getDate()}일 ${roomStartDate.toISOString().substr(11, 5)} - ${roomEndDate.getMonth()}월 ${roomEndDate.getDate()}일 ${roomEndDate.toISOString().substr(11, 5)}`

      this.roomPlaytime = this.roomPlaytime ? this.roomPlaytime : null;
    },
    closeChatting() {
      this.isChatting = false;
    },
    async getRoomInfo() {
      // const token = localStorage.getItem('jwt')

      const roomInfo = await axiosConnector.get(`/playroom/${this.$route.params.id}`);
      await this.$store.dispatch('playroom/setRoomInfo', roomInfo);

      const userFollowerInfo = await axiosConnector.get(`/profile/followers/${this.roomAuthorId}/count`);
      this.SET_ROOM_AUTHOR({ follower: parseInt(userFollowerInfo.data) })

      // TODO: user profile 부분이 미완성이라 임시로 접속할때 얻어옴. 추후 삭제 필요
      if (this.$store.state.isLogin)
      {
        var userInfo = await axiosConnector.get(`/account/userInfo`);
        this.userInfo = userInfo.data;
      }

      await this.checkPermission();
      await this.loadFirstVideo();
      await this.loadLikeState();
      await this.loadRoomPlaytime();
      await this.initChatRoom();
      clearInterval(this.sendSync);
      clearInterval(this.checkHeartbeat)
      setInterval(this.sendSync, 1000);
      setInterval(this.checkHeartbeat, 1000);
    },
    checkPermission() {
      // 방 운영시간 외이면
      if (this.roomStartTime >= Date.now() || this.roomEndTime <= Date.now())
        this.showErrorOperationTime();

      // 비공개방이고 미초대 유저면
      if (!this.roomPublic && !this.roomInviteIds.find(inviteId => inviteId == this.userInfo.userSeq)) {
        this.showErrorNotInvited();
      }
    },
    showErrorOperationTime() {
      if (this.isOperationTimeError) return;
      this.isOperationTimeError = true;
      // setTimeout(() => {
      //   this.isOperationTimeError = false;
      //   this.$router.go(-1);
      // }, 3000)
    },
    showErrorNotInvited() {
      if (this.isNotInvitedError) return;
      this.isNotInvitedError = true;
      // setTimeout(() => {
      //   this.isNotInvitedError = false;
      //   this.$router.go(-1);
      // }, 3000)
    },
    showInfoAuthorChanged() {
      if (this.isAuthorChangedInfo) return;
      this.isAuthorChangedInfo = true;
      setTimeout(() => {
        this.isAuthorChangedInfo = false;
      }, 3000)
    },
    loadFirstVideo() {
      if (this.roomFirstVideo)
      {
        this.SET_ROOM_CURRENT_PLAYLIST_ID(this.roomFirstVideo.playlistId)
        this.SET_ROOM_CURRENT_VIDEO_ID(this.roomFirstVideo.videoId)
        this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
        this.updateVideoId();
      }
    },
    initChatRoom() {
      const token = localStorage.getItem('jwt')
      const baseURL = "https://tupli.kr/api/v1" + "/ws-stomp"
      const sock = new SockJS(baseURL);
      this.wsConnector = Stomp.over(sock);
      this.wsConnector.connect(
        token ? { Authorization: this.token } : { },
        async () => {
          await this.wsConnector.subscribe(`/sub/chat/room/${this.chatroomId}`, this.onReceiveMessage, token ? { Authorization: token } : undefined)
          this.SET_USER_START_TIME(new Date())
        },
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
        const idx = this.selectedVideoItem.findIndex(el => el == id)
        this.selectedVideoItem.splice(idx, 1)
      }
      else
      {
        this.selectedVideoItem.push(id)
      }
      if (this.roomAuthorId == this.userInfo.userSeq) this.playThisVideo()
    },
    onVideoReady() {
    },
    onVideoEnded() {
      this.loadNextVideo()
    },
    onVideoPlaying() {
    },
    onVideoPaused() {
    },
    onVideoBuffering() {
    },
    onVideoCued() {
      this.playVideo()
    },
    async onReceiveMessage(payload) {
      const id = payload.headers['message-id']
      const body = JSON.parse(payload.body);

      if (body.type == 'KICK')
      {
        this.isKickedError = true
      }
      else if (body.type == 'SYNC')
      {
        const currentPlaylistId = this.roomCurrentPlaylistId
        const currentVideoId = this.roomCurrentVideoId
        const currentVideoTime = await this.player.getCurrentTime();
        const currentPlayerState = await this.player.getPlayerState();
        const currentSyncSender = this.roomLastSyncSender

        const syncData = JSON.parse(body.message)
        const syncPlaylistId = syncData.playlistId
        const syncVideoId = syncData.videoId
        const syncVideoTime = syncData.videoPlaytime
        const syncPlayerState = syncData.playerState
        const syncSender = syncData.sender

        if (!currentSyncSender) {
          this.SET_ROOM_LAST_SYNC_SENDER(syncSender)
        }
        else if (currentSyncSender != syncSender)
        {
          this.showInfoAuthorChanged()
          await this.getRoomInfo()
          await this.SET_ROOM_LAST_SYNC_SENDER(syncSender)
        }

        if (this.userInfo.userSeq == this.roomAuthorId) return;

        if (currentPlayerState != syncPlayerState) {
          if (currentPlaylistId != syncPlaylistId) this.SET_ROOM_CURRENT_PLAYLIST_ID(syncPlaylistId)
          if (currentVideoId != syncVideoId) this.SET_ROOM_CURRENT_VIDEO_ID(syncVideoId)

          if (syncPlayerState == 1) this.player.playVideo()
          else if (syncPlayerState == 2) this.player.pauseVideo()
        }
        //console.log('syncVideoTime', syncVideoTime, 'currentVideoTime', currentVideoTime)
        if (Math.abs(syncVideoTime - currentVideoTime) > 2) {
          // if (currentPlaylistId != syncPlaylistId) this.SET_ROOM_CURRENT_PLAYLIST_ID(syncPlaylistId)
          // if (currentVideoId != syncVideoId) this.SET_ROOM_CURRENT_VIDEO_ID(syncVideoId)

          this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(syncVideoTime);
        }
        this.heartbeat = 0;
      }
      else// if (body.type == 'TALK')
      {
        // const profile = await axiosConnector.post('/echo', {
        //   nickname: '시스템',
        //   profilePictureUrl: 'https://picsum.photos/80/80'
        // })
        const author = { id: body.userSeq, name: body.sender ? body.sender : "익명의 유저", thumbnail: body.img };
        const content = body.message;
        const timestamp = new Date().getTime();
        const blockedUser = ( this.chatBlockedUid.find((v) => v == author.id) != undefined );
        const blockedMessage = ( this.chatBlockedId.find((v) => v == id) != undefined );
        this.$store.commit('playroom/RECEIVE_MESSAGE', { id, author, content, timestamp, blockedUser, blockedMessage });
      }
    },
    selectAllVideo() {
      this.selectedVideoItem = (this.selectedVideoItem.length == this.roomCurrentPlaylistVideos.length) ?
        [] :
        this.roomCurrentPlaylistVideos.map(v => parseInt(v.id));
    },
    isSelectedVideo(id) {
      return this.selectedVideoItem.findIndex(el => el == id) > -1
    },
    loadNextVideo() {
      if (this.roomNextVideo)
      {
        this.SET_ROOM_CURRENT_PLAYLIST_ID(this.roomNextVideo.playlistId)
        this.SET_ROOM_CURRENT_VIDEO_ID(this.roomNextVideo.videoId)
        this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      } else if (this.roomRepeat) {
        this.loadFirstVideo();
      }
    },
    updateVideoId() {
      //console.log('updateVideoId', this.roomCurrentPlaylistVideos, this.roomCurrentPlaylistId, this.roomCurrentVideoId)
      if (!this.roomCurrentPlaylistVideos) return;
      if (!this.roomCurrentVideoId) return;
      this.videoId = this.roomCurrentPlaylistVideos.find(roomCurrentPlaylistVideo => roomCurrentPlaylistVideo.id == this.roomCurrentVideoId).videoId;
    },
    playVideo() {
      this.player.playVideo()
    },
    seekTo() {
      console.log('seekTo', this.roomCurrentVideoPlaytime)
      this.player.seekTo(this.roomCurrentVideoPlaytime)
      //setTimeout(this.player.playVideo, 1000)
    },
    playThisVideo() {
      if (this.selectedVideoItem.length != 1) {
        alert('바로 재생할 영상을 1개만 선택해주세요')
        return;
      }
      console.log(this.selectedVideoItem)
      this.SET_ROOM_CURRENT_VIDEO_ID(this.selectedVideoItem[0])
      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      this.selectedVideoItem = []
      this.seekTo()
    },
    async loadLikeState() {
      if (!this.$store.state.isLogin) return;

      const { status, data } = await axiosConnector.get(`/playroom/${this.roomId}/like`)
      if (status != 200) return;
      this.SET_ROOM_LIKED(Boolean(data))
    },
    async playroomLike() {
      if (this.roomLiked)
      {
        // 좋아요 되어있으면 좋아요 해제
        this.SET_ROOM_LIKED(false)
        await axiosConnector.delete(`/playroom/${this.roomId}/like`);
      }
      else
      {
        // 좋아요 안되어있으면 좋아요 설정
        this.SET_ROOM_LIKED(true)
        await axiosConnector.post(`/playroom/${this.roomId}/like`);
      }

      await this.loadLikeState();
    },
    async playroomRepeat() {
      if (this.roomRepeat)
      {
        // 반복 설정 되어있으면 반복 해제
        this.SET_ROOM_REPEAT(false)
      }
      else
      {
        // 반복 설정 안되어있으면 반복 설정
        this.SET_ROOM_REPEAT(true)
      }
    },
    async sendMessage(payload) {
      if (!this.chatroomId) return;
      if (!payload || !payload.type || !payload.message || !payload.token) return;

      if (!this.wsConnector)
        if (this.$router.currentRoute.name == 'PlayroomDetail') await this.initChatRoom();
        else return;

      if (!this.wsConnector.subscriptions) return;

      return await this.wsConnector.send(
        "/pub/chat/message",
        JSON.stringify({ type: payload.type, roomId: this.chatroomId, message: payload.message }),
        { Authorization: payload.token }
      );
    },
    async sendKick(userId) {
      const token = localStorage.getItem('jwt')

      const kickData = {
        timestamp: new Date().getTime(),
        userId
      };

      if (!token) return;
      if (this.userInfo.userSeq != this.roomAuthorId) return;

      this.sendMessage({ type: 'KICK', message: JSON.stringify(kickData), token })
    },
    async sendSync() {
      const token = localStorage.getItem('jwt')

      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(await this.player.getCurrentTime())

      const syncData = {
        timestamp: new Date().getTime(),
        playlistId: this.roomCurrentPlaylistId,
        videoId: this.roomCurrentVideoId,
        videoPlaytime: this.roomCurrentVideoPlaytime,
        playerState: await this.player.getPlayerState()
      };

      if (!token) return;
      if (this.userInfo.userSeq != this.roomAuthorId) return;

      this.sendMessage({ type: 'SYNC', message: JSON.stringify(syncData), token })
    },
    sendChat() {
      const token = localStorage.getItem('jwt')

      this.disableChatbox()
      this.pendingToSendMessage()
      this.sendMessage({ type: 'TALK', message: this.message, token })
        .then(() => {
          this.clearMessage()
        })
        .catch((err) => {
          this.notifySendError()
        })
        .finally(() => {
          this.completeToSendMessage()
          this.enableChatbox()
        })
    },
    clearMessage() {
      this.message = ''
    },
    pendingToSendMessage() {
      this.sending = true
    },
    completeToSendMessage() {
      this.sending = false
    },
    enableChatbox() {
      this.canChat = true
      this.$nextTick(() => this.$refs.chat_input.focus())
    },
    disableChatbox() {
      this.canChat = false
    },
    notifySendError() {
      this.errorOnSend = true;
      setTimeout(this.clearSendError, 1000);
    },
    clearSendError() {
      this.errorOnSend = false;
    },
    checkHeartbeat() {
      this.checkPermission();

      if (this.heartbeat > 30)
      {
        // 방 접속자 정보가 있다면 (ID리스트)
        // Date.now().getTime() % this.roomUsers.length
        // const userOffset = this.roomUsers.findIndex(roomUser => roomUser == this.userInfo.userSeq)

        // 그런거 없으니까 일단은.. 무작정 도전!
        if (this.userInfo.userSeq)
          if ((parseInt(this.userInfo.userSeq) + Date.now()) % 10 == (parseInt(this.roomId) + parseInt(this.roomAuthorId)) % 10)
          {
            this.showInfoAuthorChanged()
            this.requestRoomAuthor()
          }

      }

      if (this.userInfo.userSeq && this.userInfo.userSeq == this.roomAuthorId) return;
      this.heartbeat += 1;
    },
    async requestRoomAuthor() {
      await axiosConnector.put(`/playroom/${this.roomId}/user`);
      await this.getRoomInfo();
      this.heartbeat = 0;
    },
    async releaseChatroom() {
      // 이 방에 있었던 시간 (밀리초 단위)
      this.SET_USER_END_TIME(new Date())


      var time = Math.floor((new Date(this.roomUserEndTime).getTime() - new Date(this.roomUserStartTime).getTime()) / 1000 / 1000)
      console.log(time, '초 경과')

      // 새로운 뱃지 취득시 이거 응답으로 받습니다...
      await axiosConnector.put(`/playroom/out/${this.roomId}`, { watchTime:time })

      await this.wsConnector.disconnect()
      this.wsConnector = null
    },
    ...mapMutations('playroom', ['RESET_VUEX_DATA', 'SET_ROOM_AUTHOR', 'SET_ROOM_LIKED', 'SET_ROOM_REPEAT', 'SEEK_VIDEO',
      'SET_ROOM_CURRENT_PLAYLIST_ID', 'SET_ROOM_CURRENT_VIDEO_ID', 'SET_ROOM_CURRENT_VIDEO_PLAYTIME',
      'SET_ROOM_LAST_SYNC_SENDER', 'SET_USER_START_TIME', 'SET_USER_END_TIME']),
    ...mapActions('account', ['validateToken']),
  },
}
</script>

<style lang="scss">

iframe {
  width: 100%;
  /*max-width: 650px; /* Also helpful. Optional. */
}

.chat-dialog {
  position: absolute;
  width: 100%;
  height: calc(100% - 200px);
  margin-bottom: 0;
  padding-bottom: 0;
  bottom: 0;
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
</style>
