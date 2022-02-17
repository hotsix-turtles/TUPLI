<template>
  <body>
    <div
      class="background-login"
    >
      <div
        class="d-flex justify-center align-center px-5"
        style="height:822px;"
      >
        <div>
          <img
            src="@/assets/tupli_logo_dark.png"
            width="40%"
            alt="tupli logo"
          >
          <h4>
            너와 나의 유튜브 취향을 꽃피우다
          </h4>
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
                :rules="emailRules"
                label="이메일을 입력해주세요"
                required
              />

              <v-text-field
                v-model="credentials.password"
                class=""
                type="password"
                :rules="[passwordRules.min]"
                label="비밀번호를 입력해주세요"
                required
                @keydown.enter="onInputKeyword"
              />
            </v-form>

            <v-btn
              class="white--text my-5"
              color="#5B5C9D"
              block
              elevation="0"
              rounded
              width="50px"
              @click="requestLogin"
            >
              로그인
            </v-btn>
          </div>

          <!-- 소셜 로그인 -->
          <v-container>
            <div class="align-center my-4">
              <v-row class="justify-center">
                <v-col col>
                  <v-row class="justify-center mb-2">
                    <p>----------</p>
                    <p>또는</p>
                    <p>----------</p>
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
                  </v-row>
                </v-col>
              </v-row>
            </div>
          </v-container>

          <!-- 그 외 -->
          <div>
            <div class="row justify-center mt-1">
              <p
                class="mx-1"
                @click="$router.push({ name: 'Signup2' })"
              >
                회원가입
              </p>
              <p class="mx-1">
                |
              </p>
              <p
                class="mx-1"
                @click="findPassword"
              >
                비밀번호 찾기
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Login',

  // 이메일 비밀번호 규칙 설정
  data: () => ({
    // 로그인 값
    credentials: {
      email: '',
      password: '',
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
      min: v => v.length >= 4 || '올바른 비밀번호를 입력해주세요.',
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
      // const BACKEND_URL = "https://tupli.kr/api/v1"
      // const FRONTEND_PORT = SERVER.FRONTEND_PORT === null ? '' : `:${SERVER.FRONTEND_PORT}`
      // const REDIRECT_URI = `${location.protocol}//${location.hostname}${FRONTEND_PORT}/oauth/redirect`
      // console.log(`${BACKEND_URL}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`)
      return `https://tupli.kr/api/v1/oauth2/authorization/${socialType}?redirect_uri=https://tupli.kr/oauth/redirect`
    },

    findPassword: function() {
      this.$router.push({ name: 'FindPassword' })
    },

    onInputKeyword: function() {
      this.requestLogin()
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
    width: auto;
  }

  .backgrounds-login {
    background-color: #F1F1F4;
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


</style>

