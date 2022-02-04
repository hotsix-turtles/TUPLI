<template>
  <v-card
    height="100px"
    class="d-flex"
    outlined
    tile
    :color="color"
    @click.stop="onItemClick"
  >
    <v-list-item two-line>
      <img
        :src="thumbnail"
        style="width: 40%; height: auto"
        class=""
      >
      <v-list-item-content class="ml-2">
        <v-list-item-title>{{ title }}</v-list-item-title>
        <v-list-item-subtitle>{{ author }}</v-list-item-subtitle>
        <!-- <v-list-item-subtitle>{{ playtime }}</v-list-item-subtitle> -->
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
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: 'PlaylistVideoItem',
  props: {
    id: { type: Number, default: -1 },
    title: { type: String, default: '' },
    author: { type: String, default: '' },
    thumbnail: { type: String, default: '' },
    playtime: { type: String, default: '0:0' },
<<<<<<< HEAD
    clicked: { type: Boolean, default: false },
=======
    selected: { type: Boolean, default: false },
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
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
<<<<<<< HEAD
    onItemClick (event) {
      if (this.readonly) return;
      this.clickState = !this.clickState
      this.$emit('click', { id: this.id, clickState: this.clickState })
=======
    onItemClick () {
      if (this.readonly) return;
      this.$emit('click', { id: this.id, selected: this.selected })
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
    }
  }
}
</script>

<style>
</style>
