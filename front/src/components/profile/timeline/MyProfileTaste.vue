<template>
  <div>
    <div>
      <span
        style="font-size:16px;"
        class="mx-5 mt-3"
      >
        <span style="font-weight: 600;">{{ nickname }}</span>님의 <span style="color:#5C5B9D; font-weight: 800;">취향 분석</span>
      </span>

      <taste-list
        :tastes="tastes"
      />

      <div class="mb-3">
        <span
          style="font-size:16px;"
          class="mx-5 mt-5"
        >
          <span style="font-weight: 600;">{{ nickname }}</span>님의 <span style="color:#5C5B9D; font-weight: 800;">튜플리 뱃지</span>
        </span>
      </div>
      <badge-list
        :badges="badges"
      />
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
