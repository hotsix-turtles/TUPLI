<template>
  <v-app>
    <div class="background-login px-5">
      <!-- <h1>회원가입 페이지</h1> -->
      <v-container>
        <v-container class="mb-5">
          <v-row
            class="align-center mt-5"
          >
            <router-link
              to="/signup2"
              class="no-background-hover"
            >
              <v-icon
                color="#5B5C9D"
              >
                mdi-chevron-left
              </v-icon>
            </router-link>
            <h4 class="">
              가입 완료
            </h4>
          </v-row>
        </v-container>

        <!-- 상태바 -->
        <div
          class="d-flex justify-center"
        >
          <img
            src="../../assets/signup_bar3.png"
            alt="logo"
            width="90%"
          >
        </div>

        <!-- 회원가입 form -->
        <div class="d-flex flex-column mt-5">
          <img
            src=""
            alt=""
          >
          <div class="d-flex">
            <p class="bold">
              {{ nickname }}
            </p>
            <p>님의</p>
          </div>
          <p>
            튜플리 가입을 환영합니다!
          </p>
          <v-btn
            to="/"
            class="white--text my-5"
            color="#5B5C9D"
            block
            elevation="0"
            rounded
          >
            홈으로 이동
          </v-btn>
        </div>
      </v-container>
    </div>
  </v-app>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'Signup3',

  // 이메일 비밀번호 규칙 설정
  data: function() {
    return{
    }
  },

  computed: {
    ...mapState(['authToken', 'userId', 'nickname']),
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
