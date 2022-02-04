<template>
  <v-app>
    <router-link to="/home">
      Home
    </router-link>
    <router-link to="/login">
      Login
    </router-link>
    <router-link to="/signup2">
      Signup
    </router-link>
    <router-link to="/profile">
      Profile
    </router-link>
    <v-main>
      <router-view />
    </v-main>
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
