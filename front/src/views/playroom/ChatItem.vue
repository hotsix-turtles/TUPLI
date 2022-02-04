<template>
  <v-row>
    <v-col
      height="60px"
<<<<<<< HEAD
      @click.stop="selectChatItem(id)"
=======
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
      cols="12"
      md="4"
      class="d-flex align-center pa-1"
      style="width: 100%"
<<<<<<< HEAD
=======
      @click.stop="selectChatItem(id)"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    >
      <v-avatar
        circle
        @click.stop="selectChatAvatar(id)"
      >
<<<<<<< HEAD
        <img
          :src="profile"
          class="pa-1"
        >
=======
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
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
      </v-avatar>
      <p class="font-3 ml-1 font-weight-bold">
        {{ name }}
      </p>
<<<<<<< HEAD
      <p class="font-3 ml-1" v-if="!blocked">
        {{ content }}
      </p>
      <p class="font-3 ml-1 blocked" v-else>
=======
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
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
      @click:outside="deselectChatItem"
    >
      <v-card v-if="roomSelectedChatItem.type == 'CHAT_ITEM'">
        <v-card-title>
          <span>Message Actions</span>
        </v-card-title>
        <v-card-actions>
          <v-btn
<<<<<<< HEAD
            block
            text
            @click="blockMessage(id)"
            v-if="!blockedMessage"
=======
            v-if="!blockedMessage"
            block
            text
            @click="blockMessage(id)"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
          >
            메시지 차단
          </v-btn>
          <v-btn
<<<<<<< HEAD
            block
            text
            @click="unblockMessage(id)"
            v-else
=======
            v-else
            block
            text
            @click="unblockMessage(id)"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
<<<<<<< HEAD
            block
            text
            @click="blockUser(id)"
            v-if="!blockedUser"
=======
            v-if="!blockedUser"
            block
            text
            @click="blockUser(id)"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
          >
            유저 차단
          </v-btn>
          <v-btn
<<<<<<< HEAD
            block
            text
            @click="unblockUser(id)"
            v-else
=======
            v-else
            block
            text
            @click="unblockUser(id)"
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
<<<<<<< HEAD
    id: { type: Number, default: -1 },
=======
    id: { type: String, default: '' },
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
    ...mapMutations('playroom', ['selectChatItem', 'selectChatAvatar', 'deselectChatItem']),
    ...mapActions('playroom', [ 'followUser', 'showUserProfile', 'blockUser', 'blockMessage', 'unblockUser', 'unblockMessage' ])
  }
}
</script>

<style>
.blocked {
  color: #bbb
}
</style>
