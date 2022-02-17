<template>
  <v-app>
    <v-container class="mb-5">
      <v-row
        class="align-center mt-5"
      >
        <v-icon
          size="20"
          color="black"
          class="px-2"
          @click="$router.push({ name: 'Setting'})"
        >
          mdi-arrow-left
        </v-icon>
        <h3>
          관리자 페이지
        </h3>
      </v-row>
    </v-container>

    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>여기서 하신 모든 작업은 로그에 남습니다.</h4>
        <h4>신중하게 작업해주세요.</h4>
        <br>
        <h4>
          유저 삭제 (번호 입력)
        </h4>
        <v-text-field
          v-model="adminDeleteUserId"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="adminDeleteUser"
    >
      삭제하기
    </v-btn>
    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>
          게시글 삭제 (번호 입력)
        </h4>
        <v-text-field
          v-model="adminDeleteBoardId"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="adminDeleteUser"
    >
      삭제하기
    </v-btn>
    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>
          플레이리스트 삭제 (번호 입력)
        </h4>
        <v-text-field
          v-model="adminDeletePlayroomId"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="adminDeleteUser"
    >
      삭제하기
    </v-btn>
    <div class="mx-10 mt-3">
      <div class="my-2">
        <h4>
          플레이룸 삭제 (번호 입력)
        </h4>
        <v-text-field
          v-model="adminDeletePlaylistId"
          required
          dense
        />
      </div>
    </div>
    <v-btn
      class="d-flex justify-center p-5 mx-5"
      rounded
      outlined
      color="#5B5C9D"
      depressed
      @click="adminDeleteUser"
    >
      삭제하기
    </v-btn>
  </v-app>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/server'
import { mapState } from 'vuex'
import swal from 'sweetalert2'

export default {
  name: 'ChangePassword',
  data: function() {
    return {
      adminDeleteUserId : '',
      adminDeleteBoardId : '',
      adminDeletePlaylistId : '',
      adminDeletePlayroomId : '',
    }
  },
  computed: {
    ...mapState(['authToken'])
  },
  methods: {
    adminDeleteUser: function() {
      axios({
        method: 'DELETE',
        url: SERVER.URL + '/admin/user/' + this.adminDeleteUserId,
        headers: {Authorization: this.authToken},
      })
        .then((res) => {
          swal.fire ({
            icon: 'success',
            title: '유저 삭제 성공',
            text: '해당 유저가 삭제되었습니다. 작업 내용은 로그에 기록되었습니다.',
            // scrollbarPadding: false  // 모바일 환경은 알아서 체크해야
          })
        })
        .catch ((err) => {
          console.log(err.response.data)
          swal.fire ({
            icon: 'error',
            title: '삭제 실패',
            text: '잘못된 요청이나 인증권한이 없습니다.'
          })
        })
    },
    adminDeleteBoard: function() {
      axios({
        method: 'DELETE',
        url: SERVER.URL + '/admin/board/' + this.adminDeleteBoardId,
        headers: {Authorization: this.authToken},
      })
        .then((res) => {
          swal.fire ({
            icon: 'success',
            title: '유저 삭제 성공',
            text: '해당 유저가 삭제되었습니다. 작업 내용은 로그에 기록되었습니다.',
            // scrollbarPadding: false  // 모바일 환경은 알아서 체크해야
          })
        })
        .catch ((err) => {
          console.log(err.response.data)
          swal.fire ({
            icon: 'error',
            title: '삭제 실패',
            text: '잘못된 요청이나 인증권한이 없습니다.'
          })
        })
    },
    adminDeletePlaylist: function() {
      axios({
        method: 'DELETE',
        url: SERVER.URL + '/admin/playlist/' + this.adminDeletePlaylistId,
        headers: {Authorization: this.authToken},
      })
        .then((res) => {
          swal.fire ({
            icon: 'success',
            title: '유저 삭제 성공',
            text: '해당 유저가 삭제되었습니다. 작업 내용은 로그에 기록되었습니다.',
            // scrollbarPadding: false  // 모바일 환경은 알아서 체크해야
          })
        })
        .catch ((err) => {
          console.log(err.response.data)
          swal.fire ({
            icon: 'error',
            title: '삭제 실패',
            text: '잘못된 요청이나 인증권한이 없습니다.'
          })
        })
    },
    adminDeletePlayroom: function() {
      axios({
        method: 'DELETE',
        url: SERVER.URL + '/admin/playroom/' + this.adminDeletePlayroomId,
        headers: {Authorization: this.authToken},
      })
        .then((res) => {
          swal.fire ({
            icon: 'success',
            title: '유저 삭제 성공',
            text: '해당 유저가 삭제되었습니다. 작업 내용은 로그에 기록되었습니다.',
            // scrollbarPadding: false  // 모바일 환경은 알아서 체크해야
          })
        })
        .catch ((err) => {
          console.log(err.response.data)
          swal.fire ({
            icon: 'error',
            title: '삭제 실패',
            text: '잘못된 요청이나 인증권한이 없습니다.'
          })
        })
    },


  },
}
</script>

<style scoped>

</style>
