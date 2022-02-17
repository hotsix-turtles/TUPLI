<template>
  <v-card
    class="mx-auto mb-10"
    height="100%"
  >
    <!-- 유튜브 동영상 플레이어 Wrapper (필요없음) -->
    <v-sheet class="playerWrapper sticky-header" style="z-index:2">
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

      <!-- 유튜브 동영상 플레이어 하단 네비게이션 -->
      <v-bottom-navigation
        grow
        class="elevation-0"
      >
        <!-- 플레이룸 좋아요 -->
        <v-btn
          v-if="isLogin"
          class="playroomLike"
          @click="togglePlayroomLike"
        >
          <span v-text="roomCurrentLike"></span>
          <v-icon
            v-if="roomLiked"
            color="accent"
          >
            mdi-heart
          </v-icon>
          <v-icon
            v-else
          >
            mdi-heart-outline
          </v-icon>
        </v-btn>

        <!-- 비로그인 플레이룸 좋아요 -->
        <v-btn
          v-else
          class="playroomLike"
          @click="isLoginNeededInfo = true"
        >
          <span v-text="roomCurrentLike"></span>
          <v-icon>
            mdi-heart-outline
          </v-icon>
        </v-btn>

        <!-- 플레이룸 댓글 -->
        <v-btn
          class="playroomChat"
          @click="isChatting = true"
        >
          <span>채팅</span>
          <v-icon>mdi-comment-text-outline</v-icon>
        </v-btn>

        <!-- 플레이룸 공유 -->
        <v-btn
          class="playroomShare"
          @click="kakaoShare"
        >
          <span>공유</span>
          <v-icon>mdi-share-outline</v-icon>
        </v-btn>
        <!-- 플레이룸 반복 -->
        <v-btn
          v-if="isAuthor"
          @click="onClickModal"
        >
          <span>설정</span>
          <v-icon>mdi-wrench-outline</v-icon>
        </v-btn>

        <!-- 플레이룸 나가기 -->
        <v-btn
          v-else
          class="playroomReport"
          @click="exitPrompt = true"
        >
          <span>나가기</span>
          <v-icon>mdi-exit-to-app</v-icon>
        </v-btn>
      </v-bottom-navigation>
      <hr>
      <!-- 유튜브 동영상 플레이어 하단 네비게이션(플레이룸 SNS 활동 네비게이션) 끝 -->
    </v-sheet>
    <!-- 유튜브 동영상 플레이어 끝 -->

    <!-- 플레이룸 페이지 -->
    <v-sheet
      class="overflow-y-none"
    >
      <modal
        :items="selectList"
        :modal-name="'플레이룸 설정'"
        :modal-type="'modal'"
        @on-select="onSelectModal"
      />

      <!-- 플레이룸 작성자 Wrapper -->
      <v-sheet class="playroomAuthorWrapper">
        <!-- 플레이룸 작성자 프로필 사진 -->
        <div class="authorProfilePic">
          <img
            :src="ImgUrl(roomAuthorProfilePic)"
            alt=""
            class="rounded-circle"
            style="width: 100%; height: auto;"
            @click="gotoAuthorProfile"
          >
        </div>

        <div class="d-flex flex-column justify-center ml-1">
          <!-- 플레이룸 작성자 이름 -->
          <span class="authorName mt-0 mb-0">{{ roomAuthorName }}</span>

          <!-- 플레이룸 팔로워 수 -->
          <span class="authorFollower mt-0 mb-0">{{ roomAuthorFollowerCount }}</span>
        </div>

        <v-btn
          v-if="isFollow && !isAuthor && isLogin"
          class="my-auto ml-auto"
          color="accent"
          text
          @click="unfollowAuthor"
        >
          언팔로우
        </v-btn>
        <v-btn
          v-else-if="!isAuthor && isLogin"
          class="my-auto ml-auto"
          color="accent"
          text
          @click="followAuthor"
        >
          팔로우
        </v-btn>
      </v-sheet>
      <hr>

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

        <!-- 플레이룸 운영 시간(?) Wrapper -->
        <div class="playroomPlaytimeWrapper">
          <v-icon color="accent" dense>
            mdi-clock
          </v-icon>
          {{roomPlaytime}}
          <!-- <p class="playtime">
            {{ roomPlaytime }}
          </p> -->
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
      <div class="playlistWrapper mx-3 my-2">
        <p>현재 재생중인 <b>플레이리스트</b></p>
        <!-- 플레이리스트 목록 -->
        <v-sheet
          style="display:flex; flex-wrap: nowrap; overflow-x: auto"
          class="playlistThumbnailWrapper px-1 py-1"
        >
          <PlaylistThumbnailItem
            v-for="(videos, playlistId, playlistIdx) in roomPlaylists"
            :id="playlistId"
            :key="playlistId"
            :src="playlistThumbnails[playlistIdx]"
            :selected="playlistId == roomCurrentPlaylistId"
            @click="isAuthor && SET_ROOM_CURRENT_PLAYLIST_ID(playlistId)"
          />
        </v-sheet>
      </div>
      <!-- 플레이룸 플레이리스트 목록 끝 -->

      <!-- 현재 플레이리스트 비디오 목록 Wrapper 시작 -->
      <div class="mb-12">
        <!-- 현재 플레이리스트 비디오 목록 -->
        <v-container>
          <div
            v-if="!isAuthor"
            class="playlistVideoNav d-flex justify-space-between align-center mb-1"
          >
            <v-btn
              small
              elevation="0"
              color="white"
              @click="onClickSelectAll"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
          </div>
          <video-list-item-small :videos="roomCurrentPlaylistVideos" @change-video="onPlaylistVideoSelected" :isVideoList="isAuthor"/>
        </v-container>
      </div>
      <!-- 플레이룸생성/내 플레이리스트에 넣기/저장하기 -->
      <detail-button-bottom />

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
          <v-card-title
            class="chat-title py-2"
          >
            <span
              class="font-weight-bold text-md-body-2"
            >
              실시간 채팅
            </span>
            <div
              class="ml-2 text-caption"
            >
              <v-icon dense>mdi-account</v-icon>
              {{ roomGuests.length }}
            </div>
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
            <v-container fluid>
              <ChatItem
                v-for="chat in roomChats"
                :id="chat.id"
                :key="chat.id"
                :author="chat.author"
                :content="chat.content"
                :timestamp="chat.timestamp"
                :blocked-user="chat.blockedUser"
                :blocked-message="chat.blockedMessage"
                @kick-user="sendKick"
              />
            </v-container>
          </v-card-text>
          <v-card-actions
            class="mx-1 mb-0 align-item-bottom"
          >
            <!-- 채팅 입력창 -->
            <v-row>
              <v-text-field
                ref="chat_input"
                v-model="message"
                :label="isLogin ? '메시지를 입력하세요' : '로그인 후 이용 가능합니다'"
                solo
                dense
                rounded
                :disabled="!canChat || !isLogin"
                :error="errorOnSend"
                @keydown.enter="sendChat"
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
                        class="mr-0"
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
                    <v-card
                      width="250"
                      height="250"
                    >
                      <emoji @click="clickEmoji" />
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
                  <v-btn text small>
                    <v-img width="30" height="30" :src="require('@/assets/tupli_send_chat.png')" @click="sendChat"></v-img>
                  </v-btn>

                  <!-- <v-icon
                    v-else
                    @click="sendChat"
                  >
                    mdi-send
                  </v-icon> -->
                  <!-- </v-fade-transition> -->
                </template>
              </v-text-field>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-sheet>

    <login-dialog :show="isLoginNeededInfo"/>

    <!--
      방장 전환시 팝업
    -->
    <loading-dialog
      title="방장 전환중..."
      :show="isAuthorChangedInfo"
    />

    <normal-dialog
      title="오류"
      content-html="이미 접속한 플레이룸입니다."
      max-width="290"
      :show="isDuplicatedError"
      :buttons="[{name: '확인'}]"
      button-spacing
      persistent
      @button-click="errorPromptHandler"
    />
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
      title="오류"
      content-html="중복 접속으로 인하여 연결이 끊어졌습니다."
      max-width="290"
      :show="isKickedDupError"
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
  </v-card>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import Vue from 'vue'
