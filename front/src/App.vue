<template>
  <v-app id="app">
    <router-view />
    <navbar-bottom class="mt-auto" />
  </v-app>
</template>

<script>
import NavbarBottom from './components/NavbarBottom.vue';

export default {
  name: 'App',
  components: { NavbarBottom },
};
</script>
