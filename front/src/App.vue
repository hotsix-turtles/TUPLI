<template>
  <v-app>
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
    this.$store.dispatch('checkLogin')  // 시점 조절 필요
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

</style>
