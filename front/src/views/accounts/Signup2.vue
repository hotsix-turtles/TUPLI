<template>
  <v-app>
    <div class="background-login px-5">
      <h1>회원가입 페이지</h1>
      <v-container>
        <v-container class="mb-5">
          <v-row
            class="align-center mt-5"
          >
            <router-link
              to="/signup"
              class="no-background-hover"
            >
              <v-icon
                color="#5B5C9D"
              >
                mdi-chevron-left
              </v-icon>
            </router-link>
            <h4 class="">
              회원정보
            </h4>
          </v-row>
        </v-container>

        <!-- 상태바 -->
        <v-img
          src="../../assets/signup_bar2.png"
          alt="logo"
        />


        <!-- 회원가입 form -->
        <div class="mt-4 pt-4">
          <v-form ref="form">
            <h3>이메일</h3>
            <v-text-field
              v-model="credentials.email"
              class="pt-0"
              label="이메일을 입력해주세요"
            />

            <h3>비밀번호</h3>
            <v-text-field
              v-model="credentials.password"
              class="pt-0"
              type="password"
              label="비밀번호"
            />

            <v-text-field
              v-model="credentials.passwordCheck"
              class="pt-0"
              type="password"
              label="비밀번호 확인"
            />

            <h3>닉네임</h3>
            <v-text-field
              v-model="credentials.nickname"
              class="pt-0"
              type=""
              label="닉네임"
            />
          </v-form>

          <v-btn
            class="white--text my-5"
            color="#5B5C9D"
            block
            elevation="0"
            rounded
            @click="signupCheck"
          >
            회원가입
          </v-btn>
        </div>
      </v-container>
    </div>
  </v-app>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Signup2',

  // 이메일 비밀번호 규칙 설정
  data: function() {
    return {
    // 회원 정보
      credentials: {
        email: null,
        password: null,
        passwordCheck: null,
        nickname: null,
      },

      // 유효성 검사
      valid: true,

      emailRules: [
        v => !!v || '이메일을 입력해주세요.',
        v => /.+@.+\..+/.test(v) || '올바른 형식의 이메일을 입력해주세요.',
      ],

      passwordRules: {
        min: v => v.length >= 8 || '올바른 비밀번호를 입력해주세요.',
      },

      passwordCheckRules: {
        check: v => v === password || '일치하지 않는 비밀번호입니다.',
      },

      nicknameRules: {
        max: v => v.length <= 6 || '6글자 내의 닉네임을 입력해주세요.',
        min: v => v.length > 0 || '닉네임을 입력해주세요.'
      }
    };
  },

  methods: {
    ...mapActions([
      'signup',
    ]),
    // 회원가입 유효성 검사
    signupCheck: function () {
      this.signup(this.credentials)
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
