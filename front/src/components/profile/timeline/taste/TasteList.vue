<template>
  <div>
    <p
      v-for="(value, name) in tastes"
      :key="name"
      :name="name"
      :value="value"
    >
      {{ name }} : {{ value }}
    </p>
    <taste-item />
  </div>
</template>

<script>

import TasteItem from './TasteItem.vue'

export default {
  name: 'TasteList',
  components: {
    TasteItem,
  },
  props: {
    tastes: {type : Object, default() { {} } }
  },
  created: function() {
    console.log('데이터 모양 보기', this.tastes)
  }

}
</script>

<style>

</style>
