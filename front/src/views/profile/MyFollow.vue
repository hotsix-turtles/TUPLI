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
          <followers-list />
        </v-tab-item>
        <v-tab-item>
          <followings-list />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import FollowersList from '@/components/profile/user/FollowersList.vue'
import FollowingsList from '@/components/profile/user/FollowingsList.vue'

import { mapState } from 'vuex'

export default {
  name: 'Follow',
  components: {
    FollowersList,
    FollowingsList,
  },
  computed: {
    ...mapState(['authToken', 'userId', 'nickname', 'following', 'followers'])
  }
}
</script>

<style>

</style>
