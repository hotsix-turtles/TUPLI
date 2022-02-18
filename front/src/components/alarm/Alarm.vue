<template>
  <div />
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import swal from 'sweetalert2'
import axios from 'axios'
import SERVER from '@/api/server'

export default {
  name: 'Alarm',
  data() {
    return {
      newComments: [],
    }
  },
  computed: {
    ...mapState(['nickname', 'realtimeBoolean', 'userId']),
    ...mapGetters({
      realtime: ['getRealtimeAlarmList']
    })
  },
  watch: {
    realtime: {
      immediate: true,
      handler(val) {
        if ((val.toId == this.userId) && (val.isRead == false) && (this.realtimeBoolean == false) ) {
          this.realtimeNoti(val)
        }
        // console.log('handler 알림 후', val)
        // console.log('1차체크', (val.toId == this.userId))
        // console.log('2차체크', (val.isRead == false))
        // console.log('3차체크', (this.realtimeBoolean == false))
        // console.log('handler 알림 - toId :', val.toId)
        // console.log('handler 알림 - userId :', this.userId)
        // console.log('handler 알림 - val.isRead :', val.isRead)
        // console.log('handler 알림 - this.realtimeBoolean :', this.realtimeBoolean)
        // this.newAlarms = val;
        if ((val.toId == this.userId) && (val.isRead == false) ) {this.realtimeNoti(val)}
        // if ((val.toId == this.userId) && (val.isRead == false) && (this.realtimeBoolean == false) ) {this.realtimeNoti(val)}
      }
    }
  },
  methods: {
    realtimeNoti: function(val) {
      // console.log('실시간 알림 - val :', val)
      // 여러 번 읽기 방지(프론트)
      const tmp = {}
      tmp.isRead = true
      this.$store.commit('SET_REALTIME_ALARM', tmp);
      // console.log('realtimeNoti - val :', val)

      // 여러 번 읽기 방지(백)
      axios({
        method: 'GET',
        url: SERVER.URL + '/noti/reset',
      })
      // console.log('axios 후 - val :', val)


      // XX님이 팔로우하셨습니다.
      if(val.type == "follow") {
        this.$store.dispatch('setFollowDialogTitle', `${val.from}님이 회원님을 팔로우 하였습니다.`);
        this.$store.dispatch('showFollowDialog');
        // swal.fire({
        //   position: 'bottom-end',
        //   icon: 'info',
        //   title: '팔로우',
        //   text: val.from + '님이 회원님을 팔로우 하였습니다.',
        //   scrollbarPadding: false,
        //   showConfirmButton: false,
        //   timer: 1800
        // })
        // console.log('swert 알림')

      // XX님이 플레이룸을 만드셨습니다.
      } else if (val.type == "playroomMake") {
        // console.log('else if 문 1 다음 : val', val)
        this.$store.dispatch('setFollowDialogTitle', `${val.form}님이 회원님을 플레이룸을 만드셨습니다.`);
        this.$store.dispatch('showFollowDialog');
        // swal.fire({
        //   position: 'bottom-end',
        //   icon: 'info',
        //   title: '플레이룸',
        //   text: val.from + '님이 회원님을 플레이룸을 만드셨습니다.',
        //   scrollbarPadding: false,
        //   showConfirmButton: false,
        //   timer: 1800
        // })
      // XX님이 회원님을 플레이룸으로 초대하셨습니다.
      } else if (val.type == "invite") {
        // console.log('else if 문 2 다음 : val', val)
        this.$store.dispatch('setFollowDialogTitle', `${val.form}님이 회원님을 플레이룸에 초대하였습니다.`);
        this.$store.dispatch('showFollowDialog');
        // swal.fire({
        //   position: 'bottom-end',
        //   icon: 'info',
        //   title: '플레이룸',
        //   text: val.from + '님이 회원님을 플레이룸에 초대하였습니다.',
        //   scrollbarPadding: false,
        //   showConfirmButton: false,
        //   timer: 1800
        // })

      }
      // console.log('완전 종료 후 - val :', val)
    },


  }

}
</script>

<style scoped>

</style>
