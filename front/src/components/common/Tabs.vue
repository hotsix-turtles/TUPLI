<template>
  <!-- 둘러보기 탭용 -->
  <div class="mt-3 mx-3 d-flex-column">
    <div class="d-flex justify-space-between">
      <v-btn
        v-if="tabType !== 'video'"
        tile
        depressed
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === '전체' ? 'accent' : undefined"
        @click="onClick('전체')"
      >
        전체
      </v-btn>
      <v-btn
        tile
        depressed
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === '지금 핫한' ? 'accent' : undefined"
        @click="onClick('지금 핫한')"
      >
        지금 핫한
      </v-btn>
      <v-btn
        v-if="tabType === 'video'"
        tile
        depressed
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[4] ? 'accent' : undefined"
        @click="onClick(tabs[4])"
      >
        {{ tabs[4] }}
      </v-btn>
      <v-btn
        tile
        depressed
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
        tile
        depressed
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[1] ? 'accent' : undefined"
        @click="onClick(tabs[1])"
      >
        {{ tabs[1] }}
      </v-btn>
      <v-btn
        tile
        depressed
        height="7vh"
        width="29vw"
        class="my-1"
        :color="tab === tabs[2] ? 'accent' : undefined"
        @click="onClick(tabs[2])"
      >
        {{ tabs[2] }}
      </v-btn>
      <v-btn
        tile
        depressed
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
import { mapActions, mapState } from 'vuex'

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
        '지금 핫한': '999',
        '여행': '2', // Autos & Vehicles (되는 카테고리ID가 한정적이라 우선 자동차 카테고리로 넣어둠)
        '게임': '20', // Game
        '일상': '22', // People & Blogs
        '노하우/스타일': '26', // Howto & Style
        '동물': '15', // Pets & Animals
        '엔터테인먼트': '24', // Entertainment
        '영화/드라마': '1', // Film & Animation
        '음악': '10', // Music
        '교육/시사': '28', // Education
        '스포츠': '17', // Sports
        '기타': '23', // Comedy
      }
    }
  },
  computed: {
    ...mapState('video', {
      categoryVideos: state => state.categoryVideos,
    }),
  },
  created: function () {
    console.log('this.tabs', this.tabs)
    // 페이지 처음엔 플레이리스트 전체 카테고리 가져옴
    if (this.tabType === 'playlist') {
      this.getCategoryPlaylists('all')
    } else if (this.tabType === 'playroom') {
      this.getCategoryPlayrooms('all')
    } else if (this.tabType === 'video') {
      this.tab = '지금 핫한'
      const categoryName = '지금 핫한'
      const categoryId = this.tabsMatchVideo[categoryName]
      this.getCategoryVideos({ categoryName, categoryId })
    }
  },
  methods: {
    ...mapActions('playlist', [
      'getCategoryPlaylists',
    ]),
    ...mapActions('playroom', [
      'getCategoryPlayrooms',
    ]),
    ...mapActions('video', [
      'getCategoryVideos',
      'setCategoryInfos',
    ]),
    onClick: function(tabName) {
      console.log('tabName', tabName)
      console.log('this.categoryVideos[tabName]', this.categoryVideos[tabName])
      this.tab = tabName
      if (this.tabType === 'playlist') {
        const categoryName = this.tabsMatch[tabName]
        this.getCategoryPlaylists(categoryName)
      } else if (this.tabType === 'playroom') {
        const categoryName = this.tabsMatch[tabName]
        this.getCategoryPlayrooms(categoryName)
      } else if (this.tabType === 'video') {
        this.$emit('on-click-category')
        console.log('onClick', tabName)
        const categoryName = tabName
        const categoryId = this.tabsMatchVideo[categoryName]
        if (this.categoryVideos[tabName].length === 0) {
          this.getCategoryVideos({ categoryName, categoryId })
        } else {
          this.setCategoryInfos({ categoryName, categoryId })
        }
      }
    }
  }
}
</script>

<style>

</style>


