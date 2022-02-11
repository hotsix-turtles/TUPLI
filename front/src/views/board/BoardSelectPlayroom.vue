<template>
  <div>
    <cancel :page-name="'플레이룸 선택하기'" />
    selectplayroom
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'

export default {
  name: 'BoardFormPlayroom',
  components: {
    Cancel,
  },
}
</script>

<style>

</style>
