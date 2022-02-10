<template>
  <div>
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <div class="container">
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
  </div>
</template>

<script>
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector';

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
  },
  created: function () {
    axiosConnector.get(`/search/realtime`
    ).then((res) => {
      console.log('실검', res)
    }).catch((err) => {
      console.log(err)
    })
  },
}
</script>

<style>

</style>
