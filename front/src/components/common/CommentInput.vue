<template>
  <div class="background">
    <v-text-field
      ref="comment_input"
      v-model="inputVal"
      class="comment-input pt-5 pl-2"
      style="width: 94vw !important;"
      label="댓글을 입력하세요"
      solo
      dense
      background-color="white"
      @keydown.enter="sendComment"
      @click:append-outer="sendComment"
      @click:append="checkIsLogin"
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
          <v-card
            width="100%"
            height="100%"
          >
            <emoji @click="clickEmoji" />
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
    <div class="" />
    <login-dialog
      :show="showLoginDialog"
      @on-click="showLoginDialog = false"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import LoginDialog from '@/components/common/LoginDialog.vue'
import Emoji from '../../components/common/Emoji.vue';

export default {
  name: 'CommentInput',
  components: {
    LoginDialog,
    Emoji,
  },
  props: {
  },
  data: function () {
    return {
      showEmoji: false,
      inputVal: '',
      showLoginDialog: false,
    }
  },
  computed: {
    ...mapState({
      isLogin: state => state.isLogin,
    }),
  },
  methods: {
    sendComment: function (event) {
      if (this.inputVal) {
        if (this.isLogin) {
          this.$emit('send-comment', this.inputVal)
          this.inputVal = ''
        } else {
          console.log("showLoginDialog")
          this.showLoginDialog = true
        }
      }
    },
    checkIsLogin: function () {
      if (!this.isLogin) {
        this.showLoginDialog = true
      }
    },
    clickEmoji: function({ idx, emoticon }) {
      // console.log(idx, emoticon)
      this.inputVal += `#${idx}`;
      this.$nextTick(() => this.$refs.comment_input.focus())
    },
  }
}
</script>

<style scoped>
  .comment-input {
    width: 94vw !important;
    z-index: 1 !important;
    position: fixed !important;
    bottom: 50px !important;
  }

  .background {
    background-color: white;
    z-index: 1 !important;
    position: fixed !important;
    bottom: 0 !important;
    height: "20vh" !important;
    width: 100%;
  }
</style>
