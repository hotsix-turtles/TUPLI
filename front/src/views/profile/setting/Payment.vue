<template>
  <div>
    <v-container class="mb-5 ml-3">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          size="20"
          color="black"
          class="px-2"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-arrow-left
        </v-icon>
        <h3>
          결제 서비스
        </h3>
      </v-row>
    </v-container>
    <div class="pt-10">
      <div class="d-flex flex-column justify-center align-center">
        <div class="d-flex flex-column align-center mb-9">
          <span
            style="font-size: 16px; font-weight:500;"
          >더 다양하게 <span
            style="color: #5C5B9D; font-weight:700;"
          >취향</span>을 나누고 싶은 당신을 위한</span>
          <span
            class="mb-4 pt-2"
            style="font-size: 18px; font-weight:700;"
          >
            튜플리 프리미엄 회원
          </span>
          <div
            class="d-flex justify-center mb-3 mt-2"
          >
            <img
              src="@/assets/kakaopay_icon.png"
              alt="카카오페이"
              width="25%"
              @click="buyPremium"
            >
          </div>

          <span class="d-flex justify-center align-start">
            <p class="d-flex flex-column mr-2 align-center">
              <span style="text-decoration:line-through;">3,500원</span>
              <span
                style=" font-weight:500;"
              >무료 체험 기간</span>
            </p>
          </span>
        </div>
        <div
          class="d-flex justify-center px-4 pt-8"
        >
          <div class="d-flex flex-column align-start">
            <span
              class="mb-3"
              style="font-size: 16px; font-weight:700;"
            >프리미엄 회원 특전 안내</span>
            <div class="d-flex flex-column">
              <p class="mb-1">
                - 댓글 및 실시간 채팅 시 닉네임과 내용에 <span
                  style="color: #5C5B9D; font-weight:600;"
                >퍼스널 컬러 적용</span>
              </p>
              <p class="mb-1">
                - 댓글 및 실시간 채팅 시 닉네임 옆에 표시되는 <span
                  style="color: #5C5B9D; font-weight:600;"
                >회원용 배지</span>
              </p>
              <p class="mb-1">
                - 댓글 및 실시간 채팅에 사용할 수 있는 <br><span
                  style="color: #5C5B9D; font-weight:600; margin-left:15px;"
                >gif, 커스텀 이모지 추가</span>
              </p>
              <p class="mb-1">
                - 플레이룸 최대 초대 인원 수 5명 -> <span
                  style="color: #5C5B9D; font-weight:600;"
                >30명</span>
              </p>
              <p class="mb-1">
                - 플레이룸 최대 지속 시간 3시간 -> <span
                  style="color: #5C5B9D; font-weight:600;"
                >3일</span>
              </p>
              <p class="mb-1">
                - 프리미엄 회원용 배지 부여
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'

import axiosConnector from '@/utils/axios-connector.js'

export default {
  name: 'Payment',
  data: function() {
    return {
      vipCheck: '',
    }
  },
  computed: {
    ...mapState(['authToken', 'userId'])
  },
  created: function() {
    this.isVipCheck()
  },
  methods: {
    buyPremium: function() {
      axios({
        method: 'GET',  // 주소얻어오는거라 GET
        url: SERVER.URL + SERVER.ROUTES.accounts.kakaoPay,
        headers: {Authorization: this.authToken},
      })
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
    isVipCheck: function() {
      axiosConnector.get(`userinfo/${this.userId}`)
        .then((res) => {
          console.log('프리미엄 회원 여부', res.data.is_vip)
          this.vipCheck = res.data.is_vip
        })
        .catch((err) => {
          console.log('에러 - 결제', err)
        })
    },


  },
}
</script>

<style>

</style>
