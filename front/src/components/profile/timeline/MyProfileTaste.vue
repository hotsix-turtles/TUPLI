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
}
</script>

<style>

</style>
