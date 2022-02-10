<template>
  <div>
    <v-text-field
      :label="label"
      filled
      rounded
      dense
      solo
      class="mx-5 mt-5"
      prepend-inner-icon="mdi-magnify"
      type="text"
      @keypress.enter="onInputKeyword"
      @click="goTo(routerPage)"
    />
    <!-- 모바일 이동 버튼용으로 처리 필요 -->
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  props: {
    label: { type: String, default: '' },
    routerPage: { type: String, default: '' },
  },
  methods: {
    onInputKeyword: function (event) {
      this.$emit('input-change', event.target.value)
    },
    goTo: function (routerPage) {
      if (routerPage) {
        this.$router.push({ name: routerPage })
      }
    }
  }
}
</script>

<style>

</style>
