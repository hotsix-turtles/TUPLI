<template>
  <v-btn>
    <span :style="{ color: color }">{{ content }}</span>
    <v-icon :color="color">
      {{ icon }}
    </v-icon>
  </v-btn>
</template>

<script>
export default {
  name: 'NavButton',
  props: {
    color: { type: String, default: "white"},
    content: { type: String, default: "메뉴1"},
    icon: { type: String, default: "mdi-play"}
  }
}
</script>

<style>

</style>
