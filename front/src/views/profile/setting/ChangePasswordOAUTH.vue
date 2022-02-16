<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting'})"         
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          비밀번호 설정(구글유저, 초회)
        </h3>
      </v-row>
    </v-container>
    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>
          설정 비밀번호
        </h4>
        <v-text-field
          v-model="credentials_password.newPassword"
          type="password"
          required
          dense
        />
      </div>
      <div class="my-2">
        <h4>
          설정할 비밀번호 확인
        </h4>
        <v-text-field
          v-model="credentials_password.newCheckPassword"
          type="password"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="changePassword"
    >
      비밀번호 설정하기
    </v-btn>
  </v-app>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'
import swal from 'sweetalert2'

export default {
  name: 'ChangePasswordOAUTH',
  data: function() {
    return {
      credentials_password: {
        newPassword: '',
        newCheckPassword: '',  // 일치 체크는 프론트에서 해주세요.
      },
    }
  },
  computed: {
    ...mapState(['authToken'])
  },
  methods: {
    changePassword: function() {
      if (this.credentials_password.newPassword != this.credentials_password.newCheckPassword) {
        swal.fire ({
          icon: 'error',
          title: '변경 실패',
          text: '새 비밀번호가 일치하지 않습니다.',
          scrollbarPadding: false
        })
      } else {
        axios({
          method: 'PUT', 
          url: SERVER.URL + '/account/password/oauth',
          headers: {Authorization: this.authToken},
          data: {
            // password: this.credentials_password.nowPassword,
            // password: 'NO_PASS',
            passwordChange: this.credentials_password.newPassword
          }
        })
          .then((res) => {
            this.$router.push({ name: 'Setting' })
            swal.fire ({
              icon: 'success',
              title: '비밀번호 설정 성공',
              text: '요청하신 비밀번호로 설정되었습니다.',
              // scrollbarPadding: false  // 모바일 환경은 알아서 체크해야
            })
          })
          .catch ((err) => {
            // console.log(err.response.data)
            swal.fire ({
              icon: 'error',
              title: '변경 실패',
              text: '이미 변경하셨거나 구글 유저가 아니십니다.'
            })
          })
      }
    },

  },
}
</script>

<style scoped>

</style>
