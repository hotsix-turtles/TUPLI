<template>
  <v-app>
    <h1>프로필 편집</h1>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5 justify-space-between"
      >
        <div class="d-flex">
          <v-icon
            size="30"
            color="#5B5C9D"
            @click="$router.push({ name: 'Profile' })"
          >
            mdi-chevron-left
          </v-icon>
          <h3>
            프로필 편집
          </h3>
        </div>

        <div>
          <v-btn
            class="d-flex justify-end p-5 mx-5"
            small
            rounded
            outlined
            color="#5B5C9D"
            depressed
            @click="updateProfile"
          >
            변경
          </v-btn>
        </div>
      </v-row>
      <v-container>
        <div class="d-flex flex-column align-center">
          <img
            class="py-3"
            src="../../assets/logo_semi.png"
            alt=""
            width="45px"
            fab
          >
          <!-- 사진 업로드용 임시 -->
          <div class="update-modal mb-3">
            <label for="profile-photo" />
            <input
              type="file"
              @change="getNewImage"
            >
          </div>
        </div>
        <div class="d-flex flex-column justify-center pt-3">
          <v-form>
            <div class="flex-column align-start mx-3">
              <p class="mr-2 mb-0">
                닉네임
              </p>
              <v-text-field
                v-model="credentials.newNickname"
                class="pt-0"
                hint="새로운 닉네임을 입력해주세요."
              />
              <!-- label="김춘식" -->
            </div>
            <div class="flex-column align-start mx-3 mt-5">
              <p class="mr-2 mb-0">
                자기소개 글
              </p>
              <v-text-field
                v-model="credentials.newIntroduction"
                class="pt-0"
                hint="자기소개 글을 입력해주세요."
              />
              <!-- label="자기소개 글을 입력해주세요." -->
            </div>
          </v-form>
        </div>
      </v-container>
    </v-container>
  </v-app>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'

import axiosConnector from '@/utils/axios-connector'

export default {
  name: 'EditProfile',
  data: function() {
    return {
      credentials: {
        newNickname: '',
        newIntroduction:'',
        newImage: ''
      }
    }
  },
  computed: {
    ...mapState(['nickname', 'introduction', 'image', 'authToken'])
  },
  created() {
    this.init()
  },
  methods: {
    init: function() {
      this.credentials.newNickname = this.nickname
      this.credentials.newIntroduction = this.introduction
      this.credentials.newImage = ''
    },
    updateProfile: function(){
      const formData = new FormData()
      formData.append('image', this.credentials.newImage)
      // 이대로면 DB에서 내용물 읽을 수 없어지지만, 어떠한 환경에서도 UTF-8이 보장된다.
      // 물론 서버에서 처리하면 서버와 DB에서도 읽을 수 있다.
      formData.append('nickname', encodeURIComponent(this.credentials.newNickname))
      formData.append('introduction', encodeURIComponent(this.credentials.newIntroduction))
      // axios({
      //   method: 'PUT',
      //   headers: {Authorization: this.authToken},
      //   url: SERVER.URL + '/profile',
      //   data: formData
      // })
      axiosConnector.put(`/profile`,
        formData
      )
        .then((res) => {
          this.$router.push({ name: 'MyProfile' })
          // state 갱신
          this.$store.commit('UPDATE_PROFILE', res.data)
        })
        .catch((err) => console.log(err.response.data))
    },
    getNewImage: function(event) {
      this.credentials.newImage = event.target.files[0]
    },
  }
}
</script>

<style>

</style>
