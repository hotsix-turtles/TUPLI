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
        <v-list-item-title @click="addVideo(videoId)">
          <p>{{ title }}</p>
          <p>{{ channelTitle }}</p>
          <p>{{ categoryId }}</p>
          <p>{{ duration }}</p>
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
    videoId: { type: String, default: '' },
    title: { type: String, default: '' },
    thumbnail: { type: String, default: '' },
    date: { type: String, default: '' },
    duration: { type: String, default: '' },
    channelTitle: { type: String, default: '' },
    categoryId: { type: Number, default: 0 },
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
