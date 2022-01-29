<template>
  <v-card
    height="60px"
    class="d-flex pa-1"
    elevation="0"
    tile
    @click.stop="dialog2 = !dialog2"
  >
    <v-row>
      <v-col
        cols="12"
        md="4"
        class="d-flex align-center"
      >
        <v-avatar circle
          @click.stop="dialog2 = !dialog2">
          <img
            :src="profile"
            class="pa-1"
          >
        </v-avatar>
        <p class="font-3 ml-1 font-weight-bold">{{ name }}</p>
        <p class="font-3 ml-1">{{ content }}</p>
        <p class="font-3 ml-auto mr-1">{{ timeLabel }}</p>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
export default {
  name: "ChatItem",
  props: {
    id: { type: Number, default: -1},
    name: { type: String, default: ''},
    profile: { type: String, default: ''},
    content: { type: String, default: ''},
    timestamp: { type: Number, default: 0},
  },
  created() {
    this.$watch('dialog2', (newVal, oldVal) => {
      this.$emit(newVal ? 'onMenuOpened' : 'onMenuClosed');
    })
  },
  data () {
    return {
      dialog2: false
    }
  },
  computed: {
    timeLabel () {
      //const dt = new Date(this.timestamp);
      const dt = new Date();
      return `${dt.getHours() < 12 ? "오전" : "오후"} ${dt.getHours() % 12}:${dt.getMinutes()}`;
    }
  },
  methods: {
    blockMessage () {
      this.$emit('blockMessaage', this.id);
      this.closeMenu();
    },
    closeMenu () {
      this.dialog2 = false;
    }
  }
}
</script>

<style>

</style>
