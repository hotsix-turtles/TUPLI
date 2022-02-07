<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          결제 서비스
        </h3>
      </v-row>
    </v-container>
    <div class="d-flex justify-center">
      <v-btn
        class="white--text my-5"
        color="#5B5C9D"
        elevation="0"
        rounded
        @click="buyPremium"
      >
        임시 결제 버튼/프리미엄회원은 안보임
      </v-btn>
    </div>
  </v-app>
</template>

<script>
import axiosConnector from '@/utils/axios-connector.js'

export default {

  name: 'Payment',
  methods: {
    buyPremium: function() {
      axiosConnector.get('/kakaoPay/')
      // 실제 적용시에는, 이미 프리미엄 유저인지 확인하는 것 필요
      // axios({
      //   method: 'GET',
      //   url: SERVER.URL + SERVER.ROUTES.accounts.kakaoPay,
      //   headers: {Authorization: this.token},
      // })
        .then((res) => {
          window.location.href = res.data
        })
        .catch (() => {
          swal.fire ({
            icon: 'error',
            title: '결제 실패',
            text: '서버가 혼잡합니다. 다시 시도해 주세요.'
          })
        })
    },

  },
}
</script>

<style>

</style>
