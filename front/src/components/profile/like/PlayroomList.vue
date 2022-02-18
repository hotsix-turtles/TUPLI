<template>
  <v-app>
    <playroom-item
      v-for="playroomlist in playroomlists"
      :key="playroomlist.id"
      :playroomlist="playroomlist"
    />
  </v-app>
</template>

<script>
import PlayroomItem from './PlayroomItem.vue'
import { mapState, mapActions } from 'vuex'


export default {
  name: 'PlayroomList',
  components: {
    PlayroomItem,
  },
  data: function() {
    return {
      playroomlists: [],
    }
  },
  computed: {
    ...mapState('account', ['likePlayroomList'])
  },
  created: function() {
    this.getLikePlayroomList()
    this.playroomlists = this.likePlayroomList
    console.log('좋아요한 플레이룸', this.playroomlists)
  },
  methods: {
    ...mapActions('account', [
      'getLikePlayroomList'
    ])
  }
}
</script>

<style>

</style>
