<template>
  <v-dialog
    v-model="show"
    class="tupli-dialog-normal"
    max-width="300"
    persistent
  >
    <v-card>
      <v-card-title
        class="tupli-dialog-normal-title"
      >
        로그인이 필요한 서비스입니다.
      </v-card-title>
      <v-card-text
        class="tupli-dialog-normal-content"
      >
        로그인 후 이용하시겠습니까?
      </v-card-text>
      <v-card-actions style="margin-left:45%;">
        <v-btn
          text
          color="#8B8B8B"
          @click="onClickCancel"
        >
          취소
        </v-btn>
        <v-btn
          text
          color="#5B5C9D"
          @click="onClickOk"
        >
          확인
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'LoginDialog',
  props: {
    show: { type: Boolean, default: false },
  },
  methods: {
    onClickOk: function () {
      this.show = false
      this.$router.push({ name: 'Login' })
      this.$emit('on-click')
    },
    onClickCancel: function () {
      this.show = false
      this.$emit('on-click')
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
