<template>
  <div>
    <div
      class="px-4"
    >
      <canvas
        id="myChart"
        width="380"
        height="290%"
        style="margin-bottom: 20px; margin-top: 4px;"
      />
    </div>
    <taste-item />
  </div>
</template>

<script>
import { Chart, BarElement, BarController, LinearScale, CategoryScale} from 'chart.js'; // Chart.js 모듈 임포트
Chart.register(BarElement, BarController, LinearScale, CategoryScale); // chart.js 모듈 Chart 모듈에 등록

import TasteItem from './TasteItem.vue'

export default {
  name: 'TasteList',
  components: {
    TasteItem,
  },
  props: {
    tastes: {type : Object, default() { {} } }
  },
  data: function () {
    return {
      myChart: null,

      tastesObject: {},
      names: [],
      datas: [],
    }
  },
  mounted() {
    this.fillData();
  },
  created: function() {
    console.log('데이터 모양 보기', this.tastes)
    console.log('데이터 모양 보기22222', this.name)
    this.tastesObject = this.tastes
    this.chartData()
  },
  methods: {
    chartData: function() {
      for (let key in this.tastesObject) {
        console.log('제발 나와라', `${key} : ${this.tastesObject[key]}`);
        this.names.push(key)
        this.datas.push(this.tastesObject[key])
      }
    },
    fillData() {
      const ctx = document.getElementById('myChart').getContext('2d');
      const myChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: this.names,
          datasets: [{
            data: this.datas,
            backgroundColor: [
              'rgb(132, 137, 224)'
              // 'rgba(255, 99, 132, 0.2)',
              // 'rgba(54, 162, 235, 0.2)',
              // 'rgba(255, 206, 86, 0.2)',
              // 'rgba(75, 192, 192, 0.2)',
              // 'rgba(153, 102, 255, 0.2)',
              // 'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
              'rgb(220, 221, 233)'

              // 'rgba(255, 99, 132, 1)',
              // 'rgba(54, 162, 235, 1)',
              // 'rgba(255, 206, 86, 1)',
              // 'rgba(75, 192, 192, 1)',
              // 'rgba(153, 102, 255, 1)',
              // 'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
          }]
        },
        options: {
          indexAxis: 'y',
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    }

  }

}
</script>

<style>

</style>
