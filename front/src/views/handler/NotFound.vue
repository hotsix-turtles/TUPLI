<template>
  <div id="notFoundMain">
    <v-img 
      :src="require('@/assets/notFound.jpg')" 
      alt="not_found"
    />
  </div>
</template>

<script>
import swal from 'sweetalert2'

export default {
  name: 'NotFound',
  created() {
    this.returnToHome()
  },
  methods: {
    // 5초 후 화면으로 돌아갑니다.
    returnToHome: function () {
      let timerInterval
      swal.fire ({
        icon: 'error',
        position : 'top',
        title: '존재하지 않는 페이지입니다.',
        text: '3초 후 홈으로 돌아갑니다.',
        timer: 3000,
        timerProgressBar: true,
        didOpen: () => {
          swal.showLoading()
          const b = swal.getHtmlContainer().querySelector('b')
          timerInterval = setInterval(() => {
            b.textContent = swal.getTimerLeft()
          }, 100)
        },
        willClose: () => {
          clearInterval(timerInterval)
        }
      }).then((result) => {
        if (result.dismiss === swal.DismissReason.timer) {
          // 타이머 종료
          this.$router.push({ name: 'Home' })
        }
      })
    }
  },
}
</script>

<style scoped>

</style>