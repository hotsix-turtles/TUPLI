<template>
  <div class="d-flex">
    <span class="font-1 ms-auto">
      {{ pageName }}
    </span>
    <v-icon
      class="ms-auto"
      @click="goBack"
    >
      mdi-close
    </v-icon>
  </div>
</template>

<script>
export default {
  name: 'Cancel',
  components: {
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    pageName: {
      type: [Object, String],
    }
  },
  methods: {
    goBack: function() {
      this.$router.go(-1)
    }
  }
}
</script>
