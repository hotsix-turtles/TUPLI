<template>
  <div class="background-login">
    <div class="px-5">
      <div>
        <v-container class="mb-5">
          <div
            class="align-center mt-5 d-flex"
          >
            <router-link
              to="/signup"
              class="no-background-hover"
            >
              <v-icon
                size="20"
                color="black"
                class="px-2"
              >
                mdi-arrow-left
              </v-icon>
            </router-link>
            <p class="back-menu-tex">
              회원정보
            </p>
          </div>
        </v-container>


        <div
          class="d-flex justify-center"
        >
          <!-- 상태바 -->
          <img
            src="../../assets/signup_bar2.png"
            alt="logo"
            width="90%"
          >
        </div>


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

            <h3 class="mt-4">
              비밀번호
            </h3>

            <v-text-field
              v-model="credentials.password"
              required
              :rules="[passwordRules.input, passwordRules.min]"
              type="password"
              label="비밀번호"
            />

            <v-text-field
              v-model="credentials.passwordCheck"
              required
              :rules="[passwordCheckRules.input, passwordCheckRules.check]"
              type="password"
              label="비밀번호 확인"
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
            * 프로필 이미지와 자기소개 폼은 프로필 편집을 통해 변경할 수 있습니다.
          </p>

          <v-btn
            class="white--text my-6"
            color="#5B5C9D"
            block
            required
            elevation="0"
            rounded
            large
            @click="signupCheck"
          >
            회원가입
          </v-btn>
        </div>
      </div>
    </div>
    <timeout-dialog
      v-model="isFormDataNotValidError"
      title="오류"
      :content="formDataNotValidReason"
      max-width="320"
      timeout="2000"
      hide-progress
      @timeout="isFormDataNotValidError = false"
    />
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import TimeoutDialog from '../../components/common/TimeoutDialog.vue';

export default {
  name: 'Signup2',
  components: { TimeoutDialog },

  // 이메일 비밀번호 규칙 설정
  data: function() {
    return {
      // 회원 정보
      credentials: {
        email: '',
        password: '',
        passwordCheck: '',
        nickname: '',
        image: '@/assets/tupli_logo2_dark.png',
        // username: null,
      },

      isFormDataNotValidError: false,
      formDataNotValidReason: '',

      // 유효성 검사
      valid: true,

      emailRules: [
        v => !!v || '이메일을 입력해주세요.',
        v => /.+@.+\..+/.test(v) || '올바른 형식의 이메일을 입력해주세요.',
      ],

      passwordRules: {
        input: v => !!v || '비밀번호를 입력해주세요.',
        min: v => v.length >= 4 && v.length <= 16 || '4자 이상 16자 이하 비밀번호를 입력해주세요.',
      },

      passwordCheckRules: {
        input: v => !!v || '비밀번호를 입력해주세요.',
        check: v => v === this.credentials.password || '일치하지 않는 비밀번호입니다.',
      },

      nicknameRules: {
        input: v => !!v || '닉네임을 입력해주세요.',
        max: v => v.length <= 7 || '7글자 내의 닉네임을 입력해주세요.',
      }
    };
  },

  methods: {
    ...mapActions([
      'signup',
    ]),
    // 로그인
    requestSignup: async function () {
      const { data, status } = await this.signup(this.credentials)

      if (status == 201) {
        this.valid = true
        this.$router.push( { name: 'Signup3' })
      } else if (status == 400) {
        this.isFormDataNotValidError = true;
        this.formDataNotValidReason = data.errorMessage;
      }
    },

    // 회원가입 유효성 검사
    signupCheck: function () {
      this.requestSignup()
    },

    onInputKeyword: function() {
      this.requestSignup()
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
    background-color: #5B5C9D;
  }

  .no-background-hover {
   text-decoration: none !important;
}

</style>
