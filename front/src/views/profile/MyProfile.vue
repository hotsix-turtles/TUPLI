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
              :src="image"
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
        <div class="d-flex justify-center pt-3">
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'MyFollow' })"
          >
            <p class="mr-2">
              팔로잉
            </p>
            <p>{{ following.length }}</p>
          </div>
          <div
            class="d-flex mx-3"
            @click="$router.push({ name: 'MyFollow' })"
          >
            <p class="mr-2">
              팔로워
            </p>
            <p>{{ followers.length }}</p>
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
    <div class="d-flex justify-space-around mt-5">
      <v-tabs
        v-model="activeTab"
        centered
        grow
        color="#5B5C9D"
      >
        <v-tab>활동</v-tab>
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

import { mapState } from 'vuex'

export default {
  components: { ProfilePlaylist, ProfileTaste, },
  computed: {
    ...mapState(['authToken', 'nickname', 'image', 'introduction', 'following', 'followers'])
  },
  methods: {

  },


}
</script>

<style>

</style>
