<template>
  <v-app>
    <main-item
      v-for="content in contents"
      :key="content.id"
      :content="content"
    />
  </v-app>
</template>

<script>
import MainItem from './MainItem.vue'

import { mapState, mapActions } from 'vuex'

export default {
  name: 'MainList',
  components: {
    MainItem,
  },
  data: function() {
    return {
      contents: [],
    }
  },
  computed: {
    ...mapState('mainContent', ['mainContents'])
  },
  created: function() {
    console.log('이건 뜨나요22', this.contents)
    this.getMainContent()
    this.contents = this.mainContents
    console.log('이건 뜨나요44', this.contents)
  },
  methods: {
    ...mapActions('mainContent',[
      'getMainContent',
    ]),
  }
}
</script>

<style>

</style>
