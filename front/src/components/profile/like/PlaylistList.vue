<template>
  <v-app>
    <playlist-item
      v-for="playlistlist in playlistlists"
      :key="playlistlist.id"
      :playlistlist="playlistlist"
      :thumbnail="playlistlist.videos[0].thumbnail"
    />
  </v-app>
</template>

<script>
import PlaylistItem from '@/components/profile/like/PlaylistItem.vue'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'PlaylistList',
  components: {
    PlaylistItem,
  },
  data: function() {
    return {
      playlistlists: [],
    }
  },
  computed: {
    ...mapState('account', ['likePlaylistList'])
  },
  created: function() {
    this.getLikePlaylistList()
    this.playlistlists = this.likePlaylistList
    console.log('좋아요한 플레이리스트', this.playlistlists)
  },
  methods: {
    ...mapActions('account', [
      'getLikePlaylistList'
    ])
  }
}
</script>

<style>

</style>
