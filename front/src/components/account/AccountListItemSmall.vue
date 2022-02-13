<template>
  <div>
    <div
      v-for="(account, idx) in accounts"
      :key="idx"
    >
      <!-- <div>{{ account.profileImage }}</div> 나중(+프로필로 이동하기 추가) -->
      <div class="d-flex align-center my-2">
        <div>
          <img
            class="profile"
            src="https://pbs.twimg.com/media/E4fuVa0WUAAlO1D.jpg"
          >
        </div>
        <div class="ml-3 d-flex-column justify-center">
          <div class="font-2 semi-bold">
            {{ account.username }}
          <!-- {{ account.nickname }} -->
          </div>
          <div class="color-dark-gray">
            {{ account.email }}
          </div>
          <div class="color-dark-gray">
            팔로워 {{ account.followerCnt }}명
          </div>
        </div>
      </div>
      <hr class="hr">
    </div>
    <!-- {{ accounts }} -->
  </div>
</template>

<script>
export default {
  name: 'AccountListItemSmall',
  props: {
    accounts: { type: Array, default() { [] } },
  },
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
