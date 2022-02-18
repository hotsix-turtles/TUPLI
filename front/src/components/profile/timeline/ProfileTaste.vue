<template>
  <div>
    <div>
      <div>
        <span
          style="font-size:16px;"
          class="mx-5 mt-3"
        >
          <span style="font-weight: 600;">{{ nickname }}</span>님의 <span style="color:#5C5B9D; font-weight: 800;">취향 분석</span>
        </span>
        <div
          v-if="tastes"
          class="pt-2"
        >
          <taste-list
            :tastes="tastes"
          />
        </div>
        <div v-else>
          <span>튜플리를 즐기다보면 취향 분석이 피어나요.</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import TasteList from '@/components/profile/timeline/taste/TasteList.vue'
import axiosConnector from '../../../utils/axios-connector'


export default {
  name: 'ProfileTaste',
  components: {
    TasteList,
  },
  props: {
    tastes: {type : Object, default() { {} } },
    badges: {type : Array, default() { [] } },
    nickname: {type: String, default() { '' }},
    userId:  {type : Number, default() { 0 } },
  },
  created: function() {
    console.log('뱃지33ddd3', this.badges)
    this.getBadge()
  },
  methods: {
    // 뱃지 획득
    getBadge: function() {
      axiosConnector.get(`/badge/list/${this.$route.params.userSeq}`)
        .then((res) => {
          this.badges = res.data.badgeSeq
        })
        .catch((err) => {
          console.log('에러', 'err')
        })
    }
  }
}
</script>

<style>

</style>
