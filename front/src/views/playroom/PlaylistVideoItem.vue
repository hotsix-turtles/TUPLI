<template>
  <v-card
    height="13vh"
    class="d-flex align-center"
    outlined
    tile
    :color="color"
    @click.stop="onItemClick"
  >
    <div class="video-thumbnail">
      <img
        :src="video.thumbnail"
        style="width: 35vw; height: 100px;"
        class=""
        @click="watchingVideo(video)"
      >
      <span class="duration">{{ video.duration }}</span>
    </div>
    <div class="d-flex-column ml-2">
      <div
        class="h6"
      >
        <div class="font-3 line-height-s">
          {{ video.title }}
        </div>
      </div>
      <div class="font-4 color-dark-gray">
        <span>{{ video.channelTitle }}</span>
      </div>
    </div>
    <!-- <v-card-actions>
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
            <v-icon>mdi-dots-horizontal</v-icon>
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
    </v-card-actions> -->
  </v-card>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: 'PlaylistVideoItem',
  props: {
    video: { type: Object, default() {}},
    selected: { type: Boolean, default: false },
    readonly: { type: Boolean, default: false }
  },
  data() {
    return {
      items: [
        { id: 1, title: 'good' }
      ]
    }
  },
  computed: {
    color() { return this.selected ? '#dde' : '#eee' }
  },
  methods: {
    onItemClick () {
      if (this.readonly) return;
      this.$emit('click', { id: this.video.id, selected: this.selected })
    },
    ...mapActions('video', ['watchingVideo'])
  }
}
</script>

<style>
</style>
