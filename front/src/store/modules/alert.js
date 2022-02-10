import Vue from 'vue'
import firebase from 'firebase/compat/app';
import 'firebase/compat/database';

const alert = {
  state: {
    // 실시간 알람
    realtimeAlarmList: null,
    realtimeBoolean:false  // 플레이룸 등 특정 상황에서 비활성화
  },

  getters: {
    getRealtimeAlarmList: function(state) {
      return state.realtimeAlarmList;
    }
  },

  mutations: {
    // 가져온 알람 갱신
    SET_REALTIME_ALARM(state, res) {
      state.realtimeAlarmList = res;
    },
  },

  actions: {
    // 실시간 알람 가져오기 (로그인 등 이후에 호출할 것!)
    getRealtimeAlarm({state, commit}) {
      firebase
        .database()
        .ref('tupli/realtime')  // 기초 버전 : 전부 다 받는 버전
        .limitToLast(20)
        .on('value', (snap) => {
          let res = snap.val()
          const tmp = {}
          tmp.from = res.from
          tmp.fromId = res.fromId
          tmp.img = res.image
          tmp.to = res.to
          tmp.toId = res.toId
          tmp.type = res.type
          tmp.isRead = false
          commit('SET_REALTIME_ALARM', tmp);
        });
    },
  }
}

export default alert
