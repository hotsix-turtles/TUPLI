<template>
  <div>
    Please wait...
  </div>
</template>

<script>
import { mapActions, mapMutations } from 'vuex'

export default {
  name: 'AuthHandler',
  created() {
    const token = this.$route.query.token
    if (token) {
      this.TOKEN(token)
      this.getUserInfo(token) // 쿼리로 유저 네임이나 정보도 보내줘야되나...
    }
    this.$router.push('/')
  },
  methods: {
    ...mapActions(['getUserInfo']),
    ...mapMutations(['TOKEN'])
  }
}
</script>

<style>

</style>

