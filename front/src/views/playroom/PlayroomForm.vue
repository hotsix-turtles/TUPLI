<template>
  <div>
    <!-- 뒤로가기/완료 or 수정 -->
    <div class="d-flex justify-space-between fixed-top light-background">
      <div class="d-flex mx-3 my-3">
        <div>
          <v-icon @click="resetFormAndGo">
            mdi-arrow-left
          </v-icon>
        </div>
        <div class="font-2 semi-bold center">
          {{ pageName }}
        </div>
      </div>
      <div
        class="clickable font-2 semi-bold mt-3 mr-3 color-dark-gray color-main"
        @click="submit"
        v-text="formType == 'create' ? '완료' : '수정'"
      />
    </div><br><br>

    <!-- 플레이룸 생성 폼 -->
    <v-form class="mt-2 mx-1">
      <v-container>
        <!-- 제목 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="formData.title"
              :counter="30"
              color="accent"
              label="플레이룸 제목을 입력해주세요."
              hint="3글자 이상 입력해주세요"
              required
            />
          </v-col>
        </v-row>

        <!-- 소개글 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="formData.content"
              :counter="80"
              label="플레이룸 소개글을 입력해주세요."
              color="accent"
            />
          </v-col>
        </v-row>

        <!-- 태그 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <tag-input
              :proped-tags="formData.tags"
              @tag-input="updateTags"
            />
          </v-col>
        </v-row>

        <!-- 플레이리스트 구성 하기 레이블 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between"
          >
            <div>
              <div class="">
                플레이리스트 구성
              </div>
              <div class="font-4 color-dark-gray">
                원하는 플레이리스트를 추가해주세요.
              </div>
            </div>

            <!-- 플레이리스트 추가 버튼 -->
            <div
              class="py-0 mr-0"
            >
              <v-btn
                small
                color="accent"
                elevation="2"
                rounded
                @click="saveAndGoPlaylist"
              >
                <v-icon
                  small
                  color="white"
                >
                  mdi-plus
                </v-icon>
                <span style="color: white;">플레이리스트 추가</span>
              </v-btn>
            </div>
          </v-col>
        </v-row>

        <!-- 플레이리스트 리스트 조작 버튼 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between pb-0"
          >
            <v-btn
              small
              elevation="0"
              color="white"
              class="mb-3"
              @click="selectAllVideo"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4 mt-1">
              {{ numberOfAddedPlaylists }}개 플레이리스트 / {{ numberOfAddedPlaylistSelectedVideos }}개 영상 선택
            </p>
          </v-col>
        </v-row>

        <!-- 플레이리스트 리스트 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="pt-0"
          >
            <v-card
              v-if="!addedPlaylists.length"
              class="d-flex flex-column justify-center align-center"
              min-height="300"
            >
              <p>플레이리스트가 비어있습니다</p>
            </v-card>
            <playlist-list-item-small
              :playlists="addedPlaylists"
              :playlist-readonly="true"
              :video-readonly="false"
            />
          </v-col>
        </v-row>

        <!-- 친구 초대 레이블 -->
        <v-row
          v-if="formType=='create'"
        >
          <v-col
            cols="12"
            md="12"
            class="d-flex align-center justify-space-between py-0 my-5"
          >
            <div class="">
              <div class="">
                친구 초대
              </div>
              <div class="font-4 color-dark-gray">
                친구에게 플레이룸 시작 전 알림을 보냅니다.
              </div>
            </div>

            <!-- 친구 초대 버튼 -->
            <div
              class="py-0 mr-0"
            >
              <v-btn
                color="accent"
                elevation="2"
                rounded
                small
                @click="saveAndGoFriend"
              >
                <v-icon
                  small
                  color="white"
                >
                  mdi-plus
                </v-icon>
                <span style="color: white;">친구 추가</span>
              </v-btn>
            </div>
          </v-col>
        </v-row>

        <!-- 친구 초대 리스트 조작 버튼 -->
        <!-- <v-row
          v-if="formType=='create'"
        >
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between pb-0"
          >
            <v-btn
              small
              elevation="0"
              color="white"
              class="mb-3"
              @click="selectAllFriend"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4 mt-1">
              {{ numberOfAddedFriends }}명 유저 선택
            </p>
          </v-col>
        </v-row> -->

        <!-- 친구 초대 리스트 -->
        <v-row
          v-if="formType=='create'"
        >
          <v-col
            cols="12"
            md="12"
            class="pt-0"
          >
            <v-card
              class="d-flex flex-column justify-center align-center"
              min-height="300"
            >
              <p v-if="!addedFriends.length">
                초대한 친구가 없습니다
              </p>
              <account-list-item-small
                :accounts="addedFriends"
                readonly
              />
            </v-card>
          </v-col>
        </v-row>

        <!-- 플레이룸 시간 설정 레이블 -->
        <v-row
          class="h-100"
        >
          <v-col
            cols="12"
            md="12"
            class="d-flex justify-space-between py-0 mt-5"
          >
            <div class="d-flex align-center">
              <span>
                시간 설정
              </span>
              <span class="font-4 ml-2 color-dark-gray">
                플레이룸을 운영할 시간을 지정합니다.
              </span>
            </div>
            <v-checkbox
              v-model="autoTime"
              label="자동"
              class="mt-2"
              color="#5B5C9D"
            />
          </v-col>
        </v-row>

        <!-- 플레이룸 시작시간 설정 -->
        <v-row>
          <v-col
            cols="12"
            sm="5"
            class="d-flex flex-row justify-space-between"
          >
            <v-menu
              ref="startDateMenu"
              v-model="startDateMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="startDate"
                  label="플레이룸 시작 시간"
                  persistent-hint
                  prepend-icon="mdi-calendar"
                  :rules="startDateRules"
                  v-bind="attrs"
                  :disabled="autoTime"
                  v-on="on"
                />
              </template>
              <v-date-picker
                v-model="startDate"
                no-title
                @input="startDateMenu = false"
              />
            </v-menu>

            <v-menu
              ref="startTimeMenu"
              v-model="startTimeMenu"
              :close-on-content-click="false"
              :nudge-right="40"
              :return-value.sync="startTime"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="startTime"
                  label=""
                  prepend-icon="mdi-clock-time-four-outline"
                  :rules="startTimeRules"
                  readonly
                  v-bind="attrs"
                  :disabled="autoTime"
                  v-on="on"
                />
              </template>
              <v-time-picker
                v-if="startTimeMenu"
                v-model="startTime"
                full-width
                @click:minute="$refs.startTimeMenu.save(startTime)"
              />
            </v-menu>
          </v-col>
        </v-row>

        <!-- 플레이룸 종료시간 설정 -->
        <v-row>
          <v-col
            cols="12"
            sm="5"
            class="d-flex flex-row justify-space-between"
          >
            <v-menu
              ref="endDateMenu"
              v-model="endDateMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="endDate"
                  label="플레이룸 종료 시간"
                  persistent-hint
                  prepend-icon="mdi-calendar"
                  :rules="endDateRules"
                  v-bind="attrs"
                  :disabled="autoTime"
                  v-on="on"
                />
              </template>
              <v-date-picker
                v-model="endDate"
                no-title
                @input="endDateMenu = false"
              />
            </v-menu>

            <v-menu
              ref="endTimeMenu"
              v-model="endTimeMenu"
              :close-on-content-click="false"
              :nudge-right="40"
              :return-value.sync="endTime"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="endTime"
                  label=""
                  prepend-icon="mdi-clock-time-four-outline"
                  :rules="endTimeRules"
                  readonly
                  v-bind="attrs"
                  :disabled="autoTime"
                  v-on="on"
                />
              </template>
              <v-time-picker
                v-if="endTimeMenu"
                v-model="endTime"
                full-width
                @click:minute="$refs.endTimeMenu.save(endTime)"
              />
            </v-menu>
          </v-col>
        </v-row>

        <!-- 공개 여부 -->
        <v-row
          v-if="formType=='create'"
        >
          <v-col
            cols="12"
            md="4"
            class="d-flex justify-space-between py-0 mb-5"
          >
            <div class="d-flex align-center">
              <span>공개 설정</span>
              <span class="font-4 ml-2 color-dark-gray">
                {{ isPublicMsg }}
              </span>
            </div>
            <v-switch
              v-model="formData.isPublic"
              color="#5B5C9D"
            />
          </v-col>
        </v-row>

        <!-- 플레이룸 셔플 여부 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
            class="d-flex justify-space-between py-0 mb-5"
          >
            <div class="d-flex align-center">
              <span>셔플 사용</span>
              <span class="font-4 ml-2 color-dark-gray">
                {{ isShuffleMsg }}
              </span>
            </div>
            <v-switch
              v-model="formData.isShuffle"
              color="#5B5C9D"
            />
          </v-col>
        </v-row>

        <!-- 플레이룸 최대 인원 수 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
            class="d-flex justify-space-between py-0 mb-5"
          >
            <div class="d-flex align-center">
              <span>최대 인원수</span>
              <span class="font-4 ml-2 color-dark-gray">
                참여할 최대 유저 수를 설정합니다.
              </span>
            </div>
            <v-combobox
              v-model="formData.userCountMax"
              class="ml-5 mt-3"
              style="width: 50px;"
              dense
              solo
              :items="userCountMaxItems"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-form>
    <loading-dialog
      :title="formType == 'create' ? '플레이룸 생성중...' : '플레이룸 변경중...'"
      :show="isSending"
    />
    <timeout-dialog
      v-model="isFormInputError"
      title="생성 실패"
      content="입력 값이 올바르지 않습니다"
      timeout="2000"
      top-progress
      @timeout="isFormInputError = false"
    />
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import TagInput from '../../components/common/TagInput.vue'
import { mapMutations, mapActions } from 'vuex'
import axiosConnector from '../../utils/axios-connector';
import AccountListItemSmall from '../../components/account/AccountListItemSmall.vue'
import LoadingDialog from '../../components/common/LoadingDialog.vue'
import TimeoutDialog from '../../components/common/TimeoutDialog.vue'

