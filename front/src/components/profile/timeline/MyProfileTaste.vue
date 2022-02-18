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

      <div>
        <div class="mb-3">
          <span
            style="font-size:16px;"
            class="mx-5 mt-5"
          >
            <span style="font-weight: 600;">{{ nickname }}</span>님의 <span style="color:#5C5B9D; font-weight: 800;">튜플리 뱃지</span>
          </span>
        </div>
        <div v-if="badges">
          <badge-list
            :badges="badges"
          />
        </div>
        <div
          v-else
          class="pl-7"
        >
          <span>튜플리에 더 자주 방문해주세요. <br>기분 좋은 선물을 드릴게요.</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

import TasteList from '@/components/profile/timeline/taste/TasteList.vue'
import BadgeList from '@/components/profile/timeline/taste/BadgeList.vue'


export default {
  name: 'ProfileTaste',
  components: {
    TasteList,
    BadgeList,
  },
  props: {
    tastes: {type : Object, default() { {} } },
    nickname: {type: String, default() { '' }},
    badges:  {type : Object, default() { {} } },
  },
  computed: {
    ...mapState(['nickname'])
  },
  created: function() {
    console.log('취향 1', this.tastes)
  }
}
</script>

<style>

</style>
