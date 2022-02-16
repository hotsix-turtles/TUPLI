<template>
  <v-container>
    <v-row no-gutters>
      <template v-for="n in 25">
        <v-col :key="n">
          <v-card
            v-if="getImoticon(`#${n}`)"
            class="pa-2 d-flex justify-center align-center"
            outlined
            tile
            @click="$emit('click', { idx: n, emoticon: getImoticon(`#${n}`) })"
          >
            <img class="chat-emoticon" :src="getImoticon(`#${n}`)" />
          </v-card>
        </v-col>
        <v-responsive
          v-if="n % 5 === 0"
          :key="`width-${n}`"
          width="100%"
        ></v-responsive>
      </template>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Emoji',
  data() {
    return {
    }
  },
  computed: {
    renderContent() {
      const tagsToReplace = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;'
      };
      const escapedContent = this.content.replace(/[&<>]/g, (tag) => tagsToReplace[tag] || tag);

      return [...this.content.matchAll(new RegExp(/\#\d+/g))]
        .map(a => a[0])
        .reduce((prev,cur) => prev = prev.replace(cur, `<img class="chat-emoticon" src="${this.getImoticon(cur)}" />`), escapedContent)
    },
  },
  methods: {
    getImoticon(content) {
      if (content == '#1' || content == "#헐") {
        return require(`@/assets/emoticons/basic/01_really.png`)
      } else if (content == '#2' || content == "#메롱") {
        return require(`@/assets/emoticons/basic/02_silly.png`)
      } else if (content == '#3' || content == "#짜증") {
        return require(`@/assets/emoticons/basic/03_annoyed.png`)
      } else if (content == '#4' || content == "#놀람") {
        return require(`@/assets/emoticons/basic/04_shocked.png`)
      } else if (content == '#5' || content == "#웃음") {
        return require(`@/assets/emoticons/basic/05_smiley.png`)
      } else if (content == '#6' || content == "#폭소") {
        return require(`@/assets/emoticons/basic/06_ridicule.png`)
      } else if (content == '#7') {
        return require(`@/assets/emoticons/basic/07_wonder.png`)
      } else if (content == '#8') {
        return require(`@/assets/emoticons/basic/08_notgood.png`)
      } else if (content == '#9') {
        return require(`@/assets/emoticons/basic/09_blind.png`)
      } else if (content == '#10') {
        return require(`@/assets/emoticons/basic/10_mad.png`)
      } else if (content == '#11') {
        return require(`@/assets/emoticons/basic/11_thinkbad.png`)
      } else if (content == '#12' || content == "#헤" || content == "#호") {
        return require(`@/assets/emoticons/basic/12_ho.png`)
      } else if (content == '#13') {
        return require(`@/assets/emoticons/basic/13_surrender.png`)
      } else if (content == '#14' || content == "#충격") {
        return require(`@/assets/emoticons/basic/14_shocked2.png`)
      } else if (content == '#15') {
        return require(`@/assets/emoticons/basic/15_wonder2.png`)
      } else if (content == '#16') {
        return require(`@/assets/emoticons/basic/16_laugh.png`)
      } else if (content == '#17') {
        return require(`@/assets/emoticons/basic/17_strange.png`)
      } else if (content == '#18' || content == "#슬픔") {
        return require(`@/assets/emoticons/basic/18_sad.png`)
      } else if (content == '#19' || content == "#죽음") {
        return require(`@/assets/emoticons/basic/19_dead.png`)
      } else if (content == '#20' || content == "#수면") {
        return require(`@/assets/emoticons/basic/20_sleep.png`)
      } else if (content == '#21') {
        return require(`@/assets/emoticons/basic/21_happysilly.png`)
      } else if (content == '#22' || content == "#오") {
        return require(`@/assets/emoticons/basic/22_what.png`)
      } else if (content == '#23') {
        return require(`@/assets/emoticons/basic/23_laughter.png`)
      } else if (content == '#24') {
        return require(`@/assets/emoticons/basic/24_suspicious.png`)
      } else if (content == '#25') {
        return null
      }
    },
  }
}
</script>

<style>

</style>
