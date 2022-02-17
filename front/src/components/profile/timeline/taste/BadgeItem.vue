<template>
  <div
    v-if="badgeCheck"
    style="width:20%; margin-left: 4px; margin-right: 4px; margin-bottom: 4px;"
    class="d-flex flex-column"
  >
    <img
      :src="ImgUrl(img)"
      alt="badge img"
      width="100%"
      style="background-size: 50%;"
    >
    <div class="d-flex justify-center">
      <p style="font-size: 10px; padding-bottom: 5px; color:gray;">
        {{ date }}
      </p>
    </div>
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
      badgeCheck: false,
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
      if (this.badge.badgeSeq < 32) {
        this.badgeCheck = true
        this.img = this.badge.badgeSeq
        console.log('뱃지이미지이ㅣ이ㅣ', this.img, typeof(this.img))
      }
      else {
        this.badgeCheck = false
      }
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
