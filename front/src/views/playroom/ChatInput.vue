<template>
  <v-row>
    <v-text-field
      v-model="message"
      label="메시지를 입력하세요"
      solo
      dense
      :disabled="!canChat"
      :error="errorOnSend"
      @click:append-outer="sendMessage"
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
        <!-- <v-fade-transition leave-absolute> -->
        <v-progress-circular
          v-if="sending"
          size="24"
          indeterminate
        />
        <v-icon
          v-else
          @click="sendMessage"
        >
          mdi-send
        </v-icon>
        <!-- </v-fade-transition> -->
      </template>
    </v-text-field>
  </v-row>
</template>

<script>
export default {
  name: 'ChatInput',
  data() {
    return {
      showEmoji: false,
      sending: false,
      message: '',
      canChat: true,
      errorOnSend: false
    }
  },
  methods: {
    sendMessage() {
      const type = 'TALK'
      const message = this.message
      const token = localStorage.getItem('jwt')

      this.disableChatbox()
      this.pendingToSendMessage()
      this.$store.dispatch('playroom/sendMessage', { type, message, token })
        .then(() => {
          this.clearMessage()
        })
        .catch((err) => {
          this.notifySendError()
        })
        .finally(() => {
          this.completeToSendMessage()
          this.enableChatbox()
        })
    },
    clearMessage() {
      this.message = ''
    },
    pendingToSendMessage() {
      this.sending = true
    },
    completeToSendMessage() {
      this.sending = false
    },
    enableChatbox() {
      this.canChat = true
    },
    disableChatbox() {
      this.canChat = false
    },
    notifySendError() {
      this.errorOnSend = true;
      setTimeout(this.clearSendError, 1000);
    },
    clearSendError() {
      this.errorOnSend = false;
    },
  }
}
</script>

<style>

</style>
