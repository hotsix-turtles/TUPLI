<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between">
      <back :page-name="pageName" />
      <p class="clickable">
        완료
      </p>
    </div>

    <!-- 플레이룸 생성 폼 -->
<<<<<<< HEAD
    <v-form v-model="valid">
=======
    <v-form v-model="isValid">
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
      <v-container>
        <!-- 제목 -->
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
<<<<<<< HEAD
              v-model="title"
=======
              v-model="formData.title"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
<<<<<<< HEAD
              v-model="content"
=======
              v-model="formData.content"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
<<<<<<< HEAD
            <v-text-field
              v-model="content"
=======
            <TagInput
              v-model="formData.tags"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
              :counter="80"
              label="플레이룸 태그를 입력해주세요."
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
            <v-btn small>
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
            >
              <v-icon class="mdi-18px">
                mdi-check
              </v-icon>
              <span class="ml-1">전체 선택</span>
            </v-btn>
            <p class="font-4">
              {{ numberOfPlaylist }}개 플레이리스트 / {{ numberOfPlaylistVideos }}개 영상 선택
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
            <v-expansion-panels
              v-for="playlist in formData.playlists"
              :id="playlist.id"
              :key="playlist.id"
              class="mx-auto"
            >
              <v-expansion-panel
                class="py-2"
                elevation="0"
              >
                <v-expansion-panel-header>
                  <v-avatar
                    size="80"
                    class="ml-2 my-2"
                  >
                    <v-img
                      :src="playlist.thumbnailSrc"
                      contain
                    />
                  </v-avatar>
                  {{ playlist.name }}
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                  <PlaylistVideoItem
                    v-for="video in playlist.videos"
                    :id="video.id"
                    :key="video.id"
                    :title="video.title"
                    :thumbnail="video.thumbnail_url"
                    :author="video.author"
                    :playtime="video.playtime"
<<<<<<< HEAD
                    :clicked="video.selected"
=======
                    :selected="video.included"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
                    @click="onVideoItemClicked"
                  />
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </div>
</template>

<script>
import Back from '../../components/common/Back.vue'
import PlaylistVideoItem from '../playroom/PlaylistVideoItem.vue'
<<<<<<< HEAD
=======
import TagInput from '../../components/common/TagInput.vue'
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5

export default {
  name: 'PlaylistForm',
  components: {
    Back,
<<<<<<< HEAD
=======
    TagInput,
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    PlaylistVideoItem
  },
  data: function() {
    return {
      pageName: "내 플레이룸 만들기",
<<<<<<< HEAD
      // Create할 때 넘길 데이터
      formData: {
        valid: false,
=======
      titleRules: [
        v => !!v || '제목은 필수입니다.',
        v => v.length <= 2 || '제목은 3글자 이상 작성해야 합니다.',
      ],
      isValid: false,
      // Create할 때 넘길 데이터
      formData: {
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
        title: '',
        content: '',
        tags: '',
        isPublic: true,
<<<<<<< HEAD
        titleRules: [
          v => !!v || '제목은 필수입니다.',
          v => v.length <= 2 || '제목은 3글자 이상 작성해야 합니다.',
        ],
=======
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
        friends: [
          { id: 1, name: "김형준" },
          { id: 2, name: "김기솔" },
          { id: 3, name: "최하영" },
        ],
        playlists: [
          {
            id: 1, name: "3일만에 다이어트 포기함 ㅅㄱ", thumbnailSrc: "https://picsum.photos/80/80",
            videos: [
<<<<<<< HEAD
              {id: 1, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
              {id: 2, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
              {id: 3, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
              {id: 4, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
=======
              {id: 1, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
              {id: 2, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
              {id: 3, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
              {id: 4, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
            ]
          },
          {
            id: 2, name: "3일만에 다이어트 포기함 ㅅㄱ", thumbnailSrc: "https://picsum.photos/80/80",
            videos: [
<<<<<<< HEAD
              {id: 5, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
              {id: 6, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
              {id: 7, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', selected: false},
=======
              {id: 5, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
              {id: 6, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
              {id: 7, title: '해ㅐㅇ', thumbnail_url: 'https://picsum.photos/120/80', author: '쯔양', playtime: '01:30', included: false},
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
            ]
          }
        ]
      }
    }
  },
  computed: {
    isPublicMsg () {
      return this.formData.isPublic ? "내 플레이룸을 공개합니다." : "내 플레이룸을 비공개합니다."
    },
    numberOfFriend () {
      return this.formData.friends.length
    },
    numberOfPlaylist () {
      return this.formData.playlists.length
    },
    numberOfPlaylistVideos () {
<<<<<<< HEAD
      return this.formData.playlists.reduce((acc, cur) => acc + cur.videos.reduce((acc, cur) => acc + (cur.selected ? 1 : 0), 0), 0)
    }
  },
  methods: {
    onVideoItemClicked ( { id, clickState }) {
      return this.formData.playlists.map((playlist) => playlist.videos.map((v) => v.selected = (v.id == id ? clickState : v.selected)))
=======
      return this.formData.playlists.reduce((acc, cur) => acc + cur.videos.reduce((acc, cur) => acc + (cur.included ? 1 : 0), 0), 0)
    }
  },
  methods: {
    onVideoItemClicked ( { id, selected }) {
      return this.formData.playlists.map((playlist) => playlist.videos.map((v) => v.included = (v.id == id ? !selected : v.included)))
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    }
  }
}
</script>

<style>

</style>
