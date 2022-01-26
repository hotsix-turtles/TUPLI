/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <v-icon @click="goBack">
      mdi-arrow-left
    </v-icon>
    <span class="font-1">
      {{ pageName }}
    </span>
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
