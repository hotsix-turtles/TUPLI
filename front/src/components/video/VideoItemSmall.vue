<template>
  <div>
    <!-- {{ video.id }}<br><br>
    {{ video.snippet }} -->
    <v-list-item two-line>
      <img
        :src="thumbnail"
        style="width: 40%; height: auto"
        class=""
      >
      <v-list-item-content class="ml-2">
        <v-list-item-title @click="addVideo(id)">
          {{ title }}
        </v-list-item-title>
        <v-list-item-subtitle>{{ date }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
  </div>
</template>

<script>
export default {
  name: 'VideoItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    id: { type: Number, default: 1 },
    title: { type: String, default: '' },
    thumbnail: { type: String, default: '' },
    date: { type: String, default: '' },
    // runtime: { type: String, default: '' },
  },
  methods: {
    addVideo: function(videoId) {
      this.$emit('add-video', videoId)
    },
  },
}
</script>

<style>

</style>