import VueYoutube from 'vue-youtube'
import PlaylistThumbnailItem from './PlaylistThumbnailItem.vue'
import ChatItem from './ChatItem.vue'
import axiosConnector from '../../utils/axios-connector';
import Tags from '../../components/common/Tags.vue';
import NormalDialog from '../../components/common/NormalDialog.vue';
import { getImage, playtimeConverter } from '../../utils/utils'
import Modal from '../../components/common/Modal.vue';
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import LoadingDialog from '../../components/common/LoadingDialog.vue';
import Emoji from '../../components/common/Emoji.vue';
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue';
import DetailButtonBottom from '../../components/playlist/DetailButtonBottom.vue';
import LoginDialog from '../../components/common/LoginDialog.vue';

Vue.use(VueYoutube)

export default {
  name: 'PlayroomDetail',
  components: {
    PlaylistThumbnailItem,
    ChatItem,
    Tags,
    NormalDialog,
    Modal,
    LoadingDialog,
    Emoji,
    VideoListItemSmall,
    DetailButtonBottom,
    LoginDialog
  },
  data() {
    return {
      showReducedContent: false,
      videoId: '',
      playerVars: {
        mute: 1,
        playsinline: 1
      },
      isFollow: false,
      isSelectedAll: false,
      lastPlaytime: 0,
      showEmoji: false,
      sending: false,
      message: '',
      canChat: true,
      errorOnSend: false,
      playlistThumbnails: [],
      isChatting: false,
      isOperationTimeError: false,
      isNotInvitedError: false,
      isAuthorChangedInfo: false,
      isLoginNeededInfo: false,
      isDuplicatedError: false,
      isKickedError: false,
      isKickedDupError: false,
      exitPrompt: false,
      exitTo: null,
      roomPlaytime: null,
      certification: false,
    }
  },
  metaInfo () {
    return {
      titleTemplate: `${this.roomTitle} | Tupli`,
      htmlAttrs: {
        lang: 'ko-KR'
      },
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' }
      ]
    }
  },
  computed: {
    selectList() {
      return this.roomRepeat ? {
        '수정하기': 'update',
        '삭제하기': 'delete',
        '플레이리스트 반복 해제': 'repeat',
      } : {
        '수정하기': 'update',
        '삭제하기': 'delete',
        '플레이리스트 반복 설정': 'repeat',
      }
    },
    roomCurrentLike() {
      return this.roomLikesCnt + (this.roomLiked ? 1 : 0);
    },
    roomContentReduced() {
      return this.roomContent == this.roomReducedContent
    },
    isDialogOpened() {
      return this.isDuplicatedError || this.isOperationTimeError || this.isNotInvitedError || this.isAuthorChangedInfo
    },
    ...mapState([
      'userId',
      'isLogin'
    ]),
    ...mapState('playroom', [
      'roomId',
      'roomTitle',
      'roomPublic',
      'roomLiked',
      'roomLikesCnt',
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
      'roomGuests',
      'roomSendingMessage',
      'chatroomId',
      'chatBlockedId',
      'chatBlockedUid',
      'roomLastSyncSender',
      'wsConnector',
      'heartbeat'
    ]),
    ...mapState('common', [
      'showModal'
    ]),
    ...mapGetters('playroom', [
      'roomPublicLabel',
      'roomReducedContent',
      'roomCurrentPlaylistVideos',
      'roomFirstVideo',
      'roomPrevVideo',
      'roomNextVideo',
      'isAuthor'
    ])
  },
  async created() {
    //await this.checkToken();
    await this.RESET_VUEX_DATA();
  },
  mounted() {
    this.$nextTick(async () => {
      this.player = this.$refs.youtube.player;
      await this.initPlayroom();
    });

    this.$watch('roomCurrentPlaylistVideos', (newVal, oldVal) =>
    {
      this.playlistThumbnails = Object.keys(this.roomPlaylists).reduce((prevPlaylistIds, curPlaylistId) => {
        if (this.roomPlaylists[curPlaylistId])
          prevPlaylistIds.push(this.roomVideos.find(roomVideo => roomVideo.id == this.roomPlaylists[curPlaylistId][0]).thumbnail);

        return prevPlaylistIds;
      }, []);
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
    })

    this.$watch('roomChats', (newVal, oldVal) => {
      this.$nextTick(() => {
        let messages = this.$refs.chat_messages;
        if (!messages) return;
        messages.scrollTo({ top: messages.scrollHeight, behavior: 'smooth' });
      });
    });

    this.$watch('roomStartTime', (newVal, oldVal) => {
      this.loadRoomPlaytime();
    });

    this.$watch('roomEndTime', (newVal, oldVal) => {
      this.loadRoomPlaytime();
    });
  },
  async beforeRouteLeave(to, from, next) {
    if (to.name == 'Error')
    {
      this.stopHeartbeat();
      await this.destroyWsConnector();
      next();
      return;
    }

    if (this.isChatting)
    {
      this.closeChatbox();
      next(false);
      return;
    }

    if (!this.certification)
    {
      this.exitTo = to.path;
      this.exitPrompt = true;
      next(false);
      return;
    }

    this.deselectAllDetailVideos();
    this.stopHeartbeat();
    await this.destroyWsConnector();
    next();
  },
  methods: {
    async checkToken() {
      const isValid = await this.validateToken();
      if (!isValid) {
        this.clearLocalStorage();
        this.goToLoginPage();
      }
    },
    clearLocalStorage() {
      localStorage.clear();
    },
    goToLoginPage() {
      this.certification = true;
      this.$router.push({ name: 'Login' })
    },
    async requestUpdatePlayroom() {
      await this.updatePlayroom();
      this.certification = true;
      this.$router.push({ name: 'PlayroomUpdateForm', params: { id: this.roomId } })
    },
    async requestDeletePlayroom() {
      await this.loadRoomInfo(this.roomId);
      this.roomGuests.map(async (roomGuest) => await this.sendKick(roomGuest));
      await this.deletePlayroom();
      await this.RESET_VUEX_DATA();
      this.certification = true;
      this.$router.push({ name: 'Home' })
    },
    async onSelectModal(item) {
      if (item === 'update') {
        await this.requestUpdatePlayroom();
      } else if (item === 'delete') {
        await this.requestDeletePlayroom();
      } else if (item === 'repeat') {
        this.togglePlayroomRepeat();
      }
    },
    errorPromptHandler() {
      this.certification = true;
      this.$router.go(-1)
    },
    exitPromptHandler(idx) {
      if (idx == 0)
      {
        this.certification = true;
        if (this.exitTo) this.$router.push(this.exitTo);
        else this.$router.go(-1);
      }
      this.exitPrompt = false;
    },
    closeChatbox() {
      this.isChatting = false;
    },
    async initPlayroom() {
      await this.loadRoomInfo(this.$route.params.id);
      if (!this.checkPermission()) return;
      if (this.isLogin) await this.loadLikeState();
      await this.loadFollowerCount();
      if (this.isLogin) await this.loadFollowState();
      await this.loadFirstVideo();
      await this.loadRoomPlaytime();
      await this.initWsConnector();
      if (!this.checkDuplicatedAccess()) return;
    },
    async checkDuplicatedAccess() {
      try {
        // 동일 방 중복 접속 체크
        if (this.roomGuests && this.roomGuests.filter(guestId => guestId == this.userId).length)
          throw 'duplcated-access';
      } catch (err) {
        console.log(err)
        switch (err) {
        case 'duplcated-access':
          await this.sendKickDup();
          this.showErrorDuplicatedAccess();
          this.stopHeartbeat();
          await this.destroyWsConnector();
          return false;
        }
      }
      return true;
    },
    async checkPermission() {
      if (this.roomId == -1) return;

      try {
        // 방장이 아니고 방 운영시간 외이면
        if (!this.isAuthor && (this.roomStartTime >= Date.now() || this.roomEndTime <= Date.now()))
          throw 'not-operation-time';

        // 비공개방이고 미초대 유저면
        if (!this.roomPublic && !this.roomInviteIds.find(inviteId => inviteId == this.userId))
          throw 'not-invited';
      } catch (err) {
        console.log(err)
        switch (err) {
        case 'not-operation-time':
          this.showErrorOperationTime();
          this.stopHeartbeat();
          await this.destroyWsConnector();
          return false;
        case 'not-invited':
          this.showErrorNotInvited();
          this.stopHeartbeat();
          await this.destroyWsConnector();
          return false;
        default:
          break;
        }
      }
      return true;
    },
    showErrorDuplicatedAccess() {
      if (this.isDialogOpened) return;
      this.isDuplicatedError = true;
    },
    hideErrorDuplicatedAccess() {
      this.isDuplicatedError = false;
    },
    showErrorOperationTime() {
      if (this.isDialogOpened) return;
      this.isOperationTimeError = true;
    },
    hideErrorOperationTime() {
      this.isOperationTimeError = false;
    },
    showErrorNotInvited() {
      if (this.isDialogOpened) return;
      this.isNotInvitedError = true;
    },
    hideErrorNotInvited() {
      this.isNotInvitedError = false;
    },
    async checkConnection() {
      if (this.$router.currentRoute.name != 'PlayroomDetail') return;
      if (this.wsConnector && this.wsConnector.subscriptions) return;
      await this.initWsConnector();
    },
    async checkHeartbeat() {
      this.checkPermission();

      if (this.heartbeat > 30)
      {
        if (this.userId)
          if ((parseInt(this.userId) + Date.now()) % 10 == (parseInt(this.roomId) + parseInt(this.roomAuthorId)) % 10)
          {
            this.showInfoAuthorChanged()
            this.requestRoomAuthor()
          }
      }

      if (this.isAuthor) this.sendSync();
      else this.setHeartbeat();
    },
    showInfoAuthorChanged() {
      if (this.isDialogOpened) return;
      this.isAuthorChangedInfo = true;
      setTimeout(this.hideInfoAuthorChanged, 3000)
    },
    hideInfoAuthorChanged() {
      this.isAuthorChangedInfo = false;
    },
    async loadFollowerCount() {
      const userFollowerInfo = await axiosConnector.get(`/profile/followers/${this.roomAuthorId}/count`);
      this.setRoomAuthor({ follower: parseInt(userFollowerInfo.data) })
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
    loadRoomPlaytime() {
      this.roomPlaytime = playtimeConverter(this.roomStartTime, this.roomEndTime);
    },
    async initWsConnector() {
      if (!this.$route.params.id) return;

      const token = localStorage.getItem('jwt');
      const baseURL = "https://tupli.kr/api/v1" + "/ws-stomp"
      const sock = new SockJS(baseURL);

      this.setWsConnector(Stomp.over(sock));
      if (!this.wsConnector) return;

      this.wsConnector.connect(
        token ? { Authorization: token } : { },
        () => {
          this.wsConnector.subscribe(`/sub/chat/room/${this.chatroomId}`, this.onReceiveMessage, token ? { Authorization: token } : undefined);
          this.startHeartbeat(setInterval(this.checkHeartbeat, 1000));
        },
        () => alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.")
      )

      if (this.isLogin) {
        await axiosConnector.post(`/playroom/in/${this.roomId}`)
        this.SET_USER_START_TIME(new Date());
      }
    },
    async destroyWsConnector() {
      if (this.roomId == -1) return;

      if (this.isLogin) {
        // 이 방에 있었던 시간 (밀리초 단위)
        this.SET_USER_END_TIME(new Date());

        var time = Math.floor((new Date(this.roomUserEndTime).getTime() - new Date(this.roomUserStartTime).getTime()) / 1000 / 1000)
        console.log(time, '초 경과')

        // 새로운 뱃지 취득시 이거 응답으로 받습니다...
        await axiosConnector.put(`/playroom/out/${this.roomId}`, { watchTime: time })
      }

      if (this.wsConnector) await this.wsConnector.disconnect()
      this.resetWsConnector();
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
    onPlaylistVideoSelected(video) {
      if (!this.isAuthor) return;
      this.SET_ROOM_CURRENT_VIDEO_ID(video.id)
      this.playThisVideo()
    },
    async onReceiveMessage(payload) {
      const id = payload.headers['message-id']
      const body = JSON.parse(payload.body);

      if (body.type == 'KICK')
      {
        const { userId } = JSON.parse(body.message)
        if (this.userId == userId) this.isKickedError = true
      }
      else if (body.type == 'KICK_DUP')
      {
        const { userId } = JSON.parse(body.message)
        if (this.userId == userId) this.isKickedDupError = true
      }
      else if (body.type == 'SYNC')
      {
        this.resetHeartbeat();

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
        const syncSender = body.userSeq

        if (!currentSyncSender)
        {
          // console.log('currentSyncSender', currentSyncSender, 'syncSender', syncSender, syncData)
          await this.SET_ROOM_LAST_SYNC_SENDER(syncSender)
        }
        else if (currentSyncSender != syncSender)
        {
          // console.log('currentSyncSender', currentSyncSender, 'syncSender', syncSender)
          this.showInfoAuthorChanged()
          await this.SET_ROOM_LAST_SYNC_SENDER(syncSender)
          await this.loadRoomInfo(this.$route.params.id);
        }

        if (this.isAuthor) return;

        if (currentPlaylistId != syncPlaylistId) this.SET_ROOM_CURRENT_PLAYLIST_ID(syncPlaylistId)
        if (currentVideoId != syncVideoId) this.SET_ROOM_CURRENT_VIDEO_ID(syncVideoId)
        this.updateVideoId();

        // 싱크와 플레이어 상태가 다르다
        if (currentPlayerState != syncPlayerState) {
          if (syncPlayerState == 1) this.player.playVideo()
          else if (syncPlayerState == 2) this.player.pauseVideo()
        }
        //console.log('syncVideoTime', syncVideoTime, 'currentVideoTime', currentVideoTime)
        if (Math.abs(syncVideoTime - currentVideoTime) > 2) {
          // if (currentPlaylistId != syncPlaylistId) this.SET_ROOM_CURRENT_PLAYLIST_ID(syncPlaylistId)
          // if (currentVideoId != syncVideoId) this.SET_ROOM_CURRENT_VIDEO_ID(syncVideoId)

          if (syncVideoId == this.roomCurrentVideoId) this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(syncVideoTime);
          this.seekTo()
        }
      }
      else
      {
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
        this.SET_ROOM_CURRENT_PLAYLIST_ID(this.roomNextVideo.playlistId);
        this.SET_ROOM_CURRENT_VIDEO_ID(this.roomNextVideo.videoId);
        this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0);
        this.updateVideoId();
      } else if (this.roomRepeat) {
        this.loadFirstVideo();
      }
    },
    updateVideoId() {
      //console.log('updateVideoId', this.roomCurrentPlaylistVideos, this.roomCurrentPlaylistId, this.roomCurrentVideoId)
      if (!this.roomCurrentPlaylistVideos) return;
      if (!this.roomCurrentVideoId) return;
      if (!this.roomCurrentPlaylistVideos.find(roomCurrentPlaylistVideo => roomCurrentPlaylistVideo.id == this.roomCurrentVideoId)) return;
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
      this.SET_ROOM_CURRENT_VIDEO_PLAYTIME(0)
      this.updateVideoId();

      this.selectedVideoItem = []
      console.log('playThisVideo')
      this.seekTo()
    },
    async sendMessage(payload) {
      if (!this.chatroomId) return;
      if (!payload || !payload.type || !payload.message || !payload.token) return;

      if (!this.wsConnector) return;
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
      if (this.userId != this.roomAuthorId) return;

      await this.sendMessage({ type: 'KICK', message: JSON.stringify(kickData), token })
    },
    async sendKickDup() {
      const token = localStorage.getItem('jwt')

      const kickData = {
        timestamp: new Date().getTime(),
        userId: this.userId
      };

      if (!token) return;
      if (this.userId != this.roomAuthorId) return;

      await this.sendMessage({ type: 'KICK_DUP', message: JSON.stringify(kickData), token })
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
      if (this.roomLastSyncSender != this.userId) await this.loadRoomInfo(this.$route.params.id);
      if (this.userId != this.roomAuthorId) return;

      await this.sendMessage({ type: 'SYNC', message: JSON.stringify(syncData), token })
    },
    async sendChat() {
      const token = localStorage.getItem('jwt')

      this.disableChatbox()
      this.pendingToSendMessage()
      await this.sendMessage({ type: 'TALK', message: this.message, token })
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
    async requestRoomAuthor() {
      await axiosConnector.put(`/playroom/${this.roomId}/user`);
      await this.loadRoomInfo(this.$route.params.id);
      this.resetHeartbeat();
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    gotoAuthorProfile() {
      this.certification = true;
      this.$router.push(`/profile/${this.roomAuthorId}`);
    },
    clickEmoji: function({ idx, emoticon }) {
      this.message += `#${idx}`;
      this.$nextTick(() => this.$refs.chat_input.focus())
    },
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        this.deselectAllDetailVideos()
      } else {
        this.selectAllDetailVideos(this.roomCurrentPlaylistVideos)
      }
      this.isSelectedAll = !this.isSelectedAll
    },
    kakaoShare() {
      Kakao.Link.sendDefault({
        objectType: "feed",
        content: {
          title: this.roomTitle,
          description: this.roomContent,
          imageUrl:
            this.roomVideos.find(roomVideo => roomVideo.id == this.roomPlaylists[this.roomCurrentPlaylistId][0]).thumbnail,
          link: {
            mobileWebUrl: `https://tupli.kr/playroom/${this.roomId}`,
            webUrl: `https://tupli.kr/playroom/${this.roomId}`
          },
        },
        social: {
          likeCount: this.roomLikesCnt,
          subscriberCount: this.roomGuests.length,
        },
        buttons: [
          {
            title: '플레이룸 이동',
            link: {
              mobileWebUrl: `https://tupli.kr/playroom/${this.roomId}`,
              webUrl: `https://tupli.kr/playroom/${this.roomId}`
            }
          },
          {
            title: '다른 영상 찾기',
            link: {
              mobileWebUrl: 'https://tupli.kr/category',
              webUrl: 'https://tupli.kr/category'
            }
          }
        ]
      })
    },
    async loadFollowState() {
      await this.loadFollowerCount();
      const {data, status} = await axiosConnector.get(`/account/follow/${this.roomAuthorId}`)
      this.isFollow = (status == 200);
      return this.isFollow;
    },
    async followAuthor() {
      await axiosConnector.post(`/account/follow/${this.roomAuthorId}`)
      return await this.loadFollowState();
    },
    async unfollowAuthor() {
      await axiosConnector.delete(`/account/follow/${this.roomAuthorId}`)
      return await this.loadFollowState();
    },
    ...mapMutations('playroom', [
      'RESET_VUEX_DATA',
      'SET_ROOM_AUTHOR',
      'SET_ROOM_LIKED',
      'SET_ROOM_REPEAT',
      'SEEK_VIDEO',
      'SET_ROOM_CURRENT_PLAYLIST_ID',
      'SET_ROOM_CURRENT_VIDEO_ID',
      'SET_ROOM_CURRENT_VIDEO_PLAYTIME',
      'SET_ROOM_LAST_SYNC_SENDER',
      'SET_USER_START_TIME',
      'SET_USER_END_TIME'
    ]),
    ...mapActions('playroom', [
      'setRoomInfo',
      'setRoomAuthor',
      'loadRoomInfo',
      'loadLikeState',
      'togglePlayroomLike',
      'togglePlayroomRepeat',
      'updatePlayroom',
      'deletePlayroom',
      'saveFormData',
      'startHeartbeat',
      'stopHeartbeat',
      'setHeartbeat',
      'resetHeartbeat',
      'setWsConnector',
      'resetWsConnector'
    ]),
    ...mapActions('account', [
      'validateToken'
    ]),
    ...mapActions('common', [
      'onClickModal',
    ]),
    ...mapActions('video', [
      'selectAllDetailVideos',
      'deselectAllDetailVideos'
    ])
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
}

.authorProfilePic {
  width: 40px;
  height: 40px;
  border: 1px #bbb solid;
  border-radius: 100%;
  margin: 5px;
}

.authorName {
  font-size: 14px;
  font-weight: bold;
  margin-right: 3px;
}

.authorFollower:before {
  content: '팔로워';
  margin-right: 3px;
}

.authorFollower {
  color: #bbb;
  font-size: 11px;
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
  color: #d8d8ee;
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

.chat-title {
  background-color:#d8d8ee;
  font-weight: bold;
}
</style>
