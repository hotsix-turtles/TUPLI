<template>
  <div>
    <v-card>
      <img
        :src="ImgUrl(img)"
        alt="badge img"
        style="background-size: 50%;"
      >
      <v-card-title>
        {{ date }}
      </v-card-title>
    </v-card>
  </div>
</template>

<script>
import { getBadgeImage, timeConverterShort } from '../../../../utils/utils'


export default {
  name: 'BadgeItem',
  props: {
    badge:  {type : Object, default() { {} } },
  },
  data: function() {
    return {
      img: '',
      date: '',
    }
  },
  created: function() {
    this.getBadgeImg()
    this.getBadgeDate()
    // console.log('뱃지이이이이222', this.badge)
  },
  methods: {
    // 배지 번호로 이미지 찾기
    getBadgeImg: function() {
      this.img = this.badge.badgeSeq
      console.log('뱃지이미지이ㅣ이ㅣ', this.img, typeof(this.img))
    },
    // 이미지 조합
    ImgUrl: function(img) {
      return getBadgeImage(img)
    },
    // 뱃지 획득 일자
    getBadgeDate: function() {
      this.date = timeConverterShort(this.badge.acquired)
    }
  }
}
</script>

<style>

</style>
