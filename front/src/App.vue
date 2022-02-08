<template>
  <v-app>
    <v-main>
      <router-view
        :key="$route.path"
        style="margin-bottom: 60px;"
      />
    </v-main>
    <navbar-bottom class="mt-auto fixed" />
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
