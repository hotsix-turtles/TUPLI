<template>
  <div>
    <span
      v-for="(tag, idx) in tags"
      :key="idx"
    >
      <span
        class="recommend-tag me-2"
        @click="selectTag(tag)"
      >
        {{ tag }}
      </span>
    </span>
  </div>
</template>

<script>
export default {
  name: 'RecommendTags',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    tags: { type: Array },
  },
  data: function () {
    return {
    }
  },
  methods: {
    selectTag: function (tag) {
      this.$emit('select-tag', tag)
    }
  }
}
</script>

<style>
.recommend-tag {
  color: #5B5C9D;
  background-color: #F5F5F5;
  border: solid 1px;
  border-color: #5B5C9D;
  border-radius: 5px;
  font-size: 14px;
  padding: 3px 3px 3px 3px;
  line-height: 2.2;
}
</style>
