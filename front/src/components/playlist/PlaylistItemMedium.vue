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
          <tags :tags="playlist.tags.split(',')" />
          <!-- 닉네임 -->
          <div>{{ playlist.nickname }}</div>
          <!-- 임시 닉네임
          <div>춘식이</div> -->
        </div>
        <!-- 좋아요 -->
        <div class="d-flex-column text-center">
          <div
            v-if="isLikedData"
            @click="onClickUnlike"
          >
            <v-icon>mdi-cards-heart</v-icon>
          </div>
          <div
            v-else
            @click="onClickLike"
          >
            <v-icon>mdi-cards-heart-outline</v-icon>
          </div>
          <div class="font-4">
            {{ likesCntData }}
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
      isLikedData: false,
      likesCntData: 0,
    }
  },
  computed: {
    ...mapState('playlist', {
      isLiked: state => state.isLiked,
    }),
  },
  created() {
    this.isLikedData = this.playlist.isLiked
    this.likesCntData = this.playlist.likesCnt
  },
  methods: {
    ...mapActions('playlist', [
      'likePlaylist',
      'unlikePlaylist',
    ]),
    onClickLike: function () {
      this.isLikedData = true
      this.likesCntData++
      this.likePlaylist(this.playlist.id)
    },
    onClickUnlike: function () {
      this.isLikedData = false
      this.likesCntData--
      this.unlikePlaylist(this.playlist.id)
    },
  },
}
</script>

<style>

</style>
