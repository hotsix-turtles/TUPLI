<template>
  <v-app id="tupli">
    <v-main>
      <router-view
        :key="$route.path"
        style="margin-bottom: 60px;"
      />
    </v-main>
    <navbar-bottom />
  </v-app>
</template>

<script>
import NavbarBottom from './components/common/NavbarBottom.vue';
import { mapActions } from 'vuex';

export default {
  name: 'App',
  components: { NavbarBottom },
  created() {
    this.getRealtimeAlarm();
  },
  methods: {
    ...mapActions(['getRealtimeAlarm']),
  },
};
</script>


<style>
* {
  font-family: 'SUIT';
}
#tupli {
    width: 100vw;
    height: 100vh;
    max-width: 412px;
    max-height: 915px;
    margin: 0 auto;
}
</style>
