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

    <div class="d-flex align-center justify-space-between mx-10">
      <h4>알림 여부</h4>
      <v-switch
        v-model="newAlarmSetting"
        class="pl-3"
        inset
        dense
        color="#5B5C9D"
      />
    </div>

    <div
      v-if="newAlarmSetting"
      class="d-flex flex-column mx-10 mt-3"
    >
      <h4>알림 종류</h4>
      <div class="mt-2">
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            실시간 알림 받기
          </p>
          <v-switch
            v-model="newAlarmOnRealtime"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            초대 받기
          </p>
          <v-switch
            v-model="newAlarmOnInvite"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div
          v-if="newAlarmOnInvite"
          class="d-flex flex-column align-start mx-4"
        >
          <h4>초대 가능한 사람</h4>
          <v-radio-group
            v-model="newInviteDomain"
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
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            플레이룸 생성시 팔로워에게 알림
          </p>
          <v-switch
            v-model="newAlarmOnPlayroomMake"
            class="pl-3 mt-0"
            inset
            dense
            color="#5B5C9D"
          />
        </div>
        <div class="d-flex align-center justify-space-between">
          <p class="mb-0">
            뱃지 획득시 알림 받기
          </p>
          <v-switch
            v-model="newAlarmOnBadge"
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
import axiosConnector from '@/utils/axios-connector.js'
import { mapState } from 'vuex'

export default {
  name: 'InviteNotice',
  data: function () {
    return {
      newAlarmSetting: true,  // 알림 여부      
      newAlarmOnRealtime: true,  // 실시간 알림 받기
      newAlarmOnInvite: true, // 초대 받기
      newInviteDomain: 'everyone', //초대 가능한 사람
      newAlarmOnPlayroomMake: true,  // 플레이룸 생성시 팔로워에게 알람 보낼지 여부
      newAlarmOnBadge: true,
    }
  },
  computed: {
    ...mapState(['alarmSetting', 'alarmOnRealtime', 'alarmOnInvite', 'inviteDomain', 'alarmOnPlayroomMake', 'alarmOnBadge'])
  },
  created: function () {
    this.initSetting()
  },
  beforeDestroy() {
    this.sendSetting()
  },
  methods: {
    // 기본 설정
    initSetting: function() {
      this.newAlarmSetting = this.alarmSetting
      this.newAlarmOnRealtime = this.alarmOnRealtime
      this.newAlarmOnInvite = this.alarmOnInvite
      this.newInviteDomain = this.inviteDomain
      this.newAlarmOnPlayroomMake = this.alarmOnPlayroomMake
      this.newAlarmOnBadge = this.alarmOnBadge
    },
    // 설정 변경 여부와 상관없이 보내기
    sendSetting: function() {
      const data = {
        alarmSetting: this.newAlarmSetting,
        alarmOnRealtime: this.newAlarmOnRealtime,
        alarmOnInvite: this.newAlarmOnInvite,
        inviteDomain: this.newInviteDomain,
        alarmOnPlayroomMake: this.newAlarmOnPlayroomMake,
        alarmOnBadge: this.newAlarmOnBadge,
      }
      axiosConnector.put('/account/setting', data)
        .then(() => {
          // 설정 정보 갱신
          this.$store.commit('GET_USER_SETTING', data)
        })
        .catch((err) => {
          console.log(err.response.data)
        })
    },
  }
}
</script>

<style>

</style>
