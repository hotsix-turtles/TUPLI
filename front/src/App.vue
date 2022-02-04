<template>
  <v-app>
    <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
    </router-link>
    <router-link to="/signup">
      Signup
    </router-link>
    <router-link to="/signup2">
      Signup2
    </router-link>
    <router-link to="/signup3">
      Signup3
    </router-link>
    <router-view />
  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
    //
  }),
  meta: [
    { charset: 'utf-8' },
    //{ name: 'description', content: ''},
    { name: 'viewport', content: 'width=device-width, initial-scale=1' }
  ]


};
</script>
