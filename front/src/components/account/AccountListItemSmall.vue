<template>
  <div>
    <account-item-small
      v-for="(account, idx) in accounts"
      :key="idx"
      :account="account"
      :readonly="readonly"
      :underline="underline"
    />
  </div>
</template>

<script>
import AccountItemSmall from './AccountItemSmall.vue'
export default {
  components: { AccountItemSmall },
  name: 'AccountListItemSmall',
  props: {
    accounts: { type: Array, default() { [] } },
    readonly: { type: Boolean, defaut: true },
    underline: { type: Boolean, default: false },
  },
}
</script>

<style>
</style>
