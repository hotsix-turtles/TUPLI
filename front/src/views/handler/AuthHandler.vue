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
      this.getUserInfo(token)
      this.getSetting(token)
      this.OAUTH_LOGIN()
    }
    this.$router.push('/')
  },
  methods: {
    ...mapActions(['getUserInfo', 'getSetting']),
    ...mapMutations(['TOKEN', 'OAUTH_LOGIN'])
  }
}
</script>

<style>

</style>

