<template>
  <v-app>
    <main-item
      v-for="content in mainContents"
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
  },
  methods: {
  }
}
</script>

<style>

</style>
