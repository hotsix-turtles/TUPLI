<template>
  <v-app>
    <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
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
