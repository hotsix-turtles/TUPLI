<template>
  <v-app>
    <div class="background-login px-5">
      <h1>로그인 페이지</h1>
      <v-container>
        <h2 class="mt-5">
          튜플리에서
        </h2>
        <h2 class="mb-5">
          우리 같이 유튜브 볼래?
        </h2>

        <!-- 로그인 form -->
        <div class="mt-4 pt-4">
          <v-form ref="form">
            <v-text-field
              v-model="credentials.email"
              class="pt-0"
              :rules="emailRules"
              label="이메일을 입력해주세요"
              required
            />

            <v-text-field
              v-model="credentials.password"
              class="pt-0"
              type="password"
              :rules="[passwordRules.min]"
              label="비밀번호를 입력해주세요"
              required
            />
          </v-form>

          <v-btn
            class="white--text my-5"
            color="#5B5C9D"
            block
            elevation="0"
            rounded
            @click="requestLogin"
          >
            로그인
          </v-btn>
        </div>
      </v-container>

      <!-- 소셜 로그인 -->
      <v-container>
        <div class="align-center my-4">
          <v-row class="justify-center">
            <v-col col>
              <v-row class="justify-center mb-2">
                <p>-------------------</p>
                <p>또는</p>
                <p>-------------------</p>
              </v-row>
              <v-row class="justify-center">
                <v-btn
                  fab
                  elevation="0"
                  class="mx-3"
                  :href="socialLoginUrl('google')"
                  :loading="loading"
                  :disabled="loading"
                  @click="loader = 'loading'"
                >
                  구글
                </v-btn>

                <v-btn
                  fab
                  elevation="0"
                  class="mx-3"
                >
                  구글
                </v-btn>

                <v-btn
                  fab
                  elevation="0"
                  class="mx-3"
                >
                  구글
                </v-btn>
              </v-row>
            </v-col>
          </v-row>
        </div>
      </v-container>

      <!-- 그 외 -->
      <v-container>
        <div class="row justify-center mb-4 mt-1">
          <router-link
            to="/signup2"
            class="no-background-hover-text"
          >
            <p class="mx-1">
              회원가입
            </p>
          </router-link>
          <p class="mx-1">
            |
          </p>
          <p class="mx-1">
            이메일 찾기
          </p>
          <p class="mx-1">
            |
          </p>
          <p class="mx-1">
            비밀번호 찾기
          </p>
        </div>
      </v-container>
    </div>
  </v-app>
</template>

<script>
import { mapActions } from 'vuex'
import SERVER from '@/api/server.js'

export default {
  name: 'Login',

  // 이메일 비밀번호 규칙 설정
  data: () => ({
    // 로그인 값
    credentials: {
      email: null,
      password: null,
    },

    // 로딩 아이콘
    loader: null,
    loading: false,

    // 유효성 검사
    valid: false,

    email: '',
    emailRules: [
      v => !!v || '이메일을 입력해주세요.',
      v => /.+@.+\..+/.test(v) || '올바른 형식의 이메일을 입력해주세요.',
    ],

    password: '',
    passwordRules: {
      min: v => v.length >= 8 || '올바른 비밀번호를 입력해주세요.',
    }
  }),

  watch: {
    //로딩 애니메이션
    loader () {
      const l = this.loader
      this[l] = !this[l]

      setTimeout(() => (this[l] = false), 3000)

      this.loader = null
    },
  },

  methods: {
    // 로그인
    requestLogin: function () {
      this.login(this.credentials)
      this.valid = true
    },
    ...mapActions([
      'login',
    ]),

    socialLoginUrl: function(socialType){
      // const BACKEND_PORT = SERVER.BACKEND_PORT === null ? '' : `:${SERVER.BACKEND_PORT}`
      // const BACKEND_URL = "https://i6a102.p.ssafy.io/api/v1"
      // const FRONTEND_PORT = SERVER.FRONTEND_PORT === null ? '' : `:${SERVER.FRONTEND_PORT}`
      // const REDIRECT_URI = `${location.protocol}//${location.hostname}${FRONTEND_PORT}/oauth/redirect`
      // console.log(`${BACKEND_URL}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`)
      return `https://i6a102.p.ssafy.io/api/v1/oauth2/authorization/${socialType}?redirect_uri=https://i6a102.p.ssafy.io/oauth/redirect`
    }

  },

  metaInfo () {
    return {
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' }
      ]
    }
  },

}
</script>

<style scoped>
  .background-login {
    background-color: #F1F1F4;
    height: 844px;
  }

  .btn-login {
    /* background-color: #D7D7E8; */
    background-color: #5B5C9D;

  }

  .custom-loader {
    animation: loader 1s infinite;
    display: flex;
  }

  @keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }

  .no-background-hover-text {
    text-decoration: none !important;
    text-decoration-color: black !important;
  }
</style>

