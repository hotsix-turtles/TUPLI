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
          <img
            class="my-3"
            src="https://yt3.ggpht.com/wb7A_9h1cIkVGNLQAjljyVzlFvYowycvJd_fM-1O3Ozp-0cpsjvkz16154jOIu-BORVWbLD7Nw=s176-c-k-c0x00ffffff-no-rj-mo"
            alt=""
            width="88px"
            style="border-radius: 100px; margin: 10px;"
          >
          <h3 class="text-center pt-2 pb-1">
            {{ nickname }}
          </h3>
          <div class="d-flex align-center mt-1">
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
            v-if="followText === '팔로우'"
            class="text-center mx-2 white--text"
            color="#5B5C9D"
            rounded
            @click="followBtn"
          >
            &nbsp;{{ followText }}&nbsp;
          </v-btn>
          <v-btn
            v-if="followText === '팔로잉 중'"
            class="text-center mx-2 dark--text"
            color="#5B5C9D"
            rounded
            outlined
            @click="followBtn"
          >
            &nbsp;{{ followText }}&nbsp;
          </v-btn>
          <v-btn
            class="text-center mx-2"
            outlined
            color="#5B5C9D"
            rounded
            to="/editprofile"
          >
            같이 시청하기
          </v-btn>
        </div>
      </div>
    </v-container>

    <div class="d-flex justify-space-around mt-5">
      <v-tabs
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
import ProfilePlaylist from '../../components/profile/timeline/ProfilePlaylist.vue'
import ProfileTaste from '../../components/profile/timeline/ProfileTaste.vue'

import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'

export default {
  components: { ProfileTaste, ProfilePlaylist },
  data: function() {
    return {
      followCheck: false,
      followText: '팔로잉',
    }
  },
  computed: {
    ...mapState(['authToken', 'nickname', 'introduction', 'following', 'followers'])
  },
  methods: {
    followBtn: function() {
      if (this.followText === '팔로우') {
        console.log('follow')
        this.follow(this.profileInfo.id)
        this.follower_cnt++
      } else {
        console.log('unfollow')
        this.unfollow(this.profileInfo.id)
        this.follower_cnt--
      }
      axios({
        method: 'POST',
        url: SERVER.URL + SERVER.ROUTES.account.follow,
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
    }
  },


}
</script>

<style>

</style>
