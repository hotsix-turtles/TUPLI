<<<<<<< HEAD
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
      this.SET_TOKEN(token)
      this.getUserInfoToken(token) // 쿼리로 유저 네임이나 정보도 보내줘야되나...
    }
    // this.$router.replace('/')  // 홈으로 보내버리겠다. 2
    // router.push('/')  // 홈으로 보내버리겠다. 1
    this.$router.push('/home')
  },
  methods: {
    ...mapActions(['getUserInfoToken']),
    ...mapMutations(['SET_TOKEN'])
  }
}
</script>

<style>

</style>
=======
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
      this.SET_TOKEN(token)
      this.getUserInfoToken(token) // 쿼리로 유저 네임이나 정보도 보내줘야되나...
    }
    // this.$router.replace('/')  // 홈으로 보내버리겠다. 2
    // router.push('/')  // 홈으로 보내버리겠다. 1
    this.$router.push('/home')
  },
  methods: {
    ...mapActions(['getUserInfoToken']),
    ...mapMutations(['SET_TOKEN'])
  }
}
</script>

<style>

</style>
>>>>>>> 0c38fcaafc995ca9626a62d51fe31b7bf2c42a99
