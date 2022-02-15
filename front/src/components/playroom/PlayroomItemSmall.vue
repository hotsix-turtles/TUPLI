<template>
  <v-expansion-panel
    id="panelHeader"
    elevation="0"
    aria-expanded="false"
  >
    <!-- 플레이룸 정보 -->
    <v-expansion-panel-header
      class="pl-0 py-3"
    >
      <div
        class="d-flex justify-space-between"
        width="100%"
      >
        <div class="d-flex align-center">
          <!-- 라디오 버튼 -->
          <div
            v-if="isRadioBtn"
            class="mx-2"
          >
            <div v-if="playroom.id === chosenPlayroom.id">
              <v-icon color="#5B5C9D">
                mdi-radiobox-marked
              </v-icon>
            </div>
            <div
              v-else
              @click.stop="choose"
            >
              <v-icon>
                mdi-radiobox-blank
              </v-icon>
            </div>
          </div>
          <div class="video-thumbnail">
            <img
              :src="playroom.videos[0].thumbnail"
              style="width: 35vw; height: 100px; object-fit: cover;"
              class=""
              @click="$router.push({ name: 'PlayroomDetail', params: { id: playroom.id } })"
            >
            <!-- <div
              v-if="playroom.onPlay"
              class="on-play"
            > 나중에 이걸로 변경-->
            <div
              class="on-play"
            >
              ON PLAY
            </div>
            <span class="duration d-flex">
              <div>
                <v-icon
                  x-small
                  color="white"
                  class="mr-1"
                >
                  mdi-account-multiple
                </v-icon>
              </div>
              <div>132</div>
            <!-- {{ playroom.userCount }} 나중에 바꿀 부분 -->
            </span>
          </div>
          <div class="d-flex-column ml-2">
            <v-list-item-title class="semi-bold">
              {{ playroom.title }}
            </v-list-item-title>
            <v-list-item-subtitle class="color-dark-gray mt-1">
              {{ playroom.title }}
            </v-list-item-subtitle>
            <!-- <div class="color-dark-gray">
              <span>{{ playroom.user.nickname }}</span>
            </div> 나중 -->
            <div class="font-4 color-dark-gray mt-1 mb-2">
              <span>
                <span class="semi-bold">
                  PLAY TIME
                </span>
                {{ playroom.startTime }} ~ {{ playroom.endTime }}
              </span>
            </div>
            <tags
              v-if="playroom.tags"
              :tags="playroom.tags.split(',').slice(0, 3)"
              class="mt-3"
            />
          </div>
        </div>
      </div>
    </v-expansion-panel-header>
    <!-- 펼치기 했을 때 나오는 플레이리스트 정보 -->
    <v-expansion-panel-content
      style="overflow:scroll;  height: 30vh;"
      class="px-0"
    >
      <div
        v-for="(playlist, idx) in playroom.playlistsInfo"
        :key="idx"
      >
        <playroom-playlist-item-small
          :playlist="playlist"
        />
      </div>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Tags from '../common/Tags.vue'
import PlayroomPlaylistItemSmall from './PlayroomPlaylistItemSmall.vue'

export default {
  name: 'PlayroomItemSmall',
  components: { Tags, PlayroomPlaylistItemSmall },
  props: {
    playroom: { type: Object, default() { {} } },
    isRadioBtn: { type: Boolean, default: false },
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapState('board', {
      chosenPlayroom: state => state.chosenPlayroom
    })
  },
  methods: {
    ...mapActions('board', [
      'choosePlayroom'
    ]),
    choose: function () {
      console.log('choosePlayroom')
      this.choosePlayroom(this.playroom)
    }
  }
}
</script>

<style>
</style>
