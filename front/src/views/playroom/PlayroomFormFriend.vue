<template>
  <v-card
    class="playroom mx-auto overflow-hidden mb-10"
    height="100%"
  >
    <!-- 하단 네비게이션 (팔로우 목록 조작) -->
    <v-bottom-navigation
      absolute
      background-color="#5B5C9D"
      height="75px"
      class="fixed-bottom"
      :input-value="addedFriends.length > 0 || selectedFriends.length > 0"
    >
      <!-- 선택된 계정 개수 뱃지 -->
      <v-badge
        :content="selectedFriends.length"
        color="#EAEAEA"
        offset-x="20"
        offset-y="20"
        overlap
        class="videoCounter"
      />

      <!-- 추가하기 버튼 -->
      <NavButton
        color="white"
        content="추가"
        icon="mdi-plus"
        @click="requestAddFriends"
      />
    </v-bottom-navigation>

    <!-- 플레이룸 페이지 -->
    <v-sheet
      id="scroll-threshold-example"
      class="overflow-y-auto"
    >
      <back :page-name="pageName" />
      <!-- <search-bar
        :label="'초대할 친구를 검색해주세요'"
        :is-detail="true"
        @input-change="onEnterSearch"
      /> -->

      <!-- 탭 -->
      <v-tabs
        v-model="tab"
        background-color="transparent"
        grow
        class="mt-14"
      >
        <v-tab
          v-for="item in items"
          :key="item"
        >
          {{ item }}
        </v-tab>
      </v-tabs>

      <!-- 탭에 따른 결과물 -->
      <v-tabs-items v-model="tab">
        <v-tab-item
          v-for="(item, idx) in items"
          :key="idx"
        >
          <v-container fluid>
            <account-list-item-small
              v-if="searchKeyword"
              :accounts="idx ? cofollowerFriends.filter(friend => friend.nickname.search(`.*${searchKeyword}.*`)) : followerFriends.filter(friend => friend.nickname.search(`.*${searchKeyword}.*`))"
            />
            <account-list-item-small
              v-else
              :accounts="idx ? cofollowerFriends : followerFriends"
            />
          </v-container>
        </v-tab-item>
      </v-tabs-items>
    </v-sheet>
  </v-card>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import Back from '../../components/common/Back.vue'
// import SearchBar from '../../components/common/SearchBar.vue'

import NavButton from '../../components/common/NavButton.vue'
import axiosConnector from '../../utils/axios-connector';
import AccountListItemSmall from '../../components/account/AccountListItemSmall.vue'

export default {
  name: 'PlayroomFormFriend',
  components: { NavButton, Back, AccountListItemSmall },
  data() {
    return {
      pageName: "친구 추가하기",
      tab: null,
      items: [
        '나를 팔로우한 친구', '나와 맞팔로우한 친구',
      ],
      searchKeyword: ''
    }
  },
  computed: {
    ...mapState('friend', ['addedFriends', 'selectedFriends', 'followerFriends', 'cofollowerFriends']),
    ...mapState(['userId'])
  },
  created: function () {
    if (this.addedFriends.length) {
      this.revokeFriends()
    }
    console.log(this.addedFriends, this.selectedFriends)
  },
  mounted () {
    this.$nextTick(() => {
      this.getUserFriendInfo();
    });
  },
  methods: {
    async getUserFriendInfo() {
      var followerFriends;
      var cofollowerFriends;

      followerFriends = await axiosConnector.get(`/profile/followers/${this.userId}`);
      cofollowerFriends = await axiosConnector.get(`/profile/cofollowers/${this.userId}`);

      if (followerFriends) this.setFollowerFriend(followerFriends)
      if (cofollowerFriends) this.setCofollowerFriend(cofollowerFriends)
    },
    async requestAddFriends() {
      await this.addFriends()
      this.$router.go(-1)
    },
    onEnterSearch(value) {
      this.searchKeyword = value;
    },
    ...mapActions('friend', ['setFollowerFriend','setCofollowerFriend', 'addFriends', 'revokeFriends'])
  }
}
</script>

<style>

</style>
