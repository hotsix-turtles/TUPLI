<template>
  <v-app>
    <div class="d-flex align-center">
      <div>
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
      </div>
      <div class="d-flex justify-space-between align-center">
        <div class="mx-3">
          <p
            class="mb-0"
            @click="setProfile"
          >
            너구리&nbsp;
          </p>
        </div>
        <div class="d-flex">
          <v-btn>팔로잉</v-btn>
          <v-icon>mdi-dots-horizontal</v-icon>
        </div>
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
