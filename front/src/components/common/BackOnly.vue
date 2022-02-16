/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <v-icon
      color="black"
      @click="goBack"
    >
      mdi-arrow-left
    </v-icon>
  </div>
</template>

<script>
export default {
  name: 'Back',
  components: {
  },
  methods: {
    goBack: function() {
      this.$router.go(-1)
    }
  }
}
</script>
