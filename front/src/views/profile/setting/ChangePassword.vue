<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          비밀번호 변경
        </h3>
      </v-row>
    </v-container>
    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>
          현재 비밀번호
        </h4>
        <v-text-field
          v-model="credentials_password.nowPassword"
          type="password"
          required
          dense
        />
      </div>
      <div class="my-2">
        <h4>
          변경 비밀번호
        </h4>
        <v-text-field
          v-model="credentials_password.newPassword"
          type="password"
          required
          dense
        />
      </div>
      <div class="my-2">
        <h4>
          변경할 비밀번호 확인
        </h4>
        <v-text-field
          v-model="credentials_password.newCheckPassword"
          type="password"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="$router.push({ name: 'Setting'})"
    >
      비밀번호 변경하기
    </v-btn>
  </v-app>
</template>

<script>
export default {
  name: 'ChangePassword',
  data: function() {
    return {
      credentials_password: {
        nowPassword: '',
        newPassword: '',
        newCheckPassword: '',
      },
    }
  }
}
</script>

<style scoped>

</style>
