<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between">
      <back :page-name="pageName" />
      <p class="clickable">
        완료
      </p>
    </div>

    <!-- 플레이리스트 생성 폼 -->
    <v-form v-model="valid">
      <v-container>
        <v-row>
          <!-- 제목 -->
          <v-col
            cols="12"
            md="4"
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
              <p>공개 설정</p>
              <p class="font-4">
                {{ isPublicMsg }}
              </p>
            </div>
            <v-switch
              v-model="formData.isPublic"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </div>
</template>

<script>
import Back from '../../components/Back.vue'
export default {
  name: 'PlaylistForm',
  components: {
    Back
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
  }
}
</script>

<style>

</style>
