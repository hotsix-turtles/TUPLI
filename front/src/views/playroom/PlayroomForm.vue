<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between">
      <back :page-name="pageName" />
      <v-btn
        class="clickable"
        text
        @click="submit"
      >
        완료
      </v-btn>
    </div>

    <!-- 플레이룸 생성 폼 -->
    <v-form v-model="isValid">
      <v-container>
        <!-- 제목 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="formData.title"
              :rules="titleRules"
              :counter="30"
              label="플레이룸 제목을 입력해주세요."
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

        <!-- 공개 여부 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
            class="d-flex flex-row justify-space-between"
          >
            <p class="font-3">
              공개 여부
            </p>
            <p class="font-4 ml-1 mr-auto">
              {{ isPublicMsg }}
            </p>
            <v-switch
              v-model="formData.isPublic"
            />
          </v-col>
        </v-row>

        <!-- 친구 초대 레이블 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between"
          >
            <div>
              <p class="font-3">
                친구 초대
              </p>
              <p class="font-4 ml-1 mr-auto">
                친구에게 플레이룸 시작 전 알림을 보냅니다.
              </p>
            </div>

            <!-- 친구 초대 버튼 -->
            <div
              class="py-0 mr-0"
            >
              <v-btn
                small
                @click="saveAndGoFriend"
              >
                <v-icon color="black">
                  mdi-plus
                </v-icon>
                <span style="color: black;">친구 추가</span>
              </v-btn>
            </div>
          </v-col>
        </v-row>

        <!-- 친구 초대 리스트 조작 버튼 -->
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
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4">
              {{ numberOfFriend }}명 유저 선택
            </p>
          </v-col>
        </v-row>

        <!-- 친구 초대 리스트 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="pt-0"
          >
            <v-expansion-panels class="mx-auto">
              <v-expansion-panel
                class="py-2"
                elevation="0"
              >
                <v-expansion-panel-header>
                  초대할 친구가 없습니다
                </v-expansion-panel-header>
                <v-expansion-panel-content
                  v-for="(inviteId, idx) in formData.inviteIds"
                  :id="idx"
                  :key="idx"
                >
                  {{ inviteId }}
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
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
              <p class="font-3">
                플레이리스트 구성
              </p>
              <p class="font-4 ml-1 mr-auto">
                원하는 플레이리스트를 검색하고 추가하여<br> 나만의 플레이리스트를 구성할 수 있습니다.
              </p>
            </div>

            <!-- 플레이리스트 추가 버튼 -->
            <div
              class="py-0 mr-0"
            >
              <v-btn
                small
                @click="saveAndGoPlaylist"
              >
                <v-icon color="black">
                  mdi-plus
                </v-icon>
                <span style="color: black;">플레이리스트 추가</span>
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
              @click="selectAllVideo"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4">
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

        <!-- 플레이룸 시간 설정 레이블 -->
        <v-row
          class="h-100"
        >
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between"
          >
            <p class="font-3">
              시간 설정
            </p>
            <p class="font-4 ml-1 mr-auto">
              플레이룸을 운영할 시간을 지정합니다.
            </p>
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
      </v-container>
    </v-form>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import TagInput from '../../components/common/TagInput.vue'
import { mapMutations, mapActions } from 'vuex'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'PlaylistForm',
  components: {
    Back,
    TagInput,
    PlaylistListItemSmall
  },
  data: function() {
    return {
      pageName: "내 플레이룸 만들기",
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
        v => this.startDateTime.getTime() < this.endDateTime.getTime() || '종료날짜은 시작날짜보다 이후이어야 합니다',
      ],
      endTimeRules: [
        v => !!v || '종료시간은 필수입니다.',
        v => this.startDateTime.getTime() < this.endDateTime.getTime() || '종료시간은 시간시간보다 이후이어야 합니다',
      ],
      isValid: false,
      // Create할 때 넘길 데이터
      formData: {
        title: '',
        content: '',
        tags: [],
        isPublic: true,
        inviteIds: [],
        playlists: []
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
    }
  },
  computed: {
    computedDateFormatted () {
      return this.formatDate(this.startDate)
    },
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이룸을 공개합니다." : "내 플레이룸을 비공개합니다."
    },
    numberOfFriend () {
      console.log(this.formData, this.formData.inviteIds)
      return this.formData.inviteIds.length
    },
    ...mapState('playlist', ['addedPlaylists', 'addedPlaylistVideoIds']),
    ...mapState('playroom', ['savedFormData']),
    ...mapGetters('playlist', ['numberOfAddedPlaylists', 'numberOfAddedPlaylistSelectedVideos', 'numberOfAddedPlaylistVideos'])
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
  created: function () {
    if (this.savedFormData) {
      console.log('restoreData', this.savedFormData)
      this.formData = this.savedFormData
    }
  },
  methods: {
    updateTags: function (tags) {
      this.formData.tags = tags
    },
    onVideoItemClicked ( { id, selected }) {
      return this.formData.playlists.map((playlist) => playlist.videos.map((v) => v.included = (v.id == id ? !selected : v.included)))
    },
    submit() {
      // TODO: 원래 axiosConnector에서 알아서 갱신하고 보내야하지만...
      const token = localStorage.getItem('jwt')

      this.formData.tags = this.formData.tags.join();
      this.formData.playlists =
        this.addedPlaylists.reduce((prevPlaylists, curPlaylist) => {
          if (curPlaylist.videos)
          {
            // 현재 플레이리스트에 비디오가 존재한다면
            prevPlaylists[curPlaylist.id] = curPlaylist.videos.reduce((prevVideoIds, curVideo) => {
              if (this.addedPlaylistVideoIds.find(addedPlaylistVideoId => addedPlaylistVideoId == curVideo.videoId))
                prevVideoIds.push(curVideo.videoId)
              return prevVideoIds
            }, [])
          }
          return prevPlaylists
        }, {})

      // '2022-02-06T04:41:08.443Z'
      this.formData.startTime = `${this.startDate}T${this.startTime}:00.000+09:00`
      this.formData.endTime = `${this.endDate}T${this.endTime}:00.000+09:00`
      console.log(this.formData)

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
      this.resetAddedPlaylists()
    },
    saveAndGoPlaylist: function () {
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlayroomFormPlaylist'})
    },
    saveAndGoFriend: function () {
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlayroomFormFriend'})
    },
    selectAllVideo () {
      if (!this.numberOfAddedPlaylistVideos == this.numberOfAddedPlaylistSelectedVideos)
        this.selectAllPlaylistVideo()
      else
        this.deselectAllPlaylistVideo()
    },
    createPlayroom: function ({formData, token}) {
      return axiosConnector.post('/playroom', formData)
    },
    ...mapMutations('playroom', ['RESET_FORM_DATA']),
    ...mapActions('playroom', ['saveFormData']),
    ...mapActions('playlist', ['selectAllPlaylistVideo', 'deselectAllPlaylistVideo', 'resetAddedPlaylists'])
  },
}
</script>

<style>

</style>
