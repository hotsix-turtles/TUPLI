<template>
  <div>
    <!-- 뒤로가기/완료 or 수정 -->
    <div class="d-flex justify-space-between fixed-top light-background">
      <div class="d-flex mx-3 my-3">
        <div>
          <v-icon @click="checkResetData">
            mdi-arrow-left
          </v-icon>
        </div>
        <div class="font-2 semi-bold center">
          {{ pageName }}
        </div>
      </div>
      <div
        v-if="formType === 'create'"
        class="clickable font-2 semi-bold mt-3 mr-3 color-dark-gray"
        :class="{ 'color-main': valid && addedVideos.length > 0 }"
        @click="onClickCompletion"
      >
        완료
      </div>
      <div
        v-else
        class="clickable font-2 semi-bold mt-3 mr-3 color-dark-gray"
        :class="{ 'color-main': valid && addedVideos.length > 0 }"
        @click="onClickCompletion"
      >
        수정
      </div>
    </div><br><br>

    <!-- 플레이리스트 생성 폼 -->
    <v-form
      v-model="valid"
      class="mt-5 mx-1"
    >
      <v-container>
        <v-row>
          <!-- 제목 -->
          <v-col
            cols="12"
            md="4"
            class="py-0"
          >
            <v-text-field
              v-model="formData.title"
              :rules="[rules.required, rules.counterMin3, rules.counterMax30]"
              :counter="30"
              label="플레이리스트 제목"
              required
              color="accent"
            />
          </v-col>

          <!-- 소개글 -->
          <v-col
            cols="12"
            md="4"
            class="py-0"
          >
            <v-text-field
              v-model="formData.content"
              :rules="[rules.counterMax80]"
              :counter="80"
              label="플레이리스트 소개글"
              color="accent"
            />
          </v-col>

          <!-- 태그 -->
          <v-col
            cols="12"
            md="4"
            class="py-0"
          >
            <tag-input
              :proped-tags="formData.tags"
              @tag-input="updateTags"
            />
          </v-col>

          <!-- 공개 여부 -->
          <v-col
            class="d-flex justify-space-between py-0 mb-5"
            cols="12"
            md="4"
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

        <!-- 플레이리스트 영상 리스트 조작 관련 -->
        <div class="d-flex justify-space-between">
          <v-btn
            color="accent"
            elevation="2"
            rounded
            @click="saveAndGo"
          >
            <v-icon>mdi-plus</v-icon>
            <span>영상 추가</span>
          </v-btn>
          <div v-if="checkVideoList && addedVideos.length === 0">
            <span
              class="font-4"
              style="color: red;"
            >영상은 1개 이상 선택해주세요.</span>
          </div>
        </div>

        <div class="d-flex justify-space-between">
          <!-- 전체 선택 -->
          <div
            class="mt-5 mb-3"
            @click="onClickSelectAll"
          >
            <v-icon>mdi-check</v-icon>
            <span class="clickable">전체 선택</span>
          </div>
          <div class="mt-5">
            <span>총 {{ addedVideos.length }}개 영상</span>
          </div>
        </div>
        <!-- 플레이리스트에 담긴 영상 리스트 -->
        <video-list-item-small
          :videos="addedVideos"
        />
      </v-container>
    </v-form><br><br>

    <!-- 하단 리스트에 삭제하기 버튼 -->
    <remove-button-bottom />
    <!-- 데이터 저장 여부 확인 모달 -->
    <normal-dialog
      title="입력값"
      content-html="기존 입력 데이터가 사라집니다."
      max-width="290"
      persistent
      :buttons="[{ name: '취소', color: 'gray' }, { name: '확인', color: '#5B5C9D' }]"
      :show="isReset"
      @button-click="onClickSaveDataDialog"
    />
    <!-- 플레이룸 생성 모달 -->
    <normal-dialog
      title="생성하기"
      content-html="이 플레이리스트로 플레이룸을 생성하시겠습니까?"
      max-width="290"
      persistent
      :buttons="[{ name: '플레이룸 생성', color: 'black' }, { name: '플레이리스트만 생성', color: 'black' }]"
      :show="createPlayroom"
      @button-click="onClickCreatePlayroomDialog"
    />
    <!--
        생성 지연시 로딩바
      -->
    <v-dialog
      v-model="isCreating"
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
          플레이리스트 생성 중...
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          />
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog
      v-model="isUpdating"
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
          플레이리스트 수정 중...
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          />
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

