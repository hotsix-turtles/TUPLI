<template>
  <v-app id="app">
    <v-main>
      <router-view />
    </v-main>
    <!-- <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
    </router-link> -->
    <navbar-bottom class="mt-auto fixed" />
  </v-app>
</template>

<script>
import NavbarBottom from './components/common/NavbarBottom.vue';

export default {
  name: 'App',
  components: { NavbarBottom },
};
</script>
