/* eslint-disable vue/require-default-prop */
<template>
  <div class="d-flex px-3 py-3 light-background fixed-top">
    <div>
      <v-icon
        color="black"
        @click="goBack"
      >
        mdi-arrow-left
      </v-icon>
    </div>
    <div class="font-2 semi-bold center">
      {{ pageName }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'Back',
  components: {
  },
  props: {
    pageName: {type: String, default: ''},
    cnt: {type: Number, default: 0}, // 같은 페이지 안에서 여러 번 페이지 변경한뒤 상위 페이지로 가고싶을때 사용
  },
  methods: {
    goBack: function() {
      const goCnt = -1 - this.cnt
      this.$router.go(goCnt)
    }
  }
}
</script>

<style scoped>
</style>
