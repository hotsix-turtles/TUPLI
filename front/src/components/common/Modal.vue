<template>
  <div>
    <!-- 검정배경 -->
    <div
      v-if="showModal"
      class="dark-background"
      @click="onClickModal"
    />
    <!-- 하얀색 모달 목록 -->
    <div
      v-if="showModal"
      class="fixed-bottom light-background px-5 py-5"
    >
      <div class="d-flex justify-space-between">
        <div class="font-2 bold mb-3">
          {{ modalName }}
        </div>
        <div>
          <v-icon @click="onClickModal">
            mdi-close
          </v-icon>
        </div>
      </div>
      <div
        v-for="(item, idx) in Object.keys(items)"
        :key="idx"
      >
        <div
          class="my-4 font-2"
          @click="onSelect(item)"
        >
          <span :class="{ colored: modalType === 'order' && item === selected }">
            {{ item }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'Modal',
  components: {
  },
  props: {
    items: { type: Object , default() {} }, // 목록 내 아이템들 {이름: 실제값}
    modalName: { type: String, default: '' },  // 목록 이름
    modalType: { type: String, default: '' }  // 모달 종류 (정렬: order)
  },
  data: function () {
    return {
    }
  },
  computed: {
    ...mapState('common', {
      showModal: state => state.showModal,
      selected: state => state.selected,
    })
  },
  created: function () {
    console.log(this.showModal)
    this.resetModalData()
    this.onSelectItem(Object.keys(this.items)[0])
  },
  methods: {
    ...mapActions('common', [
      'onClickModal',
      'onSelectItem',
      'resetModalData',
    ]),
    // vuex보다 이게  더편한듯
    onSelect: function (item) {
      this.$emit('on-select', this.items[item])
      this.onSelectItem(item)
    },
  }
}
</script>

<style scoped>

  .colored {
    color: #5B5C9D;
    font-weight: bold;
  }

</style>
