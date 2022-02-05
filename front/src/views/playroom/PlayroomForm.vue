<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between">
      <back :page-name="pageName" />
      <v-btn
        class="clickable"
        text
        @click="submit"
      >
        완료
      </v-btn>
    </div>

    <!-- 플레이룸 생성 폼 -->
    <v-form v-model="isValid">
      <v-container>
        <!-- 제목 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="formData.title"
              :rules="titleRules"
              :counter="30"
              label="플레이룸 제목을 입력해주세요."
              required
            />
          </v-col>
        </v-row>

        <!-- 소개글 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="formData.content"
              :counter="80"
              label="플레이룸 소개글을 입력해주세요."
            />
          </v-col>
        </v-row>

        <!-- 태그 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <tag-input
              :proped-tags="formData.tags"
              @tag-input="updateTags"
            />
            />
          </v-col>
        </v-row>

        <!-- 공개 여부 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
            class="d-flex flex-row justify-space-between"
          >
            <p class="font-3">
              공개 설정
            </p>
            <p class="font-4 ml-1 mr-auto">
              {{ isPublicMsg }}
            </p>
            <v-switch
              v-model="formData.isPublic"
            />
          </v-col>
        </v-row>

        <!-- 친구 초대 레이블 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between"
          >
            <p class="font-3">
              친구 초대
            </p>
            <p class="font-4 ml-1 mr-auto">
              친구에게 플레이룸 시작 전 알림을 보냅니다.
            </p>
          </v-col>
        </v-row>

        <!-- 친구 초대 버튼 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="py-0"
          >
            <v-btn small>
              <v-icon color="black">
                mdi-plus
              </v-icon>
              <span style="color: black;">친구 추가</span>
            </v-btn>
          </v-col>
        </v-row>

        <!-- 친구 초대 리스트 조작 버튼 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between pb-0"
          >
            <v-btn
              small
              elevation="0"
              color="white"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4">
              {{ numberOfFriend }}명 유저 선택
            </p>
          </v-col>
        </v-row>

        <!-- 친구 초대 리스트 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="pt-0"
          >
            <v-expansion-panels class="mx-auto">
              <v-expansion-panel
                class="py-2"
                elevation="0"
              >
                <v-expansion-panel-header>
                  초대할 친구 리스트
                </v-expansion-panel-header>
                <v-expansion-panel-content
                  v-for="friend in formData.friends"
                  :id="friend.id"
                  :key="friend.id"
                >
                  {{ friend.name }}
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
          </v-col>
        </v-row>

        <!-- 플레이리스트 추가 버튼 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="py-0"
          >
            <v-btn
              small
              @click="saveAndGo"
            >
              <v-icon color="black">
                mdi-plus
              </v-icon>
              <span style="color: black;">플레이리스트 추가</span>
            </v-btn>
          </v-col>
        </v-row>

        <!-- 플레이리스트 리스트 조작 버튼 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="d-flex flex-row justify-space-between pb-0"
          >
            <v-btn
              small
              elevation="0"
              color="white"
              @click="selectAllVideo"
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4">
              {{ numberOfAddedPlaylists }}개 플레이리스트 / {{ numberOfAddedPlaylistSelectedVideos }}개 영상 선택
            </p>
          </v-col>
        </v-row>

        <!-- 플레이리스트 리스트 -->
        <v-row>
          <v-col
            cols="12"
            md="12"
            class="pt-0"
          >
            <playlist-list-item-small
              :playlists="addedPlaylists"
              :playlist-readonly="true"
              :video-readonly="false"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import PlaylistListItemSmall from '../../components/playlist/PlaylistListItemSmall.vue'
import TagInput from '../../components/common/TagInput.vue'
import { mapActions } from 'vuex'

export default {
  name: 'PlaylistForm',
  components: {
    Back,
    TagInput,
    PlaylistListItemSmall
  },
  data: function() {
    return {
      pageName: "내 플레이룸 만들기",
      titleRules: [
        v => !!v || '제목은 필수입니다.',
        v => v.length > 2 || '제목은 3글자 이상 작성해야 합니다.',
      ],
      isValid: false,
      // Create할 때 넘길 데이터
      formData: {
        title: '',
        content: '',
        tags: [],
        isPublic: true,
        friends: [],
        playlists: []
      },
      numberOfPlaylist: 0,
      numberOfPlaylistVideos: 0
    }
  },
  computed: {
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이룸을 공개합니다." : "내 플레이룸을 비공개합니다."
    },
    numberOfFriend () {
      return this.formData.friends.length
    },
    ...mapState('playlist', ['addedPlaylists']),
    ...mapState('playroom', ['savedFormData']),
    ...mapGetters('playlist', ['numberOfAddedPlaylists', 'numberOfAddedPlaylistSelectedVideos', 'numberOfAddedPlaylistVideos'])
  },
  created: function () {
    if (this.savedFormData) {
      console.log('restoreData', this.savedFormData)
      this.formData = this.savedFormData
    }
  },
  methods: {
    updateTags: function (tags) {
      this.formData.tags = tags
    },
    onVideoItemClicked ( { id, selected }) {
      return this.formData.playlists.map((playlist) => playlist.videos.map((v) => v.included = (v.id == id ? !selected : v.included)))
    },
    submit() {
      if (this.formData.tags) this.formData.tags = this.formData.tags.join()
      this.formData.playlists = this.addedPlaylists
      console.log(this.formData)
      this.createPlayroom(this.formData)
    },
    saveAndGo: function () {
      this.saveFormData(this.formData)
      this.$router.push({ name: 'PlayroomFormPlaylist'})
    },
    selectAllVideo () {
      if (!this.numberOfAddedPlaylistVideos == this.numberOfAddedPlaylistSelectedVideos)
        this.selectAllPlaylistVideo()
      else
        this.deselectAllPlaylistVideo()
    },
    ...mapActions('playroom', ['saveFormData', 'createPlayroom']),
    ...mapActions('playlist', ['selectAllPlaylistVideo', 'deselectAllPlaylistVideo'])
  },
}
</script>

<style>

</style>
