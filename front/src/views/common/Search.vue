<template>
  <div>
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <div class="mx-5 my-2">
      <p class="font-1 bold color-main mb-5">
        실시간 검색어 트렌드
      </p>
      <div
        v-for="(keyword, idx) in keywords"
        :key="idx"
      >
        <div class="font-4 color-dark-gray ml-1">
          {{ keyword.type }}
        </div>
        <div class="font-2">
          <span class="color-main bold ml-1 mr-2">{{ idx + 1 }}</span>
          <span>{{ keyword.keyword }}</span>
        </div>
        <hr
          v-if="idx !== 9"
          class="my-2"
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
      keywords: [],
    }
  },
  created: function () {
    axiosConnector.get(`/search/realtime`
    ).then((res) => {
      console.log('실검', res)
      this.keywords = res.data
    }).catch((err) => {
      console.log(err)
    })
  },
}
</script>

<style>

</style>
