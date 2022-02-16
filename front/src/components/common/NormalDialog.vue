<template>
  <v-dialog
    v-model="show"
    :width="width ? parseInt(width) : undefined"
    :max-width="maxWidth ? parseInt(maxWidth) : undefined"
    :scrollable="scrollable"
    :persistent="persistent"
  >
    <v-card
      class="tupli-dialog-normal"
    >
      <v-card-title
        class="tupli-dialog-normal-title"
      >
        {{ title }}
      </v-card-title>
      <v-card-text
        v-if="contentHtml"
        class="tupli-dialog-normal-content"
        v-html="contentHtml"
      />
      <v-card-text
        v-else-if="content"
        class="tupli-dialog-normal-content"
      >
        {{ content }}
      </v-card-text>
      <v-card-actions
        v-if="buttons.length"
      >
        <v-spacer v-if="buttonSpacing" />
        <v-btn
          v-for="(button, idx) in buttons"
          :key="idx"
          text
          :class="button.class"
          :color="button.color"
          @click="onButtonClick(idx)"
        >
          {{ button.name }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'NormalDialog',
  props: {
    title: { type: String, default: '' },
    content: { type: String, default: '' },
    contentHtml: { type: String, default: '' },
    width: { type: String, default: '' },
    maxWidth: { type: String, default: '290' },
    scrollable: { type: Boolean, default: false },
    persistent: { type: Boolean, default: false },
    buttonSpacing: { type: Boolean, default: false },
    buttons: { type: Array, default() { return [] } },
    show: { type: Boolean, default: false },
  },
  created() {
    this.$emit('created', this);
  },
  mounted() {
    this.$emit('mounted', this);
  },
  beforeDestroy() {
    this.$emit('beforeDestroy', this);
  },
  methods: {
    onButtonClick(idx) {
      this.$emit('button-click', idx);
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
