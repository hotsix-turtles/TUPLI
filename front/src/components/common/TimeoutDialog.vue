<template>
  <v-dialog
    v-model="value"
    :width="width ? parseInt(width) : undefined"
    :max-width="maxWidth ? parseInt(maxWidth) : undefined"
    :scrollable="scrollable"
    :persistent="persistent"
  >
    <v-card
      class="tupli-dialog-timeout"
    >
      <v-progress-linear
        v-if="timeout && !hideProgress && (topProgress || (!topProgress && !bottomProgress))"
        v-model="progress"
        class="mb-0"
        color="accent"
      />
      <v-card-title
        class="tupli-dialog-timeout-title"
      >
        {{ title }}
      </v-card-title>
      <v-card-text
        v-if="contentHtml"
        class="tupli-dialog-timeout-content"
        v-html="contentHtml"
      />
      <v-card-text
        v-else-if="content"
        class="tupli-dialog-timeout-content"
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
      <v-progress-linear
        v-if="timeout && !hideProgress && bottomProgress"
        v-model="progress"
        class="mb-0"
        color="accent"
      />
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'TimeoutDialog',
  props: {
    title: { type: String, default: '' },
    content: { type: String, default: '' },
    contentHtml: { type: String, default: '' },
    width: { type: String, default: '' },
    maxWidth: { type: String, default: '290' },
    scrollable: { type: Boolean, default: false },
    persistent: { type: Boolean, default: true },
    buttonSpacing: { type: Boolean, default: false },
    buttons: { type: Array, default() { return [] } },
    timeout: { type: String, default: '3000' },
    redirect: { type: String, default: '' },
    // show: { type: Boolean, default: false },
    hideProgress: { type: Boolean, default: false },
    topProgress: { type: Boolean, default: false },
    bottomProgress: { type: Boolean, default: false },
    value: { type: Boolean, default: false },
  },
  data() {
    return {
      progressEvent: null,
      time: 0
    }
  },
  computed: {
    progress() {
      return Math.ceil(this.time / parseInt(this.timeout) * 100);
    }
  },
  // watch: {
  //   show: function () {
  //     if (this.show = true) {
  //       setTimeout(() => {
  //         console.log('setTimeout')
  //         this.show = false
  //         this.$emit('timeout')
  //       }, this.timeout)
  //     }
  //   }
  // },
  created() {
    this.$emit('created', this);

    this.$watch('value', (newVal, oldVal) => {
      if (newVal) {
        this.time = 0

        if (this.timeout)
          this.progressEvent = setInterval(this.updateProgress, 50); // 60FPS
      }
    });
  },
  mounted() {
    this.$emit('mounted', this);
  },
  beforeDestroy() {
    if (this.progressEvent)
    {
      clearInterval(this.progressEvent);
      this.progressEvent = null
    }
    this.$emit('beforeDestroy', this);
  },
  methods: {
    updateProgress() {
      this.time += 50;

      if (this.progress >= 100)
      {
        this.fireTimeout()
        if (this.redirect) this.fireRedirect()
      }
    },
    fireRedirect() {
      if (this.progressEvent)
      {
        clearInterval(this.progressEvent);
        this.progressEvent = null
      }
      this.$router.push(this.redirect);
    },
    fireTimeout() {
      if (this.progressEvent)
      {
        clearInterval(this.progressEvent);
        this.progressEvent = null
      }
      this.$emit('timeout')

      if (this.progressEvent)
      {
        clearInterval(this.progressEvent);
        this.progressEvent = null
      }
    },
    onButtonClick(idx) {
      this.$emit('button-click', idx);
    }
  }
}
</script>

<style lang="scss" scoped>
.tupli-dialog-timeout-title {
  color: $color-main;
}

.tupli-dialog-timeout-content {
  color: black !important;
}
</style>
