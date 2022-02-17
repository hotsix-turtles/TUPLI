<template>
  <v-row
    class="black--text"
  >
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
        size="40"
        @click.stop="SELECT_CHAT_AVATAR(id)"
      >
        <v-img
          :src="ImgUrl(author.thumbnail)"
          :lazy-src="ImgUrl(author.thumbnail)"
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
      <div class="d-flex ml-3 flex-column">
        <div class="d-flex flex-row">
          <p class="font-weight-bold">
            {{ author.name }}
          </p>
          <p class="ml-2 text-caption">
            {{ timeLabel }}
          </p>
        </div>

        <p
          v-if="!blocked"
          class="font-caption mr-1"
          v-html="renderContent"
        />
        <p
          v-else
          class="ml-1 mr-1 blocked"
        >
          [차단됨]
        </p>
      </div>
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
            @click="followUser(author.id)"
          >
            팔로우
          </v-btn>
          <v-btn
            block
            text
            @click="showUserProfile(author.id)"
          >
            유저 프로필
          </v-btn>
          <v-btn
            v-if="!blockedUser"
            block
            text
            @click="blockUser(author.id)"
          >
            유저 차단
          </v-btn>
          <v-btn
            v-else
            block
            text
            @click="unblockUser(author.id)"
          >
            유저 차단 해제
          </v-btn>
          <v-btn
            v-if="userId == roomAuthorId"
            block
            text
            @click="kickUser(author.id)"
          >
            유저 강퇴
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex';
import { getImage } from '../../utils/utils'

export default {
  name: "ChatItem",
  props: {
    id: { type: String, default: '' },
    author: { type: Object, default() {} },
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
    renderContent() {
      const tagsToReplace = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;'
      };
      const escapedContent = this.content.replace(/[&<>]/g, (tag) => tagsToReplace[tag] || tag);

      return [...this.content.matchAll(new RegExp(/\#\d+/g))]
        .map(a => a[0])
        .reduce((prev,cur) => prev = prev.replace(cur, `<img class="chat-emoticon" src="${this.getImoticon(cur)}" />`), escapedContent)
    },
    timeLabel () {
      //const dt = new Date(this.timestamp);
      const dt = new Date();
      dt.setHours(dt.getHours() + 9);

      return dt.toISOString().substr(11,5);
    },
    blocked () {
      return this.blockedUser || this.blockedMessage;
    },
    selected () {
      return this.roomSelectedChatItem.id == this.id
    },
    ...mapState('playroom', ['roomAuthorId', 'roomSelectedChatItem']),
    ...mapState(['userId'])
  },
  methods: {
    ImgUrl: function(img) {
      return getImage(img)
    },
    showUserProfile(id) {
      this.$router.push(`/profile/${this.author.id}`)
      this.DESELECT_CHAT_ITEM();
    },
    kickUser(id) {
      this.$emit('kick-user', this.author.id);
    },
    getImoticon(content) {
      if (content == '#1' || content == "#헐") {
        return require(`@/assets/emoticons/basic/01_really.png`)
      } else if (content == '#2' || content == "#메롱") {
        return require(`@/assets/emoticons/basic/02_silly.png`)
      } else if (content == '#3' || content == "#짜증") {
        return require(`@/assets/emoticons/basic/03_annoyed.png`)
      } else if (content == '#4' || content == "#놀람") {
        return require(`@/assets/emoticons/basic/04_shocked.png`)
      } else if (content == '#5' || content == "#웃음") {
        return require(`@/assets/emoticons/basic/05_smiley.png`)
      } else if (content == '#6' || content == "#폭소") {
        return require(`@/assets/emoticons/basic/06_ridicule.png`)
      } else if (content == '#7') {
        return require(`@/assets/emoticons/basic/07_wonder.png`)
      } else if (content == '#8') {
        return require(`@/assets/emoticons/basic/08_notgood.png`)
      } else if (content == '#9') {
        return require(`@/assets/emoticons/basic/09_blind.png`)
      } else if (content == '#10') {
        return require(`@/assets/emoticons/basic/10_mad.png`)
      } else if (content == '#11') {
        return require(`@/assets/emoticons/basic/11_thinkbad.png`)
      } else if (content == '#12' || content == "#헤" || content == "#호") {
        return require(`@/assets/emoticons/basic/12_ho.png`)
      } else if (content == '#13') {
        return require(`@/assets/emoticons/basic/13_surrender.png`)
      } else if (content == '#14' || content == "#충격") {
        return require(`@/assets/emoticons/basic/14_shocked2.png`)
      } else if (content == '#15') {
        return require(`@/assets/emoticons/basic/15_wonder2.png`)
      } else if (content == '#16') {
        return require(`@/assets/emoticons/basic/16_laugh.png`)
      } else if (content == '#17') {
        return require(`@/assets/emoticons/basic/17_strange.png`)
      } else if (content == '#18' || content == "#슬픔") {
        return require(`@/assets/emoticons/basic/18_sad.png`)
      } else if (content == '#19' || content == "#죽음") {
        return require(`@/assets/emoticons/basic/19_dead.png`)
      } else if (content == '#20' || content == "#수면") {
        return require(`@/assets/emoticons/basic/20_sleep.png`)
      } else if (content == '#21') {
        return require(`@/assets/emoticons/basic/21_happysilly.png`)
      } else if (content == '#22' || content == "#오") {
        return require(`@/assets/emoticons/basic/22_what.png`)
      } else if (content == '#23') {
        return require(`@/assets/emoticons/basic/23_laughter.png`)
      } else if (content == '#24') {
        return require(`@/assets/emoticons/basic/24_suspicious.png`)
      } else if (content == '#25') {
        return null
      }
    },
    ...mapMutations('playroom', ['SELECT_CHAT_ITEM', 'SELECT_CHAT_AVATAR', 'DESELECT_CHAT_ITEM']),
    ...mapActions('playroom', [ 'followUser', 'blockUser', 'blockMessage', 'unblockUser', 'unblockMessage'])
  }
}
</script>

<style>
.blocked {
  color: #bbb
}
.chat-emoticon {
  width: 20px;
  height: 20px;
}
</style>
