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
      formData.append('nickname', this.credentials.newNickname)
      formData.append('introduction', this.credentials.newIntroduction)
      axios({
        method: 'PUT',
        headers: {Authorization: this.authToken},
        url: SERVER.URL + '/profile',
        data: formData        
      })
        .then((response) => {
          this.$router.push({ name: 'Profile' })
          // 필요하면 자체 갱신
          // this.nickname = response.data.email
          // state 갱신
          this.$store.commit('UPDATE_PROFILE', formData)
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
