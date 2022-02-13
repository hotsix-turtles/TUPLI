<template>
  <div class="">
    <back :page-name="'댓글'" />
    {{ playlistComments }}
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'

export default {
  name: 'PlaylistComment',
  components: {
    Back
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
    console.log('this.$route.params.playlistId', this.$route.params.playlistId)
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
