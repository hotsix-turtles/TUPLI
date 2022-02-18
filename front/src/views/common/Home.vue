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
        @click="goNotice"
      >
        mdi-bell
      </v-icon>
    </div>
    <div class="d-flex-column">
      <hr style="width: 100%; height: 1px; background-color: #f1f1f4 !important; ">
      <div
        v-if="authToken"
        class="d-flex mx-5 mt-4 mb-3"
      >
        <p>
          <span
            class="font-weight-bold color-main"
            style="margin-right: 0px;"
            bold
          >
            {{ nickname }}
          </span>
          <span class="">
            님이 좋아하는&nbsp;
          </span>
          <span class="color-main">
            오늘의 추천 컨텐츠
          </span>
        </p>
      </div>
      <div
        v-else
        class="d-flex mx-5 mt-4 mb-3"
      >
        <p
          class="font-weight-bold color-main"
          style="margin-right: 1px;"
          bold
        >
          튜플리
        </p>
        <p class="">
          가 추천하는&nbsp;
        </p>
        <p class="color-main">
          오늘의 컨텐츠
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
import { playtimeConverter } from '../../utils/utils';

import { mapState } from 'vuex'

export default {
  name: 'Home',

  components: {
    MainList,
    InfiniteLoading,
  },

  data: function () {
    return {
      page: 0,
      mainContents: [],
    }
  },

  created: function () {
  },

  computed: {
    ...mapState(['authToken', 'nickname']),
    today: function () {
      return new Date() / 1000
    }
  },

  methods: {
    getMainContents($state) {
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
            console.log('Home', res.data)
            this.page++
            const contents = res.data
            contents.forEach((content) => {
              if (content.type === 'playroom') {
                if (content.startTime <= this.today && content.endTime >= this.today) {
                  content.onPlay = true
                } else {
                  content.onPlay = false
                }
                content.playTime = playtimeConverter(content.startTime, content.endTime)
                content.image = content.videos.thumbnail
                content.profileImg = content.userProfileImg
                content.nickname = content.nickName
              }
            })
            this.mainContents.push(...contents)
            $state.loaded()
          } else {
            $state.complete()
          }
        })
        .catch((err) => {
          console.log(err)
          $state.complete()
        })
    },
    // 로그인 상태 확인
    goNotice: function() {
      if (this.authToken) {
        this.$router.push({ name: 'Notice' })
      }
      else {
        this.$router.push({ name: 'Login' })
      }
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
