<template>
  <div>
    <span
      v-for="(tag, idx) in tags"
      :key="idx"
    >
      <span class="tag me-1">
        {{ tag }}
      </span>
    </span>
  </div>
</template>

<script>
export default {
  name: 'Tags',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    tags: { type: Array },
  },
  data: function () {
    return {
    }
  },
  methods: {
  }
}
</script>

<style>

</style>
