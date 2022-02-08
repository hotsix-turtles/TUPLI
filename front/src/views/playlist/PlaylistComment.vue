<template>
  <div class="container">
    {{ playlistComments }}
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'PlaylistComment',
  components: {
  },
  data: function() {
    return {
    }
  },
  computed: {
    ...mapState('playlist', {
      playlistComments: state => state.playlistComments,
    }),
    ...mapState('account', {
      userId: state => state.userId,
    })
  },
  created: function() {
    this.getPlaylistComments(this.$route.params.playlistId)
  },
  methods: {
    ...mapActions('playlist', [
      'getPlaylistComments',
    ]),
  },
}
</script>

<style>

</style>
