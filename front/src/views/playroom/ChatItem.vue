<template>
  <v-row>
    <v-col
      height="60px"
      cols="12"
      md="4"
      class="d-flex align-center pa-1"
      style="width: 100%"
      @click.stop="SELECT_CHAT_ITEM(id)"
    >
      <v-avatar
        circle
        @click.stop="SELECT_CHAT_AVATAR(id)"
      >
        <v-img
          :src="profile"
          :lazy-src="profile"
          class="pa-1"
        >
          <template v-slot:placeholder>
            <v-row
              class="fill-height ma-0"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="grey lighten-5"
              />
            </v-row>
          </template>
        </v-img>
      </v-avatar>
      <p class="font-3 ml-1 font-weight-bold">
        {{ name }}
      </p>
      <p
        v-if="!blocked"
        class="font-3 ml-1"
      >
        {{ content }}
      </p>
      <p
        v-else
        class="font-3 ml-1 blocked"
      >
        [차단됨]
      </p>
      <p class="font-3 ml-auto mr-1">
        {{ timeLabel }}
      </p>
    </v-col>
    <v-dialog
      v-model="selected"
      hide-overlay
      max-width="500px"
      @click:outside="DESELECT_CHAT_ITEM"
    >
      <v-card v-if="roomSelectedChatItem.type == 'CHAT_ITEM'">
        <v-card-title>
          <span>Message Actions</span>
        </v-card-title>
        <v-card-actions>
          <v-btn
            v-if="!blockedMessage"
            block
            text
            @click="blockMessage(id)"
          >
            메시지 차단
          </v-btn>
          <v-btn
            v-else
            block
            text
            @click="unblockMessage(id)"
          >
            메시지 차단 해제
          </v-btn>
        </v-card-actions>
      </v-card>
      <v-card v-else-if="roomSelectedChatItem.type == 'CHAT_AVATAR'">
        <v-card-title>
          <span>User Actions</span>
        </v-card-title>
        <v-card-actions class="d-flex flex-column">
          <v-btn
            block
            text
            @click="followUser(id)"
          >
            팔로우
          </v-btn>
          <v-btn
            block
            text
            @click="showUserProfile(id)"
          >
            유저 프로필
          </v-btn>
          <v-btn
            v-if="!blockedUser"
            block
            text
            @click="blockUser(id)"
          >
            유저 차단
          </v-btn>
          <v-btn
            v-else
            block
            text
            @click="unblockUser(id)"
          >
            유저 차단 해제
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex';

export default {
  name: "ChatItem",
  props: {
    id: { type: String, default: '' },
    name: { type: String, default: '' },
    profile: { type: String, default: '' },
    content: { type: String, default: '' },
    timestamp: { type: Number, default: 0 },
    blockedUser: { type: Boolean, default: false },
    blockedMessage: { type: Boolean, default: false }
  },
  data () {
    return {
    }
  },
  computed: {
    timeLabel () {
      //const dt = new Date(this.timestamp);
      const dt = new Date();
      return `${dt.getHours() < 12 ? "오전" : "오후"} ${dt.getHours() % 12}:${dt.getMinutes()}`;
    },
    blocked () {
      return this.blockedUser || this.blockedMessage;
    },
    selected () {
      return this.roomSelectedChatItem.id == this.id
    },
    ...mapState('playroom', ['roomSelectedChatItem'])
  },
  methods: {
    ...mapMutations('playroom', ['SELECT_CHAT_ITEM', 'SELECT_CHAT_AVATAR', 'DESELECT_CHAT_ITEM']),
    ...mapActions('playroom', [ 'followUser', 'showUserProfile', 'blockUser', 'blockMessage', 'unblockUser', 'unblockMessage' ])
  }
}
</script>

<style>
.blocked {
  color: #bbb
}
</style>
