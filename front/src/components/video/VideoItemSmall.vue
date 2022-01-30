<template>
  <div>
    <!-- {{ video.id }}<br><br>
    {{ video.snippet }} -->
    <v-list-item two-line>
      <img
        :src="video.snippet.thumbnails.default.url"
        style="width: 40%; height: auto"
        class=""
      >
      <v-list-item-content class="ml-2">
        <v-list-item-title @click="addVideo(video.id.videoId)">
          {{ video.snippet.title }}
        </v-list-item-title>
        <v-list-item-subtitle>{{ video.snippet.publishTime }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
  </div>
</template>

<script>
export default {
  name: 'VideoItem',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    video: { type: Object }
  },
  mehtods: {
    addVideo: function(videoId) {
      this.$emit('add-video', videoId)
    },
  },
}
</script>

<style>

</style>
