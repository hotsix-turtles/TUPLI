<template>
  <div>
    결제 실패! <br>
    홈으로 이동 중...
  </div>
</template>

<script>
import swal from 'sweetalert2'
import router from '@/router'

export default {
  name: 'KakaoPayCancel',
  created() {
    this.returnToHome()
  },
  methods: {
    // 1초 후 화면으로 돌아갑니다.
    returnToHome: function () {
      let timerInterval
      swal.fire ({
        icon: 'error',
        position : 'top',
        title: '결제 실패',
        html: '서버가 혼잡합니다. 나중에 다시 시도해주세요.<br>1초 후 홈으로 돌아갑니다.',
        timer: 1000,
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
          router.push('/') 
        }
      })
    }
  },
}
</script>

<style>

</style>