/* eslint-disable vue/require-default-prop */
<template>
  <div class="d-flex mx-3 my-3">
    <div>
      <v-icon @click="goBack">
        mdi-arrow-left
      </v-icon>
    </div>
    <div class="font-2 semi-bold center">
      {{ pageName }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'Back',
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

<style scoped>
</style>