import Back from '../../components/common/Back.vue'
import RemoveButtonBottom from '../../components/playlist/RemoveButtonBottom.vue'
import TagInput from '../../components/common/TagInput.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
import NormalDialog from '../../components/common/NormalDialog.vue';
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'PlaylistForm',
  components: {
    VideoListItemSmall,
    TagInput,
    RemoveButtonBottom,
    NormalDialog,
  },
  data: function() {
    return {
      formType: 'create',  // 생성하기/수정하기
      isSelectedAll: false,
      // Create할 때 넘길 데이터
      formData: {
        title: '',
        content: '',
        tags: [],
        isPublic: true,
        videos: [],
      },
      valid: false,
      checkVideoList: false,
      rules: {
        required: v => !!v || '필수 입력값입니다.',
        counterMin3: v => v.length >= 3 || '제목은 3글자 이상 작성해야 합니다.',
        counterMax30: v => v.length <= 30 || '제목은 30글자 이상 작성할 수 없습니다.',
        counterMax80: v => v.length <= 80 || '소개글은 80글자 이상 작성할 수 없습니다.',
      },
      createPlayroom: false,
      isCreating: false,
      isUpdating: false,
      isReset: false,
    }
  },
  computed: {
    pageName () {
      return this.formType == 'create' ? "플레이리스트 생성" :
        this.formType == 'update' ? "플레이리스트 정보 변경" : null
    },
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이리스트를 공개합니다." : "내 플레이리스트를 비공개합니다."
    },
    ...mapState('video', {
      addedVideos: state => state.addedVideos,
    }),
    ...mapState('playlist', {
      savedFormData: state => state.savedFormData,
      isSaved: state => state.isSaved,
    }),
  },
  // 뒤로가기할 때 저장한 데이터 삭제돼도 괜찮은지 확인 후 처리 로직 필요
  created: function () {

    console.log('this.$route', this.$route)
    if (this.isSaved) {
      this.formData = this.savedFormData
    } else {
      this.resetVideoAddState()
    }
    if (this.$route.params.playlistId === 'video' || typeof this.$route.params.playlistId === 'undefined') {
      console.log('create 완료 페이지')
      this.formType = 'create'
    } else {
      console.log('update 수정 페이지', this.$route.params.playlistId)
      this.formType = 'update'
    }
  },
  methods: {
    ...mapActions('playlist', [
      'createPlaylist',
      'updatePlaylist',
      'saveFormData',
      'resetFormData',
      // 'createPlaylistBeforePlayroom',
    ]),
    ...mapActions('video', [
      'selectAllAddedVideos',
      'deselectAllAddedVideos',
      'resetVideoAddState',
    ]),
    ...mapActions('playroom', [
      'savePlaylistData',
    ]),
    updateTags: function (tags) {
      this.formData.tags = tags
    },
    onClickCompletion: function () {
      if (this.valid && this.addedVideos.length > 0) {
        if (this.formType === 'create') {
          this.createPlayroom = true
        } else {
          this.isUpdating = true
          const data = {
            title: this.formData.title,
            content: this.formData.content,
            tags: this.formData.tags.join(),
            isPublic: this.formData.isPublic,
            videos: this.addedVideos,
          }
          const params = {
            id: this.$route.params.playlistId,
            formData: data,
          }
          this.updatePlaylist(params)
          setTimeout(() => {this.resetVideoAddState()}, 1000)
        }
      } else {
        this.checkVideoList = true
      }
    },
    onClickSelectAll: function () {
      if (this.isSelectedAll) {
        this.deselectAllAddedVideos()
      } else {
        this.selectAllAddedVideos()
      }
      this.isSelectedAll = !this.isSelectedAll
    },
    saveAndGo: function () {
      console.log('this.formData', this.formData)
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlaylistFormVideo'})
    },
    onClickCreatePlayroomDialog: function (idx) {
      if (idx === 0) { // 확인
        console.log('onClickCreatePlayroomDialog')
        this.createPlayroom = false
        const data = {
          title: this.formData.title,
          content: this.formData.content,
          tags: this.formData.tags.join(),
          isPublic: this.formData.isPublic,
          videos: this.addedVideos,
        }
        axiosConnector.post('/playlist',
          data
        )
          .then((res) => {
            console.log('onClickCreatePlayroomDialog', res)
            const playlistId = res.data.id
            const videoIds = data.videos.map((video) => {
              return video.videoId
            })
            const playlists = {
              [playlistId]: videoIds
            }
            data.playlists = playlists
            data.tags = data.tags.split(',')
            this.savePlaylistData(data)
          })
        console.log('확인')
      } else { // 취소
        this.isCreating = true
        this.createPlayroom = false
        const data = {
          title: this.formData.title,
          content: this.formData.content,
          tags: this.formData.tags.join(),
          isPublic: this.formData.isPublic,
          videos: this.addedVideos,
        }
        this.createPlaylist(data)
        console.log('취소')
      }
      setTimeout(() => {this.resetVideoAddState()}, 1000)
    },
    // 데이터 리셋 여부 확인
    checkResetData: function () {
      if (this.formData.title || this.formData.content || this.formData.tags.length > 0 || this.addedVideos.length > 0 ) {
        console.log(this.formData.title)
        console.log(this.formData.content)
        console.log(this.formData.tags)
        console.log(this.addedVideos)
        this.isReset = true
      } else {
        this.$router.go(-1)
      }
    },
    onClickSaveDataDialog: function (idx) {
      if (idx === 0) { // 확인
        this.$router.go(-1)
        this.resetVideoAddState()
        this.resetFormData()
      } else { // 취소
        this.isReset = false
      }
    }
  },
}
</script>

<style>
</style>
