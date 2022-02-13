<template>
  <div>
    <cancel :page-name="'플레이리스트 선택하기'" />
    selectplaylist
  </div>
</template>

<script>
import Cancel from '../../components/common/Cancel.vue'

export default {
  name: 'BoardFormPlaylist',
  components: {
    Cancel,
  },
}
</script>

<style>

</style>
