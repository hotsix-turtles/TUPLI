<template>
  <v-app>
    <v-main>
      <router-view />
      <router-link to="/home">
        Home
      </router-link>
      <router-link to="/signup2">
        Signup
      </router-link>
      <router-link to="/login">
        Login
      </router-link>
      <router-link to="/playroom">
        Playroom
      </router-link>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',
};
</script>
