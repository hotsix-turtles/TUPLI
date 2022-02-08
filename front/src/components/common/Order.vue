<template>
  <div class="d-flex">
    <div class="ms-auto">
      <!-- <select
        v-model="order"
        name="order"
        @change="onChangeOrder"
      > -->
      <!-- {{ order }} -->
      <select
        v-model="order"
        name="order"
        @change="onChangeOrder(order)"
      >
        <option
          v-for="(select, idx) in Object.keys(selectList)"
          :key="idx"
          :value="selectList.select"
        >
          {{ select }}
        </option>
      </select>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
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
      order: '관련순',
    }
  },
  methods: {
    ...mapActions('video', [
      'onChangeOrder',
    ]),
  }
}
</script>
