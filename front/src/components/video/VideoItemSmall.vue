<template>
  <v-card
    height="100px"
    class="d-flex"
    outlined
    tile
    :color="color"
  >
    <v-list-item two-line>
      <img
        :src="video.thumbnail"
        style="width: 150px; height: 100px"
        class=""
        @click="watchingVideo(video)"
      >
      <v-list-item-content
        class="ml-2"
        @click="addVideo"
      >
        <v-list-item-title>
          <p>{{ video.title }}</p>
          <!-- <p>{{ video.channelTitle }}</p> -->
          <!-- <p>{{ video.categoryId }}</p>
          <p>{{ video.duration }}</p> -->
        </v-list-item-title>
        <!-- <v-list-item-subtitle>{{ video.date }}</v-list-item-subtitle> -->
        <v-list-item-subtitle>{{ video.channelTitle }}</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-card-actions>
      <v-menu bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            v-bind="attrs"
            small
            text
            style="align-self: start"
            fab
            v-on="on"
          >
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item
            v-for="(item, index) in items"
            :key="index"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-card-actions>
  </v-card>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'VideoItemSmall',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    video: { type: Object },
  },
  data() {
    return {
      clickState: false,
      items: [
        { id: 1, title: 'good' }
      ],
    }
  },
  computed: {
    ...mapState('video', {
      selectedVideos: state => state.selectedVideos,
    }),
    color: function () { return this.clickState ? "#dde" : "white" }
  },
  methods: {
    ...mapActions('video', [
      'watchingVideo',
      'addSelectedVideo',
      'removeSelectedVideo',
      'resetVideoSearchState',
    ]),
    addVideo: function() {
      console.log('addVideo', this.video)
      if (this.clickState) {
        this.removeSelectedVideo(this.video.videoId)
      } else {
        this.addSelectedVideo(this.video)
      }
      this.clickState = !this.clickState
    },
  },

}
</script>

<style>

</style>
