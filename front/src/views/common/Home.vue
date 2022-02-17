<template>
  <v-app>
    <div
      class="d-flex justify-space-between sticky-header px-5 pb-4 pt-5"
      style="background-color: white;"
    >
      <img
        src="@/assets/tupli_logo_dark.png"
        class="home-logo"
      >
      <!-- 알림 아이콘 -->
      <v-icon
        color="#000000"
        @click="$router.push({ name: 'Notice' })"
      >
        mdi-bell
      </v-icon>
    </div>
    <div class="d-flex-column">
      <hr style="width: 100%; height: 1px; background-color: #f1f1f4 !important; ">
      <div class="d-flex mx-4 mt-3 mb-3">
        <p
          class="font-weight-bold color-main"
          bold
        >
          {{ nickname }}
        </p>
        <p class="">
          님이 좋아하는&nbsp;
        </p>
        <p class="color-main">
          오늘의 추천 컨텐츠
        </p>
      </div>
    </div>

    <div>
      <main-list :main-contents="mainContents" />
    </div>

    <!--무한스크롤 -->
    <infinite-loading
      spinner="waveDots"
      @infinite="getMainContents"
    >
      <div slot="no-results" />
      <div slot="no-more" />
    </infinite-loading><br><br>
  </v-app>
</template>

<script>
import MainList from '@/components/home/MainList'
import InfiniteLoading from "vue-infinite-loading"
import axiosConnector from '../../utils/axios-connector';

import { mapState } from 'vuex'

export default {
  name: 'Home',

  components: {
    MainList,
    InfiniteLoading,
  },

  data: function () {
    return {
      page: 1,
      mainContents: [],
    }
  },

  created: function () {
  },

  computed: {
    ...mapState(['authToken', 'nickname'])
  },

  methods: {
    getMainContents($state) {
      console.log('getMainContents')
      const params = {
        paged: true,
        page: this.page,
        size: 5,
      }
      axiosConnector.get(`home/all/`, {
        params
      })
        .then((res) => {
          if (res.data.length) {
            console.log('getMainContents then', res)
            this.page++
            this.mainContents.push(...res.data)
            $state.loaded()
            console.log('this.mainContents', this.mainContents)
          } else {
            $state.complete()
          }
        })
        .catch((err) => {
          console.log(err)
          $state.complete()
        })
    }
  }
}
</script>

<style>

  .home-logo {
    width: 100px;
    height: 28px;
  }

  .sticky-header {
    position: sticky;
    top: 0;
    z-index: 1;
  }
</style>
