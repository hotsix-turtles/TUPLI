<template>
  <v-app>
    <div class="d-flex align-center px-4">
      <div
        class="d-flex justify-center align-center"
        style="background-color: yellow; width: 60px; height: 60px; border-radius: 100%;"
      >
        <img
          src="@/assets/tupli_logo2_dark.png"
          alt="profile img"
          width="30"
        >
      </div>
      <div class="mx-3">
        <div class="d-flex mb-0">
          <p
            class="mb-0"
            @click="setProfile"
          >
            너구리&nbsp;
          </p>
          <p class="mb-0">
            님이&nbsp;
          </p>
          <p class="mb-0">
            회원님을 팔로우합니다.
          </p>
        </div>
        <div>
          <p
            class="mb-0"
          >
            15분
          </p>
        </div>
      </div>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'NoticeItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    notices: { type: Object },
  },
  methods: {
    setProfile: function() {
      this.$router.push({ name: 'Profile' })
    }
  },
}
</script>

<style>

</style>
