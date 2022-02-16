<template>
  <div>
    <v-text-field
      v-model="query"
      :label="label"
      filled
      rounded
      dense
      solo
      :class="{ 'search-bar': isDetail, 'mx-3': isDetail, 'mx-4': !isDetail }"
      class="mt-5"
      prepend-inner-icon="mdi-magnify"
      type="text"
      @keypress.enter="onInputKeyword"
      @keydown.enter="onInputKeyword"
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
    isDetail: { type: Boolean, default: false },
    query: { type: String, default: '' },
  },
  created: function () {

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

  .search-bar {
    width: 84vw !important;
  }

</style>
