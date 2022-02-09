<template>
  <div class="container">
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <p>실시간 검색어 트렌드</p>
    <div
      v-for="(keyword, idx) in keywords"
      :key="idx"
    >
      <div class="font-4">
        {{ keyword.keywordType }}
      </div>
      <div class="font-2">
        {{ idx + 1 }} {{ keyword.keyword }}
      </div>
      <hr
        v-if="idx !== 9"
        class="mt-3"
      >
    </div>
  </div>
</template>

<script>
import SearchBar from '../../components/common/SearchBar.vue'
export default {
  name: 'Search',
  components: { SearchBar },
  data: function () {
    return {
      keywords: [
        {
          keywordType: '플레이리스트',
          keyword: '춘식이',
        },
        {
          keywordType: '플레이룸',
          keyword: '먹방',
        },
        {
          keywordType: '플레이리스트',
          keyword: '춘식이',
        },
        {
          keywordType: '플레이룸',
          keyword: '먹방',
        },
        {
          keywordType: '플레이리스트',
          keyword: '춘식이',
        },
        {
          keywordType: '플레이룸',
          keyword: '먹방',
        },
        {
          keywordType: '플레이리스트',
          keyword: '춘식이',
        },
        {
          keywordType: '플레이룸',
          keyword: '먹방',
        },
        {
          keywordType: '플레이리스트',
          keyword: '춘식이',
        },
        {
          keywordType: '플레이룸',
          keyword: '먹방',
        },
      ]
    }
  }
}
</script>

<style>

</style>