export default {
  name: 'PlayroomForm',
  components: {
    TagInput,
    PlaylistListItemSmall,
    AccountListItemSmall,
    LoadingDialog,
    TimeoutDialog
  },
  data: function() {
    return {
      titleRules: [
        v => !!v || '제목은 필수입니다.',
        v => v.length > 2 || '제목은 3글자 이상 작성해야 합니다.',
      ],
      startDateRules: [
        v => !!v || '시작날짜는 필수입니다.',
        //v => this.startDateTime.getTime() <= this.endDateTime.getTime() || '시작날짜는 종료날짜보다 이전이어야 합니다',
      ],
      startTimeRules: [
        v => !!v || '시작시간은 필수입니다.',
        //v => this.startDateTime.getTime() <= this.endDateTime.getTime() || '시작시간은 종료시간보다 이전이어야 합니다',
      ],
      endDateRules: [
        v => !!v || '종료날짜는 필수입니다.',
        v => this.startDateTime.getTime() < this.endDateTime.getTime() || '종료날짜는 시작날짜 이전일 수 없습니다',
      ],
      endTimeRules: [
        v => !!v || '종료시간은 필수입니다.',
        v => this.startDateTime.getTime() < this.endDateTime.getTime() || '종료시간은 시간시간 이전일 수 없습니다',
      ],
      isShuffle: false,
      isSending: false,

      isFormInputError: false,

      // Create할 때 넘길 데이터
      formType: '',
      formData: {
        title: '',
        content: '',
        tags: [],
        isPublic: true,
        inviteIds: [],
        playlists: [],
        userCountMax: 5
      },
      numberOfPlaylist: 0,
      numberOfPlaylistVideos: 0,

      startDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      startTime: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(11, 5),
      startDateMenu: false,
      startTimeMenu: false,
      startDateTime: new Date(),

      endDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      endTime: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(11, 5),
      endDateMenu: false,
      endTimeMenu: false,
      endDateTime: new Date(),

      autoTime: true,
      userCountMaxItems: [2,3,5,10,20] // TODO: 프리미엄 유저 기능으로 100명 무제한 이런식?
    }
  },
  computed: {
    pageName () {
      return this.formType == 'create' ? "플레이룸 생성" :
        this.formType == 'update' ? "플레이룸 정보 변경" : null
    },
    computedDateFormatted () {
      return this.formatDate(this.startDate)
    },
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이룸을 공개합니다." : "내 플레이룸을 비공개합니다."
    },
    isShuffleMsg () {
      return this.isShuffle ? "플레이리스트를 랜덤으로 섞습니다." : "플레이리스트 순서대로 재생합니다."
    },
    ...mapState('playlist', ['addedPlaylists', 'addedPlaylistVideoIds']),
    ...mapState('friend', ['addedFriends', 'selectedFriends']),
    ...mapState('playroom', ['savedFormData']),
    ...mapGetters('playlist', ['numberOfAddedPlaylists', 'numberOfAddedPlaylistSelectedVideos', 'numberOfAddedPlaylistVideos']),
    ...mapGetters('friend', ['numberOfAddedFriends'])
  },
  watch: {
    startDate (val) {
      this.startDateTime = new Date(`${this.startDate}T${this.startTime}:00.000+09:00`)
      if (this.startDateTime <= new Date()) {
        const timezoneOffset = new Date().getTimezoneOffset() * 60 * 1000;
        const localTime = new Date(Date.now() - timezoneOffset)

        this.startDateTime = localTime;
        this.startDate = this.startDateTime.toISOString().substr(0, 10);
        this.startTime = this.startDateTime.toISOString().substr(11, 5);
      }
    },
    startTime (val) {
      this.startDateTime = new Date(`${this.startDate}T${this.startTime}:00.000+09:00`)
      if (this.startDateTime <= new Date()) {
        const timezoneOffset = new Date().getTimezoneOffset() * 60 * 1000;
        const localTime = new Date(Date.now() - timezoneOffset)

        this.startDateTime = localTime;
        this.startDate = this.startDateTime.toISOString().substr(0, 10);
        this.startTime = this.startDateTime.toISOString().substr(11, 5);
      }
    },
    endDate (val) {
      this.endDateTime = new Date(`${this.endDate}T${this.endTime}:00.000+09:00`)
      if (this.endDateTime <= new Date()) {
        const timezoneOffset = new Date().getTimezoneOffset() * 60 * 1000;
        const localTime = new Date(Date.now() - timezoneOffset)

        this.endDateTime = localTime;
        this.endDate = this.endDateTime.toISOString().substr(0, 10);
        this.endTime = this.endDateTime.toISOString().substr(11, 5);
      }
    },
    endTime (val) {
      this.endDateTime = new Date(`${this.endDate}T${this.endTime}:00.000+09:00`)
      if (this.endDateTime <= new Date()) {
        const timezoneOffset = new Date().getTimezoneOffset() * 60 * 1000;
        const localTime = new Date(Date.now() - timezoneOffset)

        this.endDateTime = localTime;
        this.endDate = this.endDateTime.toISOString().substr(0, 10);
        this.endTime = this.endDateTime.toISOString().substr(11, 5);
      }
    },
  },
  created() {
    // const isValid = await this.validateToken();
    // if (!isValid)
    // {
    //   // 토큰 만료시 현재 vuex 정보를 초기화하고 로그인 페이지로 이동
    //   localStorage.clear();
    //   this.$router.push('/login')
    // }

    if (this.$route.name == 'PlayroomForm') this.formType = 'create'
    if (this.$route.name == 'PlayroomUpdateForm') this.formType = 'update'
    if (this.$route.name == 'PlayroomByPlaylist') this.formType = 'create'

    if (this.savedFormData) {
      this.formData = this.savedFormData
      this.RESET_FORM_DATA()

      if (!Object.keys(this.formData.playlists).length) return;

      const promiseArray = [
        ...Object.keys(this.formData.playlists).map(
          async playlistId => {
            const {data} = await this.getPlaylistInfo(playlistId);
            this.selectPlaylist2(data);
          }
        )
      ]

      Promise.all(promiseArray).then(
        () => {
          this.addPlaylists();
          this.deselectAllPlaylistVideo();
          Object.keys(this.formData.playlists).map(
            playlistId => {
              this.formData.playlists[playlistId].map(videoId => this.selectPlaylistVideo(videoId));
            }
          );
          this.formData.playlists = []
        }
      );
    }
  },
  methods: {
    isValid () {
      return this.formData.title.length >= 3 &&
        this.formData.tags.length > 0 &&
        this.addedPlaylists.length > 0 &&
        this.formData.userCountMax <= 20
    },
    resetFormAndGo() {
      this.RESET_FORM_DATA();
      this.RESET_ADDED_PLAYLISTS();
      this.$router.go(-1);
    },
    async getPlaylistInfo(playlistId) {
      return await axiosConnector.get(`/playlist/${playlistId}`)
    },
    updateTags: function (tags) {
      this.formData.tags = tags
    },
    onVideoItemClicked ( { id, selected }) {
      return this.formData.playlists.map((playlist) => playlist.videos.map((v) => v.included = (v.id == id ? !selected : v.included)))
    },
    submit() {
      if (!this.isValid()) {
        this.isFormInputError = true
        return;
      }

      this.isSending = true;

      const token = localStorage.getItem('jwt')
      let totalDuration = 0

      this.formData.tags = typeof(this.formData.tags) != String ? this.formData.tags.join() : this.formData.tags;
      this.formData.playlists =
        this.addedPlaylists.reduce((prevPlaylists, curPlaylist) => {
          if (curPlaylist.videos)
          {
            // 현재 플레이리스트에 비디오가 존재한다면
            prevPlaylists[curPlaylist.id] = curPlaylist.videos.reduce((prevVideoIds, curVideo) => {
              if (this.addedPlaylistVideoIds.find(addedPlaylistVideoId => addedPlaylistVideoId == curVideo.videoId))
              {
                prevVideoIds.push(curVideo.videoId)

                const curVideoDuration = curVideo.duration.split(':')
                totalDuration += parseInt(curVideoDuration[0]) * 60 + parseInt(curVideoDuration[1]);
              }

              return prevVideoIds
            }, [])
          }

          // 플레이리스트 셔플
          if (this.isShuffle) curPlaylist.videos.sort(() => Math.random() - 0.5);

          return prevPlaylists
        }, {})

      // '2022-02-06T04:41:08.443Z'

      if (this.autoTime)
      {
        this.startDate = (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10);
        this.startTime = (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(11, 5);
        this.formData.startTime = `${this.startDate}T${this.startTime}:00.000+09:00`

        const endTimeLong = Date.now() - (new Date()).getTimezoneOffset() * 60000 + totalDuration * 1000
        const endTimeCalc = new Date(endTimeLong)
        const endDate = endTimeCalc.toISOString().substr(0, 10)
        const endTime = endTimeCalc.toISOString().substr(11, 5)
        this.formData.endTime = `${endDate}T${endTime}:00.000+09:00`;
      }
      else
      {
        this.formData.startTime = `${this.startDate}T${this.startTime}:00.000+09:00`
        this.formData.endTime = `${this.endDate}T${this.endTime}:00.000+09:00`
      }

      this.formData.inviteIds = this.addedFriends.map(addedFriend => addedFriend.userSeq)

      if (this.$route.params && this.$route.params.id)
      {
        this.updatePlayroom({ formData: this.formData, token })
          .then((res) => {
            this.clearForm()
            this.RESET_FORM_DATA()

            this.$router.push('/playroom/' + res.data.id)
          })
          .catch((err) => {
            return null
          })
      }
      else
      {
        this.createPlayroom({ formData: this.formData, token })
          .then((res) => {
            console.log(res)

            this.clearForm()
            this.RESET_FORM_DATA()

            this.$router.push('/playroom/' + res.data.id)
          })
          .catch((err) => {
            console.log(err)
            return null
          })
      }
    },
    clearForm() {
      this.formData = {
        title: '',
        content: '',
        tags: [],
        isPublic: true,
        inviteIds: [],
        playlists: []
      }
      this.resetAddedPlaylists();
      this.resetAddedFriends();
    },
    saveAndGoPlaylist: function () {
      this.formData.playlists = {}
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlayroomFormPlaylist'})
    },
    saveAndGoFriend: function () {
      this.formData.playlists = {}
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlayroomFormFriend'})
    },
    selectAllVideo () {
      if (!this.numberOfAddedPlaylistVideos == this.numberOfAddedPlaylistSelectedVideos)
        this.selectAllPlaylistVideo()
      else
        this.deselectAllPlaylistVideo()
    },
    // selectAllFriend () {
    //   if (!this.selectedFriends.length)
    //     this.selectAllFriends()
    //   else
    //     this.deselectAllFriends()
    // },
    createPlayroom: function ({formData}) {
      return axiosConnector.post('/playroom', formData)
    },
    updatePlayroom: function ({formData}) {
      return axiosConnector.put(`/playroom/${this.$route.params.id}`, formData)
    },
    ...mapMutations('playroom', [
      'RESET_FORM_DATA'
    ]),
    ...mapMutations('playlist', [
      'RESET_ADDED_PLAYLISTS'
    ]),
    ...mapActions('playroom', [
      'saveFormData'
    ]),
    ...mapActions('playlist', [
      'selectPlaylist2',
      'addPlaylists',
      'selectPlaylistVideo',
      'selectAllPlaylistVideo',
      'deselectAllPlaylistVideo',
      'resetAddedPlaylists'
    ]),
    ...mapActions('account', [
      'validateToken'
    ]),
    ...mapActions('friend', [
      'resetAddedFriends',
      // 'selectAllFriends',
      // 'deselectAllFriends'
    ])
  },
}
</script>

<style>

</style>
