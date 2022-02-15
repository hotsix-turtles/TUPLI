<template>
  <div>
    <div class="d-flex flex-column align-center">
      <notice-item
        v-for="notice in notices"
        :key="notice.id"
        :notice="notice"
      />
    </div>
  </div>
</template>

<script>
import firebase from 'firebase/compat/app';
import 'firebase/compat/database';
import { mapState } from 'vuex'

import NoticeItem from './NoticeItem.vue'
import axiosConnector from '../../utils/axios-connector';

export default {
  name: 'NoticeList',
  components: {
    NoticeItem
  },
  data: function() {
    return {
      notices: [],
    }
  },
  computed: {
    ...mapState(['userId'])
  },
  created: function() {
    this.notiGet()
  },
  methods: {
    // 해당 유저의 알람 전부 가져오기
    notiGet: function() {
      console.log(this.userId)
      firebase
        .database()
        .ref(`tupli/${this.userId}`)  // 해당 유저 번호로 받아오기 (이메일이나 유니크하면 다 가능)
        .limitToLast(20)  // 상위 20개만
        .on('value', (snap) => {
          let res = snap.val();
          // this.noti = [];
          for (const idx in res) {
            res[idx].id = idx;
            this.notices.unshift(res[idx]);
          }
        });
      console.log('알람', this.notices)
      console.log('길이', this.notices.length)
      // 해당 유저의 알람 전부 읽은 것으로 처리
      axiosConnector.get(`noti/readAll/${this.userId}`)
    },
  }

}
</script>

<style>

</style>
