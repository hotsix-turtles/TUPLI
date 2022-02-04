<template>
  <v-app>
    <div class="background-login px-5">
      <h1>회원가입 페이지</h1>
      <v-container>
        <router-link
          to="/login"
          class="no-background-hover"
        >
          <v-icon
            color="#5B5C9D"
          >
            mdi-close
          </v-icon>
        </router-link>


        <h2 class="mt-5">
          튜플리에
        </h2>
        <h2 class="mb-5">
          가입하기
        </h2>

        <!-- 상태바 -->
        <v-img
          src="../../assets/signup_bar1.png"
          alt="logo"
        />

        <!-- 회원가입 form -->
        <div class="mt-4 pt-4">
          <v-container>
            <v-row>
              <v-checkbox
                v-model="checkbox1"
                :rules="checkRules"
                class="pt-0"
                label="[필수] 개인정보 이용약관 동의"
                required
              />

              <v-icon
                color="#5B5C9D"
              >
                mdi-domain
              </v-icon>
            </v-row>
          </v-container>

          <div>
            <v-checkbox
              v-model="checkbox2"
              :rules="checkRules"
              class="pt-0"
              label="[필수] 서비스 이용약관 동의"
              required
            />
            <v-icon
              color="#5B5C9D"
            >
              mdi-next
            </v-icon>
          </div>

          <h5 class="mx-5">
            약관 동의 체크는 해당 약관을 모두 숙지히였으며, 이에 동의함을 의미합니다.
          </h5>

          <v-btn
            to="/signup2"
            class="white--text my-5"
            color="#5B5C9D"
            block
            elevation="0"
            rounded
          >
            다음
          </v-btn>
        </div>
      </v-container>
    </div>
  </v-app>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Signup',

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
    valid: true,

    checkRules: [
      v => !!v || '약관 동의는 필수 선택 사항입니다.'
    ],

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
    login: function () {
      axios({
        method: 'post',
        url: '',
        data: this.credentials,
      })
        .then(res => {
          console.log(res)
          localStorage.setItem('jwt', res.data.token)
          this.$emit('login')
          this.$router.push({ name: 'empty_main' })
        })
        .catch(err => {
          console.log(err)
        })
    },

    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
    },
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
    background-color: #5B5C9D;
  }

  .no-background-hover {
   text-decoration: none !important;
  }

</style>
