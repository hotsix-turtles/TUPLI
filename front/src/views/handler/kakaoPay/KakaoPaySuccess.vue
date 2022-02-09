<template>
  <div>
    결제 성공! <br>
    홈으로 이동 중...
  </div>
</template>

<script>
import swal from 'sweetalert2'
import router from '@/router'
import { mapMutations} from 'vuex'

import axios from 'axios'
import SERVER from '@/api/server'

export default {
// 이 부분 헷갈리시는 분 위해 기술  
// 이 시점에 이미 거래는 완료 되었습니다!
// 이 핸들러가 하는 일은 -> 결제 완료시, 결제 관련 내용을 api에 요청하고 받을 수 있는 토큰을 받는데 이걸 이용해서 카카오 api로 결제 내용을 가져오고, DB에 저장
// DB 저장하면 끝인걸 굳이 front 거치는 이유 : 안 거치면 서버는 DB 저장에는 문제 없지만, 클라이언트 측은 response 못 받아서 확장성 고려하면 프론트 거쳐야합니다.
// 자세한 설명 및 궁금점 : 강민구  
  name: 'KakaoPaySuccess',
  created() {
    this.kakaoPayRequest(this.$route.query.pg_token)
  },
  methods: {
    // $$$$ state에 유저쪽 is_vip 갱신  (현재 없는 함수)
    ...mapMutations(['SET_PREMIUM']),

    // 결제 성공시 정보 받아오기
    kakaoPayRequest: function (pg_token){
      axios({
        method: 'POST',
        headers: {Authorization: localStorage.getItem('jwt')},
        url: SERVER.URL +'/kakaoPay/success/',
        params: {pgToken: pg_token},
      })
        .then((res) => {
          console.log(res.data)
          this.SET_PREMIUM()  // $$$$ state에 유저쪽 is_vip 갱신
          swal.fire ({
            icon: 'success',
            title: '후원 감사합니다.',
            text: res.data.item_name + '회원으로 전환되었습니다.',
            scrollbarPadding: false
          })
          router.push('/profile') 
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