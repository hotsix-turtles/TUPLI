<template>
  <div>
    <main-item
      v-for="content in mainContents"
      :key="content.id"
      :content="content"
    />
  </div>
</template>

<script>
import MainItem from './MainItem.vue'

import { mapState, mapActions } from 'vuex'

export default {
  name: 'MainList',
  components: {
    MainItem,
  },
  props: {
    mainContents: { type: Array, default () { [] }}
  },
  data: function() {
    return {
      thumbnail: '',
    }
  },
  computed: {
  },
  created: function() {
    console.log('메인 컨텐츠 얻기', this.mainContents)

  },
  methods: {
  }
}
</script>

<style>

</style>
