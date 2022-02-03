<template>
  <div>
    <iframe
      :src="videoURI"
      frameborder="0"
      width="100%"
      height="220px"
    />
    {{ watchingVideo.title }}
    {{ watchingVideo.channelTitle }}
    {{ watchingVideo.duration }}
    {{ watchingVideo.date }}
  </div>
</template>

<script>
export default {
  name: 'VideoIframe',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    watchingVideo: { type: Object }
  },
  computed: {
    videoURI: function () {
      const videoId = this.watchingVideo.videoId
      return `https://www.youtube.com/embed/${videoId}`
    }
  }
}
</script>

<style>

</style>
