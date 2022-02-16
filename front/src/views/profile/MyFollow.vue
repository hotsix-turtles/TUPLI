<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="d-flex align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.go(-1)"
        >
          mdi-chevron-left
        </v-icon>
        <h3 class="">
          {{ nickname }}
        </h3>
      </v-row>
    </v-container>
    <div class="d-flex mt-0">
      <v-tabs
        v-model="activeTab"
        centered
        grow
        color="#5B5C9D"
        class="sticky"
      >
        <v-tab class="v-tap-width">
          <div class="d-flex align-center">
            <p class="mx-1 mb-0">
              팔로워
            </p>
            <p class="mx-1 mb-0">
              {{ followers.length }}
            </p>
          </div>
        </v-tab>
        <v-tab class="v-tap-width">
          <div class="d-flex align-center">
            <p class="mx-1 mb-0">
              팔로잉
            </p>
            <p class="mx-1 mb-0">
              {{ following.length }}
            </p>
          </div>
        </v-tab>

        <v-tab-item>
          <my-followers-list />
        </v-tab-item>
        <v-tab-item>
          <my-followings-list />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import MyFollowersList from '@/components/profile/user/MyFollowersList.vue'
import MyFollowingsList from '@/components/profile/user/MyFollowingsList.vue'

import { mapState, mapActions } from 'vuex'

export default {
  name: 'MyFollow',
  components: {
    MyFollowersList,
    MyFollowingsList,
  },
  data: function() {
    return{
      followerlist: [],
      followinglist: [],
    }
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname', 'following', 'followers'])
  },
  created: function() {
    this.getUserInfo()
    console.log('팔로워 데이터 확인', this.following, this.followers, this.followers.length)
  },
  methods: {
    ...mapActions(['getUserInfo']),

  }
}
</script>

<style>

</style>
