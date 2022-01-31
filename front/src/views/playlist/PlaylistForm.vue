<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between">
      <back :page-name="pageName" />
      <span class="clickable">
        완료
      </span>
    </div>

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
              v-model="title"
              :rules="titleRules"
              :counter="30"
              label="플레이리스트 제목을 입력해주세요."
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
              v-model="content"
              :counter="80"
              label="플레이리스트 소개글을 입력해주세요."
            />
          </v-col>

          <!-- 태그 -->
          <v-col
            cols="12"
            md="4"
            class="py-0"
          >
            <v-text-field
              v-model="content"
              :counter="80"
              label="플레이리스트 태그를 입력해주세요."
            />
          </v-col>

          <!-- 공개 여부 -->
          <v-col class="d-flex justify-space-between">
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
        <v-btn
          color="accent"
          elevation="2"
          rounded
          @click="$router.push({ name: 'PlaylistFormVideo'} )"
        >
          <v-icon>mdi-plus</v-icon>
          <span>영상 추가</span>
        </v-btn>
        <!-- 플레이리스트에 담긴 영상 리스트 -->
        <div class="d-flex justify-space-between">
          <div>
            <v-icon>mdi-check</v-icon>
            <span class="clickable">전체 선택</span>
          </div>
          <div>
            <span>0개 영상 선택</span>
          </div>
        </div>
        <video-list-item-small
          :added-videos="addedVideos"
        />
      </v-container>
    </v-form>
  </div>
</template>

<script>
import { mapState } from 'vuex'

import Back from '../../components/common/Back.vue'
export default {
  name: 'PlaylistForm',
  components: {
    Back,
  },
  data: function() {
    return {
      pageName: "내 플레이리스트 만들기",
      // Create할 때 넘길 데이터
      formData: {
        valid: false,
        title: '',
        content: '',
        tags: '',
        isPublic: true,
        titleRules: [
          v => !!v || '제목은 필수입니다.',
          v => v.length <= 2 || '제목은 3글자 이상 작성해야 합니다.',
        ],
      }
    }
  },
  computed: {
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이리스트를 공개합니다." : "내 플레이리스트를 비공개합니다."
    },
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    }),
  }
}
</script>

<style>

</style>
