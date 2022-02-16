<template>
  <v-dialog
    v-model="show"
    persistent
    :width="width ? parseInt(width) : undefined"
    :max-width="maxWidth ? parseInt(maxWidth) : undefined"
  >
    <v-card
      color="#5B5C9D"
      dark
      height="100%"
      class="tupli-dialog-loading"
    >
      <v-card-title
        class="tupli-dialog-loading-title"
      >
      {{ title }}
      </v-card-title>
      <v-card-text
        class="tupli-dialog-loading-text"
      >
        {{ content }}
        <v-progress-linear
          indeterminate
          color="white"
        />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'LoadingDialog',
  props: {
    title: { type: String, default: '' },
    content: { type: String, default: '' },
    width: { type: String, default: '290' },
    maxWidth: { type: String, default: '290' },
    show: { type: Boolean, default: false },
  },
  created() {
    this.$emit('created', this);
  },
  mounted() {
    this.$emit('mounted', this);
  },
  beforeDestroy() {
    this.$emit('before-destroy', this);
  },
}
</script>

<style lang="scss" scoped>
.tupli-dialog-loading-title {
  font-size: 1rem !important;
  padding-bottom: 5px !important;
}
</style>
