<template>
  <v-app>
    <router-view />
    <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
    </router-link>
    <navbar-bottom class="mt-auto" />
  </v-app>
</template>

<script>
import NavbarBottom from './components/common/NavbarBottom.vue';

export default {
  name: 'App',
  components: { NavbarBottom },
};
</script>
