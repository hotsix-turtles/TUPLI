<template>
  <div>
    <post-item
      v-for="boardlist in boardlists"
      :key="boardlist.id"
      :boardlist="boardlist"
      :thumbnail="boardlist"
    />
  </div>
</template>

<script>
import PostItem from '@/components/profile/like/PostItem.vue'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'PostList',
  components: {
    PostItem,
  },
  data: function() {
    return {
      boardlists: [],
    }
  },
  computed: {
    ...mapState('account', ['likeBoardList'])
  },
  created: function() {
    this.getLikeBoardList()
    this.boardlists = this.likeBoardList
    console.log('좋아요한 게시글', this.likeBoardList)
  },
  methods: {
    ...mapActions('account', [
      'getLikeBoardList'
    ])
  }
}
</script>

<style>

</style>
