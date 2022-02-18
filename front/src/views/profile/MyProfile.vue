<template>
  <v-app>
    <v-container>
      <!-- 설정 아이콘 -->
      <div
        class="d-flex justify-end mt-7 mr-5"
      >
        <v-icon
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-cog
        </v-icon>
      </div>
      <!-- 유저 정보 -->
      <div>
        <!-- 유저 정보 : 프로필 사진, 닉네임, 자기소개 -->
        <div class="d-flex flex-column align-center">
          <div class="profile-img-large">
            <img
              :src="ImgUrl(image)"
              alt=""
              fab
            >
          </div>
          <h3 class="text-center pt-2 pb-1">
            {{ nickname }}
          </h3>
          <div class="d-flex align-center">
            <p class="mb-0 mt-1">
              {{ introduction }}
            </p>
          </div>
        </div>
        <!-- 팔로우/팔로잉 -->
        <div class="d-flex justify-center pt-3 mb-3">
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'MyFollow' })"
          >
            <p class="mr-2">
              팔로워
            </p>
            <p>{{ followerlist.length }}</p>
          </div>
          <div
            style="width: 1px; height: 6px;"
          />
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'MyFollow' })"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p>{{ followinglist.length }}</p>
          </div>
        </div>
        <!-- 프로필 편집 버튼 -->
        <div class="d-flex justify-center">
          <v-btn
            class="text-center"
            outlined
            color="#5B5C9D"
            rounded
            to="/editprofile"
          >
            프로필 편집하기
          </v-btn>
        </div>
      </div>
    </v-container>

    <!-- 유저 활동, 취향 탭 -->
    <div class="d-flex justify-space-around mt-1">
      <v-tabs
        v-model="tab"
        centered
        grow
        color="#5B5C9D"
      >
        <v-tab>활동</v-tab>
        <v-tab>취향</v-tab>
        <v-tab-item>
          <my-profile-playlist
            :activities="activities"
          />
        </v-tab-item>
        <v-tab-item>
          <my-profile-taste
            class="pt-4"
            :badges="badges"
            :tastes="tastes"
          />
        </v-tab-item>
      </v-tabs>
    </div>

    <!--무한스크롤 -->
    <infinite-loading
      v-if="tab === 0"
      spinner="waveDots"
      @infinite="getAccounts"
    >
      <div slot="no-results" />
      <div slot="no-more" />
    </infinite-loading><br><br>
  </v-app>
</template>

<script>
import MyProfilePlaylist from '../../components/profile/timeline/MyProfilePlaylist.vue'
import MyProfileTaste from '../../components/profile/timeline/MyProfileTaste.vue'
import InfiniteLoading from "vue-infinite-loading"

import axiosConnector from '@/utils/axios-connector.js'
import { getImage } from '@/utils/utils'
import { mapState } from 'vuex'

export default {
  components: { MyProfilePlaylist, MyProfileTaste, InfiniteLoading, },
  data: function() {
    return {
      tab: null,
      followerlist: [],
      followinglist: [],
      activities: [],
      badges: '',
      tastes: [],
      image: '',
      profile: [],


      // 무한스크롤용
      page: 0,
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname', 'introduction', 'following', 'followers'])
  },
  created: function() {
    // this.getAccounts()
    this.getFollowerList()
    this.getBadge()
    this.getTaste()
    console.log('본인 프로필 확인', this.profile)
  },
  methods: {
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
    // 팔로우 목록
    getFollowerList: function() {
      axiosConnector.get(`/account/userInfo`)
        .then((res) => {
          this.followerlist = res.data.from_user
          this.followinglist = res.data.to_user
        })
        .catch((err) => {
          console.log('에러', err)
        })
    },

    // [조회]
    getAccounts: function ($state) {
      // axiosConnector.get(`userinfo/${this.userId}`
      // ).then((res) => {this.profile = res.data; this.activities = res.data.activities})
      // console.log('getAccounts params 본인')
      const params = {
        paged: true,
        page: this.page,
        size: 5,
      }
      axiosConnector.get(`userinfo/${this.userId}`, {
        params
      })
        .then((res) => {
          if (this.page === 0) {
            this.page++
            console.log('본인 프로필', res.data)
            this.profile = res.data
            this.image = res.data.profileImage
            this.activities.push(...res.data.activities)
            $state.loaded()
          } else if (res.data.activities.length) {
            this.page++
            this.activities.push(...res.data.activities)
            $state.loaded()
          } else {
            $state.complete()
          }
        })
        .catch((err) => {
          console.log('에러', err)
          $state.complete()
        })
    },

    // getTaste: function() {
    //   axiosConnector.get(`/account/userInfo`)
    //     .then((res) => {
    //       console.log('본인 프로필 취향취향취향', res.data)
    //       console.log('본인 프로필 취향취향취향', res.data.taste.userInfo.tasteInfo)
    //       this.tastes = res.data.taste.userInfo.tasteInfo
    //     })
    //     .catch((err) => {
    //       console.log('에러', err)
    //       $state.complete()
    //     })
    // },

    getTaste: function() {
      axiosConnector.get(`userinfo/${this.userId}`)
        .then((res) => {
          this.tastes = res.data.userInfo.tasteInfo
        })
        .catch((err) => {
          console.log('에러 - 취향', err)
        })
    },

    getBadge: function() {
      console.log('뱃지')
      axiosConnector.get('/badge/list')
        .then((res) => {
          // console.log('뱃지 획득', res.data, typeof(res.data))
          this.badges = res.data
          // console.log('뱃지 획득2', this.badges, typeof(this.badges))

        })
        .catch((err) => {
          console.log('에러', err)
        })
    }
  },


}
</script>

<style>
p { margin-bottom: 0px !important;}
</style>
