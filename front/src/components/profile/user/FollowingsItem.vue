<template>
  <div>
    <div
      class="d-flex justify-center mx-4 my-2"
      style="width: 360px;"
    >
      <div class="d-flex align-center">
        <div class="d-flex align-center">
          <div
            class="d-flex profile-img-medium"
          >
            <img
              :src="ImgUrl(following.profileImage)"
              alt="profile img"
            >
          </div>
          <p
            class="mb-0 mx-3"
            @click="setProfile"
          >
            {{ following.nickname }}&nbsp;
          </p>
        </div>
        <v-btn>팔로잉</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { getImage } from '@/utils/utils'

export default {
  name: 'FollowingsItem',
  props: {
    following: { type: Object, default() {} }
  },
  methods: {
    // 이미지 조합
    ImgUrl: function(img) {
      return getImage(img)
    },
  }
}
</script>

<style>

</style>
