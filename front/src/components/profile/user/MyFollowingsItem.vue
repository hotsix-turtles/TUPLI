<template>
  <v-app>
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
            @click="setProfile"
          >
            너구리&nbsp;
          </p>
        </div>
        <v-btn>팔로잉</v-btn>
      </div>
    </div>
  </v-app>
</template>

<script>
export default {

}
</script>

<style>

</style>
