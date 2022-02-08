<template>
  <v-app>
    <v-container class="mb-0">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          좋아요한 게시물
        </h3>
      </v-row>
    </v-container>
    <div class="d-flex mt-3">
      <v-tabs
        v-model="activeTab"
        centered
        grow
        color="#5B5C9D"
        class="sticky"
      >
        <v-tab class="v-tap-width">
          플레이리스트
        </v-tab>
        <v-tab class="v-tap-width">
          플레이룸
        </v-tab>
        <v-tab class="v-tap-width">
          게시글
        </v-tab>
        <v-tab-item>
          <playlist-list />
        </v-tab-item>
        <v-tab-item>
          <playroom-list />
        </v-tab-item>
        <v-tab-item>
          <post-list />
        </v-tab-item>
      </v-tabs>
    </div>
  </v-app>
</template>

<script>
import PlaylistList from '@/components/profile/like/PlaylistList'
import PlayroomList from '@/components/profile/like/PlayroomList'
import PostList from '@/components/profile/like/PostList'

import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'

export default {
  name: 'Like',
  components: {
    PlaylistList,
    PlayroomList,
    PostList,
  },
  data: function() {
    return {
      playlistLikesList: [],
      playroomLikesList: [],
      BoardLikesList: []
    }
  },
  computed: {
    ...mapState(['authToken'])
  },
  created: function() {
    this.getBoardLikesList()
    this.getPlaylistLikesList()
    this.getPlayroomLikesList()
  },
  methods: {
    // 좋아요 한 플레이리스트들
    getPlaylistLikesList: function() {
      axios({
        method: 'GET',
        url: SERVER.URL + '/playlist/likes',
        headers: {Authorization: this.authToken}
      })
        .then((res) => {
          console.log('플레이리스트', res.data)
          this.playlistLikesList = res.data
        }) 
    },
    // 좋아요 한 플레이룸
    getPlayroomLikesList: function() {
      axios({
        method: 'GET',
        url: SERVER.URL + '/playroom/likes',
        headers: {Authorization: this.authToken}
      })
        .then((res) => {
          console.log('플레이룸', res.data)
          this.playlistLikesList = res.data
        }) 
    },
    // 좋아요 한 게시물들
    getBoardLikesList: function() {
      axios({
        method: 'GET',
        url: SERVER.URL + '/board/likes',
        headers: {Authorization: this.authToken}
      })
        .then((res) => {
          console.log('게시물', res.data)
          this.playlistLikesList = res.data
        }) 
    }
  }



}
</script>

<style>
  .v-tap-width {
    width: 130px !important;
  }



</style>
