<template>
  <div>
    <select
      name="order"
    >
      <option
        v-for="(select, idx) in Object.keys(selectList)"
        :key="idx"
      >
        {{ select }}
      </option>
    </select>
  </div>
</template>

<script>
export default {
  name: 'Order',
  components: {
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    selectList: {
      type: Object
    }
  },
  computed: {
    selectKeys: function () {
      return Object.keys(this.selectList)
    }
  },
  methods: {
  }
}
</script>
