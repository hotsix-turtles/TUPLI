/* eslint-disable vue/require-default-prop */
<template>
  <div>
    <v-combobox
      v-model="tags"
      multiple
      label="태그"
      append-icon
      chips
      deletable-chips
      class="tag-input"
      :search-input.sync="search"
      @keyup.tab="updateTags"
      @paste="updateTags"
    />
  </div>
</template>

<script>
export default {
  name: 'TagInput',
  components: {
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    propedTags: {
      type: Array
    }
  },
  data: function () {
    return {
      tags: [],
      items: [],
      search: "", //sync search
      select: []
    }
  },
  watch: {
    tags: function () {
      this.$emit('tag-input', this.tags)
    }
  },
  created: function () {
    this.tags = this.propedTags
  },
  methods: {
    updateTags: function () {
      this.$nextTick(() => {
        this.select.push(...this.search.split(","));
        this.$nextTick(() => {
          this.search = "";
        });
      });
    },
  },
}
</script>

<style scoped>

.tag-input span.chip {
  background-color: #1976d2;
  color: #fff;
  font-size: 1em;
}

.tag-input span.v-chip {
  background-color: #1976d2;
  color: #fff;
  font-size:1em;
  padding-left:7px;
}

.tag-input span.v-chip::before {
    content: "label";
    font-weight: normal;
    font-style: normal;
    font-size: 20px;
    letter-spacing: normal;
    text-transform: none;
    display: inline-block;
    white-space: nowrap;
    word-wrap: normal;
    direction: ltr;
}

</style>
