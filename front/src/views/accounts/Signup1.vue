<template>
  <v-app>
    <div class="background-login px-5">
      <!-- <h1>회원가입 페이지</h1> -->
      <v-container class="d-flex flex-column">
        <v-icon
          color="#5B5C9D"
          class="ml-0 mr-auto mt-5"
          @click="$router.push({ name: 'Login' })"
        >
          mdi-close
        </v-icon>

        <img
          class="mt-5"
          src="@/assets/tupli_logo2_dark.png"
          alt=""
          style="width: 30px; height:auto;"
        >
        <h2 class="mt-5 mb-5">
          튜플리에 <br>
          가입하기
        </h2>

        <!-- 상태바 -->
        <v-img
          src="../../assets/signup_bar1.png"
          alt="logo"
        />

        <!-- 회원가입 form -->
        <div class="mt-4 pt-4">
          <div class="d-flex align-center">
            <v-checkbox
              v-model="checkbox1"
              :rules="checkRules"
              class="pt-0"
              label="[필수] 개인정보 이용약관 동의"
              required
            />

            <v-icon
              color="#5B5C9D"
              class="ml-2 mb-1"
              @click="$router.push({ name: 'PrivateTerms' })"
            >
              mdi-chevron-right
            </v-icon>
          </div>

          <div class="d-flex align-center">
            <v-checkbox
              v-model="checkbox2"
              :rules="checkRules"
              class="pt-0"
              label="[필수] 서비스 이용약관 동의"
              required
            />
            <v-icon
              class="ml-2 mb-1"
              color="#5B5C9D"
              @click="$router.push({ name: 'ServiceTerms' })"
            >
              mdi-chevron-right
            </v-icon>
          </div>

          <h5 class="mx-6">
            약관 동의 체크는 해당 약관을 모두 숙지히였으며, <br>
            이에 동의함을 의미합니다.
          </h5>

          <v-btn
            class="white--text my-5"
            color="#5B5C9D"
            block
            elevation="0"
            rounded
            @click="checkTerms"
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
import Signup2Vue from './Signup2.vue'

export default {
  name: 'Signup',

  // 이메일 비밀번호 규칙 설정
  data: () => ({
    // 로그인 값
    credentials: {
      email: null,
      password: null,
    },

    checkbox1: false,
    checkbox2: false,

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
          console.log(err.response.data)
        })
    },


    // 약관 동의
    checkTerms: function() {
      if (this.checkbox1 === true && this.checkbox2 === true) {
        this.$router.push({ name: 'Signup2' })
      }
      else {
        console.log('약관에 동의해주세요')
      }

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
