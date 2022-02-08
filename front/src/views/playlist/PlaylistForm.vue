<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between fixed-top light-background">
      <back :page-name="pageName" />
      <div
        class="clickable"
        @click="onClickCompletion"
      >
        완료
      </div>
    </div><br><br>

    <!-- 플레이리스트 생성 폼 -->
    <v-form v-model="valid">
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
            class="d-flex justify-space-between py-0"
            cols="12"
            md="4"
          >
            <div class="d-flex">
              <span>공개 설정</span>
              <span class="font-4">
                {{ isPublicMsg }}
              </span>
            </div>
            <v-switch
              v-model="formData.isPublic"
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
          <div @click="onClickSelectAll">
            <v-icon>mdi-check</v-icon>
            <span class="clickable">전체 선택</span>
          </div>
          <div>
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
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

import Back from '../../components/common/Back.vue'
import RemoveButtonBottom from '../../components/playlist/RemoveButtonBottom.vue'
import TagInput from '../../components/common/TagInput.vue'
import VideoListItemSmall from '../../components/video/VideoListItemSmall.vue'
export default {
  name: 'PlaylistForm',
  components: {
    Back,
    VideoListItemSmall,
    TagInput,
    RemoveButtonBottom,
  },
  data: function() {
    return {
      pageName: "내 플레이리스트 만들기",
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
    }
  },
  computed: {
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
  created: function () {
    if (this.isSaved) {
      this.formData = this.savedFormData
    } else {
      this.resetVideoAddState()
    }
  },
  methods: {
    ...mapActions('playlist', [
      'createPlaylist',
      'saveFormData',
    ]),
    ...mapActions('video', [
      'selectAllAddedVideos',
      'deselectAllAddedVideos',
      'resetVideoAddState',
    ]),
    updateTags: function (tags) {
      this.formData.tags = tags
    },
    onClickCompletion: function () {
      if (this.valid && this.addedVideos.length > 0) {
        const data = {
          title: this.formData.title,
          content: this.formData.content,
          tags: this.formData.tags.join(),
          isPublic: this.formData.isPublic,
          videos: this.addedVideos,
        }
        this.createPlaylist(data)
        setTimeout(() => {this.resetVideoAddState()}, 1000)
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
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlaylistFormVideo'})
    }
  },
}
</script>

<style>

</style>
