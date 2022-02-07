<template>
  <div>
    Please wait...
  </div>
</template>

<script>
import { mapActions, mapMutations } from 'vuex'
import router from '@/router'

export default {
  name: 'AuthHandler',
  created() {
    const token = this.$route.query.token
    if (token) {
      this.TOKEN(token)
      this.getUserInfo() // 쿼리로 유저 네임이나 정보도 보내줘야되나...
    }
    // this.$router.replace('/')  // 홈으로 보내버리겠다. 2
    // router.push('/')  // 홈으로 보내버리겠다. 1
    this.$router.push('/home')
  },
  methods: {
    ...mapActions(['getUserInfo']),
    ...mapMutations(['TOKEN'])
  }
}
</script>

<style>

</style>

