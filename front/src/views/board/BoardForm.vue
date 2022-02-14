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
              v-model="formData.content"
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
                공유하고 싶은 게시물을 1개 선택합니다.(선택 사항)
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

            <div class="d-flex">
              <!-- 선택하기 버튼 -->
              <v-btn
                color="accent"
                elevation="2"
                @click="goSelect"
              >
                선택하기
              </v-btn>
              <!-- 플레이리스트나 플레이룸이 선택되지 않고 완료버튼을 눌렀을 떄 -->
              <div
                v-if="!boardValid && formData.radioVal === 'playlist' && chosenPlaylist.id === 0"
                color="red"
              >
                플레이리스트를 1개 선택해주세요.
              </div>
              <div
                v-if="!boardValid && formData.radioVal === 'playroom' && chosenPlayroom.id === 0"
                color="red"
              >
                플레이룸을 1개 선택해주세요.
              </div>
            </div>
          </v-col>
          <!-- 공유할 게시물 -->
          <div
            v-if="formData.isBoard"
            class="container"
          >
            <div
              v-if="formData.radioVal === 'playlist'"
            >
              {{ chosenPlaylist.id }}
              <div
                v-if="chosenPlaylist.id > 0"
                class="container added"
              >
                <playlist-item-big
                  :playlist="chosenPlaylist"
                />
              </div>
              <v-col />
            </div>
            <div
              v-else
            >
              <div
                v-if="chosenPlayroom.id > 0"
                class="container added"
              >
                {{ chosenPlayroom.id }}
                <playroom-item-big
                  :playroom="chosenPlayroom"
                />
              </div>
              <v-col />
            </div>
          </div>
        </v-row>
      </v-container>
    </v-form>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
import PlayroomItemBig from '../../components/playroom/PlayroomItemBig.vue'
import PlaylistItemBig from '../../components/playlist/PlaylistItemBig.vue'


export default {
  name: 'BoardForm',
  components: {
    Back,
    PlayroomItemBig,
    PlaylistItemBig,
  },
  data: function() {
    return {
      pageName: "게시글 작성",
      formData: {
        content: '',
        isBoard: false,
        radioVal: 'playroom',
      },
      valid: false,
      rules: {
        required: v => !!v || '필수 입력값입니다.',
        counterMax300: v => v.length <= 300 || '내용은 300글자 이상 작성할 수 없습니다.',
      },
      boardValid: true,
    }
  },
  computed: {
    ...mapState('board', {
      savedFormData: state => state.savedFormData,
      isSaved: state => state.isSaved,
      chosenPlaylist: state => state.chosenPlaylist,
      chosenPlayroom: state => state.chosenPlayroom
    }),
  },
  created: function() {
    if (this.isSaved) {
      this.formData = this.savedFormData
      console.log("내가 가져온 플레이리스트는", this.chosenPlaylist)
    } else {
      this.resetVideoAddState()
      console.log("boardForm첨왔어요")
    }
  },
  methods: {
    ...mapActions('board', [
      'saveFormData',
      'resetBoardPlaylistAddState',
      'selectPlaylistOrPlayroom',
      'createBoard',
    ]),
    onClickCompletion: function () {
      // 필수항목을 채웠을 경우
      if (this.valid) {
        const formData = {
          content: this.formData.content,
          type: null,
          typeId: 0,
        }
        // 순수 글인 경우
        if(!this.formData.isBoard) {
          console.log("순수 글 업로드")
          this.createBoard(formData)
        }
        // 플레이룸 or 플레이리스트에 대한 글
        else {
          formData.type = this.formData.radioVal
          formData.typeId = formData.type === 'playlist' ? this.chosenPlaylist.id : this.chosenPlayroom.id
          if(formData.typeId) {
            this.createBoard(formData)
          }
          else {
            this.boardValid = false
          }
        }
        setTimeout(() => {this.resetBoardPlaylistAddState()}, 1000)
      }
    },
    goSelect: function() {
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
</script>

<style>
  .added {
    border: solid 1px;
    border-color: #d8d8ee;
  }
</style>
