<template>
  <v-app>
    <post-item
      v-for="boardlist in boardlists"
      :key="boardlist.id"
      :boardlist="boardlist"
      :thumbnail="boardlist"
    />
    <div class="d-flex">
      <div class="d-flex">
        <img
          src="@/assets/tupli_logo2_dark.png"
          alt=""
        >
      </div>
      <div class="d-flex flex-column">
        <p>이름</p>
        <p>내용</p>
      </div>
    </div>
  </v-app>
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
