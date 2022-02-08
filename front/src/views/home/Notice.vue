<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          color="#5B5C9D"
          size="30"
          @click="$router.push({ name: 'Home' })"
        >
          mdi-chevron-left
        </v-icon>
        <h3>
          알림
        </h3>
      </v-row>
    </v-container>
    <notice-list />
  </v-app>
</template>

<script>
import firebase from 'firebase/compat/app';
import 'firebase/compat/database';
import { mapState } from 'vuex'
import NoticeList from '@/components/notice/NoticeList.vue'

export default {
  name: 'Notice',
  components: { NoticeList },
  data: function() {
    return {
      noti: []
    }
  },
  computed: {
    ...mapState(['userId'])
  },
  methods: {
    // 해당 유저의 알람 전부 가져오기
    notiGet: function() {
      firebase
        .database()
        .ref(`tupli/${this.userId}`)  // 해당 유저 번호로 받아오기 (이메일이나 유니크하면 다 가능)
        .limitToLast(20)  // 상위 20개만
        .on('value', (snap) => {
          let res = snap.val();
          this.noti = [];
          for (const idx in res) {
            res[idx].id = idx;
            this.noti.unshift(res[idx]);
          }
        });
    },
  }
}
</script>

<style>

</style>
