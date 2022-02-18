<template>
  <div>
    <!-- CD -->
    <div>
      <playlist-cd-medium
        :thumbnail="playlist.image"
        :playlist-id="playlist.id"
      />
    </div>
    <div class="d-flex-column">
      <!-- 제목 -->
      <div
        class="txt-2 semi-bold"
        @click="$router.push({ name: 'PlaylistDetail', params: { playlistId: playlist.id } })"
      >
        {{ playlist.title }}
      </div>
      <div class="d-flex justify-space-between">
        <div class="d-flex-column">
          <!-- 닉네임 -->
          <div
            class="font-3 color-dark-gray"
            @click="$router.push({ name: 'Profile', params: { userId : playlist.userId }})"
          >
            {{ playlist.nickname }}
          </div>
          <!-- 태그 -->
          <tags
            v-if="playlist.tags"
            :tags="playlist.tags.split(',').splice(0,3)"
          />
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
    <login-dialog
      :show="showLoginDialog"
      @on-click="showLoginDialog = false"
    />
  </div>
</template>

<script>
import Tags from '../common/Tags.vue'
import { mapActions, mapState } from 'vuex'
import PlaylistCdMedium from './PlaylistCdMedium.vue'
import LoginDialog from '../../components/common/LoginDialog.vue';

export default {
  name: 'PlaylistItemMedium',
  components: {
    PlaylistCdMedium,
    Tags,
    LoginDialog,
  },
  props: {
    playlist: { type: Object, default() { {} } },
  },
  data() {
    return {
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState('playlist', {
    }),
    ...mapState({
      isLogin: state => state.isLogin,
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
      if (this.isLogin) {
        this.playlist.isLiked = true
        this.playlist.likesCnt++
        this.likePlaylist(this.playlist.id)
      } else {
        this.showLoginDialog = true
      }
    },
    onClickUnlike: function () {
      if (this.isLogin) {
        this.playlist.isLiked = false
        this.playlist.likesCnt--
        this.unlikePlaylist(this.playlist.id)
      } else {
        this.showLoginDialog = true
      }
    },
  },
}
</script>

<style>

</style>
