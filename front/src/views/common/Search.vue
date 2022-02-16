<template>
  <div>
    <search-bar
      :label="'검색어를 입력해주세요'"
      :router-page="'SearchDetail'"
    />
    <!-- @click="goSearchDetail" -->
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
        <div
          class="font-2 clickable"
          @click="$router.push({ name: 'SearchDetail', params: { tab: tabMatch[keyword.type], keyword: keyword.keyword }})"
        >
          <span class="color-main bold ml-1 mr-2">{{ idx + 1 }}</span>
          <span>{{ keyword.keyword }}</span>
        </div>
        <hr
          v-if="idx !== 9"
          class="my-2"
        >
      </div>
    </div>
    <!-- 로그아웃 -->
    <div
      class="d-flex justify-space-between setting-bar align-center"
      @click="logoutUser"
    >
      <p>로그아웃</p>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import SearchBar from '../../components/common/SearchBar.vue'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'Search',
  components: { SearchBar },
  data: function () {
    return {
      keywords: [],
      tabMatch: {
        '플레이리스트': 0,
        '플레이룸': 1,
        '유저': 2,
      }
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
    // 검색 데이터 리셋
    this.resetSearchPlaylists()
    this.resetSearchPlayrooms()
    this.resetSearchAccounts()
    this.resetVideoSearchState()
  },
  methods: {
    ...mapActions('playlist', [
      'resetSearchPlaylists',
    ]),
    ...mapActions('playroom', [
      'resetSearchPlayrooms',
    ]),
    ...mapActions('account', [
      'resetSearchAccounts',
    ]),
    ...mapActions('video', [
      'resetVideoSearchState',
    ]),
    // 로그아웃
    logoutUser: function() {
      this.$store.dispatch('logout')
      this.$router.push({ name: 'Home' })
      swal.fire ({
        icon: 'info',
        title: '로그아웃',
        text: '로그아웃되었습니다.',
        // width: '200px'
      })
    },

  }
}
</script>

<style>

</style>
