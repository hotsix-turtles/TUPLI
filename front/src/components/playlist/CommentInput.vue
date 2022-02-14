<template>
  <div>
    <v-text-field
      v-model="inputVal"
      class="comment-input"
      label="메시지를 입력하세요"
      solo
      dense
      :disabled="!isLogin"
      @keydown.enter="sendComment"
      @click:append-outer="sendComment"
    >
      <template v-slot:append>
        <v-menu
          v-model="showEmoji"
          rounded="lg"
          top
          left
          offset-x
          offset-y
        >
          <template v-slot:activator="{ on, attrs }">
            <v-icon
              v-if="showEmoji"
              v-bind="attrs"
              v-on="on"
              @click="showEmoji = !showEmoji"
            >
              mdi-emoticon
            </v-icon>
            <v-icon
              v-else
              v-bind="attrs"
              v-on="on"
              @click="showEmoji = !showEmoji"
            >
              mdi-emoticon-outline
            </v-icon>
          </template>
          <v-card>
            <v-list>
              <v-list-item>
                이모지
              </v-list-item>
            </v-list>
          </v-card>
        </v-menu>
      </template>
      <template v-slot:append-outer>
        <v-icon
          @click="sendComment"
        >
          mdi-send
        </v-icon>
      </template>
    </v-text-field>
    <div />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'CommentInput',
  props: {
  },
  data: function () {
    return {
      showEmoji: false,
      inputVal: '',
    }
  },
  computed: {
    ...mapState({
      isLogin: state => state.isLogin,
    })
  },
  methods: {
    sendComment: function (event) {
      if(this.inputVal == '') {
        console.log("CommentInput.vue : 덧글 내용 입력을 해주세요")
        return
      }
      else {
        // console.log("CommentInput.vue 로 왔씁니까?")
        this.$emit('send-comment', this.inputVal)
        this.inputVal = ''
      }
    }
  }
}
</script>

<style scoped>
  .comment-input {
    width: 94vw !important;
    z-index: 20 !important;
    position: fixed !important;
    bottom: 0 !important;
  }

  div {
    background-color: white;
    z-index: 19 !important;
    position: fixed !important;
    bottom: 0 !important;
    height: "20vh" !important;
    width: 100%;
  }
</style>
