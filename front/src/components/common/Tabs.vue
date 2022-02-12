<template>
  <div class="mt-3 d-flex-column">
    <div class="d-flex justify-space-between">
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === '전체' ? 'accent' : undefined"
        @click="onClick('전체')"
      >
        전체
      </v-btn>
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === '지금 핫한' ? 'accent' : undefined"
        @click="onClick('지금 핫한')"
      >
        지금 핫한
      </v-btn>
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[0] ? 'accent' : undefined"
        @click="onClick(tabs[0])"
      >
        {{ tabs[0] }}
      </v-btn>
    </div>
    <div class="d-flex justify-space-between">
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[1] ? 'accent' : undefined"
        @click="onClick(tabs[1])"
      >
        {{ tabs[1] }}
      </v-btn>
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[2] ? 'accent' : undefined"
        @click="onClick(tabs[2])"
      >
        {{ tabs[2] }}
      </v-btn>
      <v-btn
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[3] ? 'accent' : undefined"
        @click="onClick(tabs[3])"
      >
        {{ tabs[3] }}
      </v-btn>
    </div><br>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Tabs',
  props: {
    tabType: {type: String, default: ''},
    tabs: {type: Array, default () { [] }},
  },
  data: function () {
    return {
      tab: '전체',
      tabsMatch: {
        '전체': 'all',
        '지금 핫한': 'hot',
        '여행': 'trip',
        '게임': 'game',
        '일상': 'life',
        '노하우/스타일': 'style',
        '동물': 'animal',
        '엔터테인먼트': 'entertainment',
        '영화/드라마': 'movie',
        '음악': 'music',
        '교육/시사': 'education',
        '스포츠': 'sports',
        '기타': 'etc',
      },
      tabsMatchVideo: {
        '여행': '19',
        '게임': '20',
        '일상': '22',
        '노하우/스타일': '26',
        '동물': '15',
        '엔터테인먼트': '24',
        '영화/드라마': '30',
        '음악': '10',
        '교육/시사': '28',
        '스포츠': '17',
        '기타': '43',
      }
    }
  },
  methods: {
    ...mapActions('playlist', [
      'getCategoryPlaylists',
    ]),
    onClick: function(tabName) {
      const categoryName = this.tabsMatch[tabName]
      this.tab = tabName
      if (this.tabType === 'playlist') {
        this.getCategoryPlaylists(categoryName)
      } else if (this.tabType === 'playroom') {
        this.getCategoryPlayrooms(categoryName)
      } else if (this.tabType === 'video') {
        if (tabName === '전체') {
          // getAllVideos()
        } else if (tabName === '지금 핫한') {
          // getHotVideos()
        } else {
          const categoryId = this.tabsMatchVideo.tabName
          // getVideoCategory(categoryName)
        }
      }
    }
  }
}
</script>

<style>

</style>
