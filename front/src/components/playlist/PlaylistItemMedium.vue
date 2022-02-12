<template>
  <div>
    <!-- CD -->
    <div @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })">
      <playlist-cd-medium
        :thumbnail="playlist.image"
      />
    </div>
    <div class="d-flex-column">
      <!-- 제목 -->
      <div @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })">
        {{ playlist.title }}
      </div>
      <div class="d-flex justify-space-between">
        <div class="d-flex-column">
          <!-- 태그 -->
          <tags
            v-if="playlist.tags"
            :tags="playlist.tags.split(',').splice(0,3)"
          />
          <!-- 닉네임 -->
          <div class="color-dark-gray">
            {{ playlist.nickname }}
          </div>
        </div>
        <!-- 좋아요 -->
        <div class="d-flex-column text-center">
          <div
            v-if="playlist.isLiked"
            class="animate__animated animate__heartBeat"
            @click="onClickUnlike"
          >
            <v-icon color="#5B5C9D">
              mdi-cards-heart
            </v-icon>
          </div>
          <div
            v-else
            @click="onClickLike"
          >
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
          <div class="font-4">
            {{ playlist.likesCnt }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Tags from '../common/Tags.vue'
import { mapActions, mapState } from 'vuex'
import PlaylistCdMedium from './PlaylistCdMedium.vue'

export default {
  name: 'PlaylistListItemMedium',
  components: { PlaylistCdMedium, Tags },
  props: {
    playlist: { type: Object, default() { {} } },
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapState('playlist', {
    }),
  },
  created() {
  },
  methods: {
    ...mapActions('playlist', [
      'likePlaylist',
      'unlikePlaylist',
    ]),
    onClickLike: function () {
      this.playlist.isLiked = true
      this.playlist.likesCnt++
      this.likePlaylist(this.playlist.id)
    },
    onClickUnlike: function () {
      this.playlist.isLiked = false
      this.playlist.likesCnt--
      this.unlikePlaylist(this.playlist.id)
    },
  },
}
</script>

<style>

</style>
