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
        console.log('env 작동 chk', val)
        // this.newAlarms = val;
        if ((val.toId == this.userId) && (val.isRead == false) && (this.realtimeBoolean == false) ) {this.realtimeNoti(val)}
      }
    }
  },
  methods: {
    realtimeNoti: function(val) {
      // 여러 번 읽기 방지(프론트)
      const tmp = {}
      tmp.isRead = true
      this.$store.commit('SET_REALTIME_ALARM', tmp);

      // 여러 번 읽기 방지(백)
      axios({
        method: 'GET',
        url: SERVER.URL + '/notiReset',
      })

      // XX님이 팔로우하셨습니다.
      if(val.type == "follow") {
        swal.fire({
          position: 'bottom-end',
          icon: 'info',
          title: '팔로우',
          text: val.from + '님이 회원님을 팔로우 하였습니다.',
          scrollbarPadding: false,
          showConfirmButton: false,
          timer: 1800
        })
      // XX님이 플레이룸을 만드셨습니다.
      } else if (val.type == "playroomMake") {
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
