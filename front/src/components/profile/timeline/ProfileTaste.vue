<template>
  <v-app>
    <div>
      <!-- 취향 분석 -->
      <div class="d-flex flex-column">
        <h3>지난 1주일 간 취향 분석</h3>
        <p>{{ nickname }}님은 지난 1주일간 ???을 가장 즐겨보셨군요?</p>
        <div class="d-flex align-center">
          <p>예능</p>
          <p>-------</p>
        </div>
        <div class="d-flex align-center">
          <p>먹방</p>
          <p>-------------------</p>
        </div>
        <div class="d-flex align-center">
          <p>스포츠</p>
          <p>---------</p>
        </div>
        <div class="d-flex align-center">
          <p>음악</p>
          <p>--------</p>
        </div>
        <div class="d-flex align-center">
          <p>게임</p>
          <p>--------</p>
        </div>
      </div>
      <!-- 뱃지 -->
      <div>
        <h3>나의 튜플리 뱃지</h3>
      </div>
    </div>
  </v-app>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'ProfileTaste',
  computed: {
    ...mapState(['nickname'])
  }
}
</script>

<style>

</style>
