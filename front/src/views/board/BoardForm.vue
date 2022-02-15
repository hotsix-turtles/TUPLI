<template>
  <div>
    <!-- 뒤로가기/완료 or 수정 -->
    <div class="d-flex justify-space-between fixed-top light-background">
      <div class="d-flex mx-3 my-3">
        <div>
          <v-icon @click="checkResetData">
            mdi-arrow-left
          </v-icon>
        </div>
        <div class="font-2 semi-bold center">
          {{ pageName }}
        </div>
      </div>
      <div
        v-if="formType === 'create'"
        class="clickable font-2 semi-bold mt-3 mr-3 color-dark-gray"
        :class="{ 'color-main': valid && boardValid }"
        @click="onClickCompletion"
      >
        완료
      </div>
      <div
        v-else
        class="clickable"
        @click="onClickCompletion"
      >
        수정
      </div>
    </div><br><br>

    <!-- 게시글 생성 폼-->
    <v-form
      v-model="valid"
      class="mt-5"
    >
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
              :rules="[rules.required, rules.counterMax600]"
              :counter="600"
              label="게시글 내용을 입력해주세요."
              required
              color="accent"
            />
          </v-col>

          <!-- 플레이룸or플레이리스트 로 만들기 (선택사항) -->
          <v-col
            class="d-flex justify-space-between py-0 align-center"
            cols="12"
            md="4"
          >
            <div class="d-flex-column">
              <div class="semi-bold">
                플레이룸/플레이리스트
              </div>
              <div class="font-4 mr-auto color-dark-gray">
                공유하고 싶은 게시물을 1개 선택합니다.(선택 사항)
              </div>
            </div>

            <v-switch
              v-model="formData.isBoard"
              color="#5B5C9D"
            />
          </v-col>

          <!-- 플레이룸인지 플레이리스트인지 선택하기 -->
          <div
            v-if="formData.isBoard"
            class="ml-2"
          >
            <v-radio-group
              v-model="formData.radioVal"
              column
            >
              <v-radio
                label="플레이룸"
                value="playroom"
                color="#5B5C9D"
                class="mb-3"
              />
              <v-radio
                label="플레이리스트"
                value="playlist"
                color="#5B5C9D"
              />
            </v-radio-group>

            <div class="d-flex mt-2 mb-3 align-center">
              <!-- 선택하기 버튼 -->
              <v-btn
                color="accent"
                elevation="2"
                @click="goChoose"
              >
                선택하기
              </v-btn>
              <!-- 플레이리스트나 플레이룸이 선택되지 않고 완료버튼을 눌렀을 떄 -->
              <div class="ml-2 color-red font-3">
                <div
                  v-if="!boardValid && formData.radioVal === 'playlist' && chosenPlaylist.id === 0"
                >
                  플레이리스트를 1개 선택해주세요.
                </div>
                <div
                  v-if="!boardValid && formData.radioVal === 'playroom' && chosenPlayroom.id === 0"
                >
                  플레이룸을 1개 선택해주세요.
                </div>
              </div>
            </div>
          </div>
          <!-- 공유할 게시물 -->
          <div
            v-if="formData.isBoard"
            class="container"
          >
            <div
              v-if="formData.radioVal === 'playlist'"
            >
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
    <!-- 데이터 저장 여부 확인 모달 -->
    <normal-dialog
      title="입력값"
      content-html="기존 입력 데이터가 사라집니다."
      max-width="290"
      persistent
      :buttons="[{ name: '확인', color: '#5B5C9D' }, { name: '취소', color: 'gray' }]"
      :show="isReset"
      @button-click="onClickSaveDataDialog"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import PlayroomItemBig from '../../components/playroom/PlayroomItemBig.vue'
import PlaylistItemBig from '../../components/playlist/PlaylistItemBig.vue'
import NormalDialog from '../../components/common/NormalDialog.vue';


export default {
  name: 'BoardForm',
  components: {
    PlayroomItemBig,
    PlaylistItemBig,
    NormalDialog,
  },
  data: function() {
    return {
      pageName: '게시글 작성',
      formType: 'create',
      formData: {
        content: '',
        isBoard: false,
        radioVal: 'playroom',
      },
      valid: false,
      rules: {
        required: v => !!v || '필수 입력값입니다.',
        counterMax600: v => v.length <= 600 || '내용은 300글자 이상 작성할 수 없습니다.',
      },
      boardValid: true,
      isReset: false,
    }
  },
  computed: {
    ...mapState('board', {
      savedFormData: state => state.savedFormData,
      isSaved: state => state.isSaved,
      chosenPlaylist: state => state.chosenPlaylist,
      chosenPlayroom: state => state.chosenPlayroom,
    }),
  },
  created: function() {
    if (this.isSaved) {
      this.formData = this.savedFormData
      console.log("내가 가져온 플레이리스트는", this.chosenPlaylist)
    } else {
      console.log("boardForm첨왔어요")
      this.resetFormData()
    }
    console.log('this.$route.params.boardId',this.$route.params.boardId)
    if (typeof this.$route.params.boardId === 'undefined') {
      this.formType = 'create'
    } else {
      this.formType = 'update'
    }
  },
  methods: {
    ...mapActions('board', [
      'resetFormData',
      'saveFormData',
      'choosePlaylistOrPlayroom',
      'createBoard',
      'updateBoard',
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
          if (this.formType === 'create') {
            this.createBoard(formData)
          } else {
            const params = {
              id: this.$route.params.boardId,
              formData: formData,
            }
            this.updateBoard(params)
          }
          setTimeout(() => {this.resetFormData()}, 1000)
        }
        // 플레이룸 or 플레이리스트에 대한 글
        else {
          formData.type = this.formData.radioVal
          formData.typeId = formData.type === 'playlist' ? this.chosenPlaylist.id : this.chosenPlayroom.id
          if(formData.typeId) {
            if (this.formType === 'create') {
              this.createBoard(formData)
            } else {
              const params = {
                id: this.$route.params.boardId,
                formData: formData,
              }
              this.updateBoard(params)
            }
            setTimeout(() => {this.resetFormData()}, 1000)
          }
          else {
            this.boardValid = false
          }
        }
      }
    },
    goChoose: function() {
      this.choosePlaylistOrPlayroom(this.formData.radioVal);
      this.saveFormData(this.formData)
      if(this.formData.radioVal == "playroom") {
        this.$router.push({ name: 'BoardSelectPlayroom' })
        console.log("플레이룸으로가")
      }
      else if(this.formData.radioVal == "playlist") {
        this.$router.push({name: 'BoardSelectPlaylist'})
        console.log("플레이리스트로가")
      }
    },
    // 데이터 리셋 여부 확인
    checkResetData: function () {
      if (this.formData.content || this.chosenPlaylist.id > 0 || this.chosenPlayroom.id > 0 ) {
        this.isReset = true
      } else {
        this.$router.go(-1)
      }
    },
    onClickSaveDataDialog: function (idx) {
      if (idx === 0) { // 확인
        this.$router.go(-1)
        this.resetFormData()
      } else { // 취소
        this.isReset = false
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
