<template>
  <v-app>
    <h1>프로필 편집</h1>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5 justify-space-between"
      >
        <div class="d-flex">
          <router-link
            to="/profile"
            class="no-background-hover"
          >
            <v-icon
              color="#5B5C9D"
            >
              mdi-chevron-left
            </v-icon>
          </router-link>
          <h4 class="">
            프로필 편집
          </h4>
        </div>
        <router-link
          to="/profile"
          class="no-background-hover"
        >
          <v-btn
            class="d-flex justify-end p-5 mx-5"
            small
            rounded
            outlined
            color="#5B5C9D"
            depressed
          >
            완료
          </v-btn>
        </router-link>
      </v-row>
      <v-container>
        <div class="d-flex flex-column align-center">
          <img
            class="py-3"
            src="../../assets/logo_semi.png"
            alt=""
            width="45px"
            fab
          >
          <p>프로필 사진 변경</p>
        </div>
        <div class="d-flex flex-column justify-center pt-3">
          <v-form>
            <div class="flex-column align-start mx-3">
              <p class="mr-2 mb-0">
                닉네임
              </p>
              <v-text-field
                class="pt-0"
                label="김춘식"
                hint="새로운 닉네임을 입력해주세요."
              />
            </div>
            <div class="flex-column align-start mx-3 mt-5">
              <p class="mr-2 mb-0">
                자기소개 글
              </p>
              <v-text-field
                class="pt-0"
                label="자기소개 글을 입력해주세요."
                hint="자기소개 글을 입력해주세요."
              />
            </div>
          </v-form>
        </div>
      </v-container>
    </v-container>
  </v-app>
</template>

<script>
export default {

}
</script>

<style>
  .no-background-hover {
      text-decoration: none !important;
    }
</style>
