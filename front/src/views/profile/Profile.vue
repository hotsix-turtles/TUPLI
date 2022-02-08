<template>
  <v-app>
    <v-container>
      <div
        class="d-flex justify-end mt-7 mr-5"
      >
        <v-icon
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-cog
        </v-icon>
      </div>
      <div>
        <div class="d-flex flex-column align-center">
          <h3 class="text-center pt-3 pb-1">
            {{ nickname }}
          </h3>
          <img
            class="py-3"
            src="../../assets/logo_semi.png"
            alt=""
            width="40px"
            fab
          >
          <div class="d-flex align-center">
            <h5 class="mr-5">
              자기소개
            </h5>
            <p class="mb-0">
              {{ introduction }}
            </p>
          </div>
        </div>
        <div class="d-flex justify-center pt-3">
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'Follow' })"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p>123</p>
          </div>
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'Follow' })"
          >
            <p class="mr-2">
              팔로워
            </p>
            <p>456</p>
          </div>
        </div>
        <div class="d-flex justify-center">
          <v-btn
            class="text-center pt-1"
            outlined
            color="#5B5C9D"
            rounded
            small
            to="/editprofile"
          >
            프로필 편집하기
          </v-btn>
        </div>
      </div>
      <div class="d-flex justify-center">
        <v-btn
          class="text-center pt-1"
          outlined
          color="#5B5C9D"
          rounded
          small
          @click="logout"
        >
          임시로그아웃버튼
        </v-btn>
      </div>
    </v-container>

    <div class="d-flex justify-space-around mt-5">
      <v-tabs
        v-model="activeTab"
        centered
        grow
        color="#5B5C9D"
      >
        <v-tab>
          활동
        </v-tab>
        <v-tab>취향</v-tab>
        <v-tab-item>
          <profile-playlist />
        </v-tab-item>
        <v-tab-item>
          <profile-taste />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import ProfilePlaylist from '../../components/profile/ProfilePlaylist.vue'
import ProfileTaste from '../../components/profile/ProfileTaste.vue'

import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'

export default {
  components: { ProfileTaste, ProfilePlaylist },
  computed: {
    ...mapState(['authToken', 'nickname', 'introduction', 'following', 'followers'])
  },
  methods: {
    buyPremium: function() {
      // 실제 적용시에는, 이미 프리미엄 유저인지 확인하는 것 필요
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
    // 임시 로그아웃 버튼 in profile
    logout: function() {
      this.$store.dispatch('logout')
      this.$router.push({ name: 'Login'})
    }
  },


}
</script>

<style>

</style>
