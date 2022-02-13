<template>
  <div>
    <!-- 뒤로가기/완료 -->
    <div class="d-flex justify-space-between fixed-top light-background">
      <back :page-name="pageName" />
      <div
        class="clickable"
        @click="onClickCompletion"
      >
        완료
      </div>
    </div><br><br>

    <!-- 게시글 생성 폼-->
    <v-form v-model="valid">
      <v-container>
        <v-row>
          <!-- 제목 쓰는칸 -->
          <v-col
            cols="12"
            md="4"
            class="py-0"
          >
            <v-text-field
              v-model="formData.title"
              :rules="[rules.required, rules.counterMax300]"
              :counter="300"
              label="게시글 내용을 입력해주세요."
              required
            />
          </v-col>

          <!-- 플레이룸or플레이리스트 로 만들기 (선택사항) -->
          <v-col
            class="d-flex justify-space-between py-0"
            cols="12"
            md="4"
          >
            <div>
              <p class="d-flex">
                플레이룸/플레이리스트
              </p>
              <p class="font-4 ml-1 mr-auto">
                공유하고 싶은 플레이룸/플레이리스트를 1개 선택합니다.(선택 사항)
              </p>
            </div>

            <v-switch
              v-model="formData.isBoard"
            />
          </v-col>

          <!-- 플레이룸인지 플레이리스트인지 선택하기 -->
          <v-col v-if="formData.isBoard">
            <v-radio-group
              v-model="formData.radioVal"
              column
            >
              <v-radio
                label="플레이룸"
                value="playroom"
              />
              <v-radio
                label="플레이리스트"
                value="playlist"
              />
            </v-radio-group>

            <!-- 선택하기 버튼 -->
            <v-btn
              color="accent"
              elevation="2"
              @click="goSelect"
            >
              선택하기
            </v-btn>
          </v-col>

          {{ formData.radioVal }}
          {{ selectedPlaylist }}
          {{ selectedPlayroom }}
          <!-- <playlist-item-medium
            v-for="(playlist, idx) in selectedPlaylist"
            :key="idx"
            :playlist="playlist"
            class="col-6"
          /> -->

          <v-col />
        </v-row>
      </v-container>
    </v-form>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
// import PlaylistItemMedium from '../../components/playlist/PlaylistItemMedium.vue'


export default {
  name: 'BoardForm',
  components: {
    Back,
    // PlaylistItemMedium
  },
  data: function() {
    return {
      pageName: "게시글 작성",
      formData: {
        title: '',
        isBoard: false,
        radioVal: '',
      },
      valid: false,
      rules: {
        required: v => !!v || '필수 입력값입니다.',
        counterMax300: v => v.length <= 300 || '내용은 300글자 이상 작성할 수 없습니다.',
      },
    }
  },
  computed: {
    ...mapState('board', {
      savedFormData: state => state.savedFormData,
      isSaved: state => state.isSaved,
      selectedPlaylist: state => state.selectedPlaylist,
      selectedPlayroom: state => state.selectedPlayroom
    }),
  },
  created: function() {
    if (this.isSaved) {
      this.formData = this.savedFormData
      console.log("내가 가져온 플레이리스트는", this.selectedPlaylist)
    } else {
      // this.resetVideoAddState()
      // console.log("boardForm첨왔어요")
    }
  },
  methods: {
    ...mapActions('board', [
      'saveFormData',
      'resetBoardPlaylistAddState',
      'selectPlaylistOrPlayroom'
    ]),
    onClickCompletion: function () {
      // 필수항목을 채웠는가?
      if (this.valid) {
        const data = {
          title: this.formData.title,
          isBoard: this.formData.isBoard,
          radioVal: this.formData.radioVal,
        }
        // 순수 글인가?
        if(data.isBoard == false) {
          console.log("순수 글 업로드")

        }
        // 플레이룸 or 플레이리스트에 대한 글
        else {
          if(this.formData.radioVal == '') {
            console.log("플레이룸or플레이리스트 선택바람")
            return
          }
          else {
            if(this.selectedPlaylist == null) {
              console.log("하나 이상 선택해야함.")
              return
            }
            else {
              if(this.formData.radioVal == "playlist") {
                console.log("플레이리스트에 대한 글 업로드")
              }
              else {
                console.log("플레이룸에 대한 글 업로드")
              }
            }
          }
        }
        // this.createPlaylist(data)
        setTimeout(() => {this.resetBoardPlaylistAddState()}, 1000)
      }
      // 필수항목을 채우지 않은경우.
      else {
        console.log("제목을 입력해주세요.")
      }
    },
    goSelect: function() {
      if(this.formData.radioVal == '') {
        console.log("플레이룸 또는 플레이리스트 선택바람.")
      }
      else {
        this.selectPlaylistOrPlayroom(this.formData.radioVal);
        this.saveFormData(this.formData)
        if(this.formData.radioVal == "playroom") {
          this.$router.push({ name: 'BoardSelectPlayroom' })
          console.log("플레이룸으로가")
        }
        else if(this.formData.radioVal == "playlist") {
          this.$router.push({name: 'BoardSelectPlaylist'})
          console.log("플레이리스트로가")
        }
      }

    }
  }
}
</script>

<style>

</style>
