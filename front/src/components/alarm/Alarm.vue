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
        console.log('handler 알림', val)
        console.log('handler 알림 - toId :', toId)
        console.log('handler 알림 - userId :', userId)
        console.log('handler 알림 - val.isRead :', val.isRead)
        console.log('handler 알림 - this.realtimeBoolean :', this.realtimeBoolean)
        console.log('handler 알림 - realtimeNoti :', realtimeNoti)
        // this.newAlarms = val;
        if ((val.toId == this.userId) && (val.isRead == false) ) {this.realtimeNoti(val)}
        // if ((val.toId == this.userId) && (val.isRead == false) && (this.realtimeBoolean == false) ) {this.realtimeNoti(val)}
      }
    }
  },
  methods: {
    realtimeNoti: function(val) {
      console.log('실시간 알림 - val :', val)
      console.log('실시간 알림 - toId :', toId)
      console.log('실시간 알림 - userId :', userId)
      console.log('실시간 알림 - val.isRead :', val.isRead)
      console.log('실시간 알림 - this.realtimeBoolean :', this.realtimeBoolean)
      console.log('실시간 알림 - realtimeNoti :', realtimeNoti)
      // 여러 번 읽기 방지(프론트)
      const tmp = {}
      tmp.isRead = true
      this.$store.commit('SET_REALTIME_ALARM', tmp);
      console.log('realtimeNoti - val :', val)
      console.log('realtimeNoti - toId :', toId)
      console.log('realtimeNoti - userId :', userId)
      console.log('realtimeNoti - val.isRead :', val.isRead)
      console.log('realtimeNoti - this.realtimeBoolean :', this.realtimeBoolean)
      console.log('realtimeNoti - realtimeNoti :', realtimeNoti)

      // 여러 번 읽기 방지(백)
      axios({
        method: 'GET',
        url: SERVER.URL + '/noti/reset',
      })
      console.log('axios 후 - val :', val)


      // XX님이 팔로우하셨습니다.
      if(val.type == "follow") {
        console.log('if 문 다음 : val', val)
        console.log('follow - toId :', toId)
        console.log('follow - userId :', userId)
        console.log('follow - val.isRead :', val.isRead)
        console.log('follow - this.realtimeBoolean :', this.realtimeBoolean)
        console.log('follow - realtimeNoti :', realtimeNoti)
        swal.fire({
          position: 'bottom-end',
          icon: 'info',
          title: '팔로우',
          text: val.from + '님이 회원님을 팔로우 하였습니다.',
          scrollbarPadding: false,
          showConfirmButton: false,
          timer: 1800
        })
        console.log('swert 알림')

      // XX님이 플레이룸을 만드셨습니다.
      } else if (val.type == "playroomMake") {
        console.log('else if 문 1 다음 : val', val)
        console.log('playroomMake - toId :', toId)
        console.log('playroomMake - userId :', userId)
        console.log('playroomMake - val.isRead :', val.isRead)
        console.log('playroomMake - this.realtimeBoolean :', this.realtimeBoolean)
        console.log('playroomMake - realtimeNoti :', realtimeNoti)
        swal.fire({
          position: 'bottom-end',
          icon: 'info',
          title: '플레이룸',
          text: val.from + '님이 회원님을 플레이룸을 만드셨습니다.',
          scrollbarPadding: false,
          showConfirmButton: false,
          timer: 1800
        })
      // XX님이 회원님을 플레이룸으로 초대하셨습니다.
      } else if (val.type == "invite") {
        console.log('else if 문 2 다음 : val', val)
        console.log('invite - toId :', toId)
        console.log('invite - userId :', userId)
        console.log('invite - val.isRead :', val.isRead)
        console.log('invite - this.realtimeBoolean :', this.realtimeBoolean)
        console.log('invite - realtimeNoti :', realtimeNoti)

        swal.fire({
          position: 'bottom-end',
          icon: 'info',
          title: '플레이룸',
          text: val.from + '님이 회원님을 플레이룸에 초대하였습니다.',
          scrollbarPadding: false,
          showConfirmButton: false,
          timer: 1800
        })

      }
    },


  }

}
</script>

<style scoped>

</style>
