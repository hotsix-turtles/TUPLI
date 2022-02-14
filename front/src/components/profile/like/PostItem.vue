<template>
  <v-app>
    <div class="d-flex">
      <div class="d-flex">
        <img
          :src="thumbnail"
          alt=""
        >
        <p>{{ boardlist.nickName }}</p>
      </div>
      <div>
        <p>{{ boardlist.content }}</p>
      </div>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'PostItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    boardlist: { type: Object },
    thumbnail: { type: String, default: '' },
  }
}
</script>

<style>

</style>
