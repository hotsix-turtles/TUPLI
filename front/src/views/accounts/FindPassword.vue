<template>
  <div>
    <h1>비밀번호 찾기</h1>
    <div class="mt-4 pt-4">
      <v-form ref="form">
        <h3>이메일</h3>
        <v-text-field
          v-model="credentials.email"
          required
          :rules="emailRules"
          label="이메일을 입력해주세요"
        />


        <h3>닉네임</h3>
        <v-text-field
          v-model="credentials.nickname"
          required
          type=""
          label="닉네임을 입력해주세요."
        />
      </v-form>

      <v-btn
        class="white--text my-5"
        color="#5B5C9D"
        block
        required
        elevation="0"
        rounded
        @click="findPassword"
      >
        비밀번호 찾기
      </v-btn>
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
    }
  },
  methods: {

    findPassword: function() {
      axiosConnector.post(`/account/passwordFind/${this.credentials.email}/nickname/${this.credentials.nickname}`)
        .then((res) =>
          console.log(res)
        )
        .catch((err) =>
          console.log('에러', err))
    }
  }
}
</script>

<style>

</style>
