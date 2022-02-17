<template>
  <div>
    <div class="background-login">
      <div class="px-5">
        <div>
          <v-container class="mb-5">
            <div
              class="align-center mt-5 d-flex"
            >
              <v-icon
                color="#5B5C9D"
                @click="$router.push('/login')"
              >
                mdi-chevron-left
              </v-icon>
              <p class="back-menu-text">
                비밀번호 찾기
              </p>
            </div>
          </v-container>




          <!-- 회원가입 form -->
          <div class="mt-4 pt-4 px-6">
            <v-form ref="form">
              <h3
                class="mt-2"
              >
                이메일
              </h3>
              <v-text-field
                v-model="credentials.email"
                required
                :rules="emailRules"
                label="이메일을 입력해주세요"
              />

              <h3 class="mt-5">
                닉네임
              </h3>
              <v-text-field
                v-model="credentials.nickname"
                required
                :rules="[nicknameRules.input, nicknameRules.max]"
                type=""
                label="닉네임"
                @keydown.enter="onInputKeyword"
              />
            </v-form>

            <p
              style="font-size: 11px; color: dark-gray;"
              class="mt-3"
            >
              * 해당 이메일로 랜덤 비밀번호를 보내드립니다.
            </p>

            <v-btn
              class="white--text my-6"
              color="#5B5C9D"
              block
              required
              elevation="0"
              rounded
              large
              @click="findPassword"
            >
              비밀번호 찾기
            </v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosConnector from '../../utils/axios-connector'
export default {
  name: 'FindPassword',
  data: function() {
    return{
      credentials: {
        email: '',
        nickname: '',
      },
      emailRules: [
        v => !!v || '이메일을 입력해주세요.',
        v => /.+@.+\..+/.test(v) || '올바른 형식의 이메일을 입력해주세요.',
      ],

      nicknameRules: {
        input: v => !!v || '닉네임을 입력해주세요.',
        max: v => v.length <= 7 || '7글자 내의 닉네임을 입력해주세요.',
      },
    }
  },
  methods: {

    findPassword: function() {
      axiosConnector.post(`/account/passwordFind/${this.credentials.email}/nickname/${this.credentials.nickname}`)
        .then((res) => {
          console.log(res)
        })
        .catch((err) => {
          console.log('에러', err)
        })
      this.$router.push({name: 'Login'})
    },

    onInputKeyword: function() {
      this.requestLogin()
    }

  }
}
</script>

<style>

</style>
