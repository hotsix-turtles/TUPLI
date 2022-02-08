<template>
  <div class="d-flex">
    <div class="ms-auto">
      <select
        name="order"
      >
        <option
          v-for="(select, idx) in Object.keys(selectList)"
          :key="idx"
          :value="selectList.select"
          @click="onChangeSelect(selectList.select)"
        >
          {{ select }}
        </option>
      </select>
    </div>
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
  data: function () {
    return {
    }
  },
  methods: {
    onChangeSelect: function (order) {
      console.log(order)
      this.$emit('onChangeSelect', order)
    },
  }
}
</script>
