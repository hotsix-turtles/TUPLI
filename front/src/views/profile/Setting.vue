<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          size="20"
          color="black"
          class="px-2"
          @click="$router.push({ name: 'MyProfile' })"
        >
          mdi-arrow-left
        </v-icon>
        <h3>
          설정
        </h3>
      </v-row>
    </v-container>

    <div>
      <hr>

      <!-- 관리자 API : hotsixturtles@gmail.com -->
      <div
        v-if="email=='hotsixturtles@gmail.com'"
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'Admin' })"
      >
        <p>관리자 페이지</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 좋아요한 게시물 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'Like' })"
      >
        <p>좋아요한 게시물</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 결제 서비스 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'Payment' })"
      >
        <p>결제 서비스</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 알림 설정 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'InviteNoticeAlert' })"
      >
        <p>초대/알림 설정</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 개인정보 이용약관 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'PrivateTerms' })"
      >
        <p>개인정보 이용약관</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 서비스 이용약관 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'ServiceTerms' })"
      >
        <p>서비스 이용약관</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 비밀번호 변경 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'ChangePassword' })"
      >
        <p>비밀번호 변경</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>

      <!-- 로그아웃 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click.stop="dialogLogout = true"
      >
        <p>로그아웃</p>
        <v-icon>mdi-chevron-right</v-icon>
        <!-- 로그아웃 여부 재확인 dialog -->
        <v-dialog
          v-model="dialogLogout"
          max-width="290"
        >
          <v-card>
            <v-card-title class="text-h6">
              로그아웃하시겠습니까?
            </v-card-title>

            <v-card-text>
              로그아웃 후 게스트 모드로 홈 화면으로 이동합니다.
            </v-card-text>

            <v-card-actions>
              <v-spacer />

              <v-btn
                color="purple lighten-4"
                text
                @click="dialogLogout = false"
              >
                취소
              </v-btn>

              <v-btn
                color="purple darken-1"
                text
                @click="requestLogout"
              >
                로그아웃
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>

      <hr>

      <!-- 회원 탈퇴 -->
      <div
        class="d-flex justify-space-between setting-bar align-center"
        @click.stop="dialogDeleteUser = true"
      >
        <p>회원 탈퇴</p>
        <v-icon>mdi-chevron-right</v-icon>
        <!-- 회원 탈퇴 여부 재확인 dialog -->
        <v-dialog
          v-model="dialogDeleteUser"
          max-width="290"
        >
          <v-card>
            <v-card-title class="text-h6">
              회원 탈퇴 하시겠습니까?
            </v-card-title>

            <v-card-text>
              회원 탈퇴 시 사용자 정보를 다시 불러올 수 없습니다.<br>
              탈퇴 후에는 비회원 상태로 홈 화면으로 이동합니다.
            </v-card-text>

            <v-card-actions>
              <v-spacer />

              <v-btn
                color="purple lighten-4"
                text
                @click="dialogDeleteUser = false"
              >
                취소
              </v-btn>

              <v-btn
                color="purple darken-1"
                text
                @click="requestDeleteUser"
              >
                회원탈퇴
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>

      <hr>

      <!-- OAUTH유저 초회 비밀번호 변경 -->
      <div
        v-if="is_oauth"
        class="d-flex justify-space-between setting-bar align-center"
        @click="$router.push({ name: 'ChangePasswordOAUTH' })"
      >
        <p>구글 유저 비밀번호 설정(초회)</p>
        <v-icon>mdi-chevron-right</v-icon>
      </div>

      <hr>
    </div>
    <timeout-dialog
      v-model="isLogoutInfo"
      title="로그아웃 되었습니다"
      content="오늘도 튜플리와 함께 즐거우셨나요?"
      timeout="2000"
      hide-progress
      @timeout="logoutUser"
    />
    <timeout-dialog
      v-model="isDeleteUserInfo"
      title="탈퇴 성공"
      content="지금까지 튜플리와 함께해주셔서 감사합니다"
      timeout="2000"
      hide-progress
      @timeout="deleteUser"
    />
    <timeout-dialog
      v-model="isDeleteUserError"
      title="탈퇴 실패"
      content-html="서버가 혼잡합니다<br>잠시 뒤 다시 시도해주세요"
      timeout="2000"
      hide-progress
      @timeout="deleteUser"
    />
  </v-app>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'
import axiosConnector from '@/utils/axios-connector.js'
import swal from 'sweetalert2'
import TimeoutDialog from '../../components/common/TimeoutDialog.vue'

export default {
  name: 'Setting',
  components: { TimeoutDialog },
  data: function() {
    return {
      dialogLogout: null,
      dialogDeleteUser: null,
      isLogoutInfo: false,
      isDeleteUserInfo: false,
      isDeleteUserError: false
    }
  },
  computed: {
    ...mapState(['authToken', 'email', 'is_oauth'])
  },
  methods: {
    requestLogout: function() {
      this.$store.dispatch('logout')
      this.dialogLogout = false
      this.isLogoutInfo = true
    },

    // 로그아웃
    logoutUser: function() {
      this.$router.push({ name: 'Home' })
    },

    requestDeleteUser: async function() {
      let response;
      try {
        response = await axiosConnector.delete('/account/withdraw');
        this.$store.commit('DELETE_TOKEN')
      } catch (err) {
        this.dialogDeleteUser = false
        this.isDeleteUserError = true
      }
    },

    // 회원 탈퇴
    deleteUser: async function() {
      this.$router.push({ name: 'Home' })
    },
  },

}
</script>

<style scoped>
  .setting-bar {
    margin-left: 25px;
    margin-right: 25px;
    margin-top: 12px;
    margin-bottom: 12px !important;
    text-decoration-color: black !important;
  }

  p {
    font-size: 14px;
    margin-bottom: 0px !important;
  }
  hr {
    border: 0;
    height: 1px;
    background:lightgray;
  }

  .no-background-hover-text {
    text-decoration: none !important;
    text-decoration-color: black !important;
  }
</style>
