<template>
  <div>
    <div
      v-for="(tag, idx) in tags"
      :key="idx"
    >
      <span class="tag">
        {{ tag }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Tags',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    tags: { type: Array },
  },
  methods: {
  }
}
</script>

<style>

</style>
