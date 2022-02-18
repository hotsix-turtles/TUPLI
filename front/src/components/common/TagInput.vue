/* eslint-disable vue/require-default-prop */
<template>
  <div class="mb-1">
    <v-combobox
      v-model="tags"
      item-color="#5B5C9D"
      color="#5B5C9D"
      multiple
      label="태그"
      append-icon
      chips
      deletable-chips
      class="tag-input"
      :search-input.sync="search"
      @keyup.tab="updateTags"
      @keyup.enter="updateTags"
      @paste="updateTags"
      @keyup="recommend"
    />
    <recommend-tags
      :tags="recommendTags"
      @select-tag="selectTag"
    />
  </div>
</template>

<script>
import { tags } from "@/utils/tags.js"
import RecommendTags from './RecommendTags.vue'

export default {
  name: 'TagInput',
  components: {
    RecommendTags
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
      select: [],
      recommendTags: [],
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
      console.log('updateTags')
      this.$nextTick(() => {
        this.select.push(...this.search.split(","));
        this.$nextTick(() => {
          this.search = "";
        });
      });
    },
    recommend: function ($event) {
      const keyword = $event.target.value
      this.recommendTags = []
      if (keyword) {
        for (let tag of tags) {
          if (tag.includes(keyword)) {
            this.recommendTags.push(tag)
          }
          if (this.recommendTags.length > 10) {
            break
          }
        }
        console.log('recommend', this.recommendTags)
      }
    },
    selectTag: function (tag) {
      this.search = tag
      this.updateTags
      this.recommendTags = []  // 여러개 누르면서 다 추가하는거는 구현 못함 ㅠㅠ
    }
  },
}
</script>

<style scoped>

.tag-input span.chip {
  background-color: #5B5C9D;
  color: #fff;
  font-size: 1em;
}

.tag-input span.v-chip {
  background-color: #5B5C9D;
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

.theme--light.v-chip:not(.v-chip--active) {
    background: #5B5C9D !important;
}

.theme--light.v-chip {
    color: white !important;
}

</style>
