<template>
  <div>
    <cancel :page-name="'플레이룸 선택하기'" />
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'
import SearchBar from '../../components/common/SearchBar.vue'

export default {
  name: 'BoardFormPlayroom',
  components: {
    Cancel,
    SearchBar

  },
}
</script>

<style>

</style>
