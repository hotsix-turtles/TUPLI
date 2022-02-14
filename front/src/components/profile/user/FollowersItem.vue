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
              src="@/assets/tupli_logo2_dark.png"
              alt="profile img"
            >
          </div>
          <p
            class="mb-0 mx-3"
          >
            {{ follower.nickname }}&nbsp;
          </p>
        </div>
        <v-btn>팔로잉</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FollowersItem',
  props: {
    follower: { type: Object, default() {} }
  }
}
</script>

<style>

</style>
