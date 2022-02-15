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
  },
  methods: {
    ...mapActions(['getRealtimeAlarm']),
  },
};
</script>


<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');

* {
  font-family: 'SUIT';
}

</style>
