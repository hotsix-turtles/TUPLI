<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Setting' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          초대/알림 설정
        </h3>
      </v-row>
    </v-container>
    <div class="d-flex flex-column align-start mx-10">
      <h4>초대 범위</h4>
      <v-radio-group
        v-model="invite"
        class="mt-3"
      >
        <v-radio
          label="모든 사람"
          value="everyone"
          color="#5B5C9D"
        />
        <v-radio
          label="팔로워"
          value="followers"
          color="#5B5C9D"
        />
        <v-radio
          label="맞팔로워"
          value="co-followers"
          color="#5B5C9D"
        />
      </v-radio-group>
    </div>

    <div class="d-flex align-center justify-space-between mx-10">
      <h4>알림 여부</h4>
      <v-switch
        v-model="onAlert"
        class="pl-3"
        inset
        dense
        color="#5B5C9D"
      />
    </div>
    <div
      v-if="onAlert"
      class="d-flex flex-column mx-10 mt-3"
    >
      <h4>알림 종류</h4>
      <div class="mt-2">
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            팔로우
          </p>
          <v-switch
            v-model="onFollow"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            초대
          </p>
          <v-switch
            v-model="onInvite"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            플레이룸 생성
          </p>
          <v-switch
            v-model="onNewPlayroom"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            플레이리스트/플레이룸 추천
          </p>
          <v-switch
            v-model="onRecommendPlay"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
      </div>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'InviteNotice',
  data: function () {
    return {
      invite: 'everyone',
      onAlert: true,
      onFollow: true,
      onInvite: true,
      onNewPlayroom: true,
      onRecommendPlay: true,
    }
  }
}
</script>

<style>

</style>
