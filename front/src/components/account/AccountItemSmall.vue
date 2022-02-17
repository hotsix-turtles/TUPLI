<template>
  <div>
    <v-card
      class="d-flex align-center my-2"
      :color="color"
      outlined
      @click.stop="clickFriend"
    >
      <div>
        <img
          class="profile"
          :src="ImgUrl(account.profileImage)"
          @click="redirectProfile"
        >
      </div>
      <div class="ml-3 d-flex-column justify-center">
        <div
          class="font-2 semi-bold"
          @click="redirectProfile"
        >
          {{ account.nickname }}
        </div>
        <div
          class="color-dark-gray"
          @click="redirectProfile"
        >
          {{ account.email }}
        </div>
        <div class="color-dark-gray">
          팔로워 {{ account.followerCnt }}명
        </div>
      </div>
    </v-card>
    <hr
      v-if="underline"
      class="hr"
    >
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import { getImage } from '../../utils/utils'

export default {
  name: 'AccountListItemSmall',
  props: {
    account: { type: Object, default() { {} } },
    readonly: { type: Boolean, default: false },
    underline: { type: Boolean, default: false }
  },
  data() {
    return {
      selected: false,
    }
  },
  computed: {
    color() {
      return !this.readonly && this.selected ? "#dde" : "white"
    },
    // addedFriend => addedAccounts로 통일 필요.. 하겠지만 일단 구현 우선
    ...mapState('friend', ['addedFriends', 'selectedFriends']),
  },
  created() {
    this.selected = Boolean(this.selectedFriends && this.selectedFriends
      .find(selectedFriend => selectedFriend.userSeq == this.account.userSeq))
    console.log(this.playlist)
  },
  methods: {
    redirectProfile() {
      if (!this.readonly) return;
      this.$router.push({ name: 'Profile', params: { userId : this.account.userSeq }})
    },
    clickFriend() {
      if (this.readonly) return;
      if (this.selected) {
        this.deselectFriend(this.account)
        this.selected = false;
      } else {
        this.selectFriend(this.account)
        this.selected = true;
      }
    },
    ImgUrl: function(img) {
      return getImage(img)
    },
    ...mapActions('friend', [
      'selectFriend',
      'deselectFriend',
    ]),
  }
}
</script>

<style>
.profile {
  border-radius: 100%;
  border: solid 3px;
  border-color: lightgrey;
  width: 110px;
  height: 110px;
}
</style>
