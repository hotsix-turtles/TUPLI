<template>
  <div>
    <p />
  </div>
</template>

<script>

export default {
  name: 'TasteItem',
  props: {
    name: {type : String, default() { '' } },
    value: {type : String, default() { '' } }

  },
  created: function() {
    // console.log('아이테메엠1', this.taste)
    // console.log('아이테메엠2', this.name)
    // console.log('아이테메엠3', this.key)
  }


}
</script>

<style>

</style>
