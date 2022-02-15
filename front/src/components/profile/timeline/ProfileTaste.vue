<template>
  <div>
    <div>
      <h2>{{ nickname }}님의 취향 분석</h2>
      <taste-list
        :tastes="tastes"
      />
      <badge-list
        :badges="badges"
      />
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
    badges: {type : Object, default() { {} } },
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
