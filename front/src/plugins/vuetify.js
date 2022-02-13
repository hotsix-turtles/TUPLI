import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

// import '@mdi/font/css/materialdesignicons.css'; // 아이콘 불러오기
import '@/scss/_index.scss'; // _index.scss를 한번만 불러오면 자동으로 css로 컴파일 해줌


Vue.use(Vuetify);

export default new Vuetify({
  // icons: {
  //   iconfont: 'mdi',
  // },
  theme: {
    themes: {
      light: {
        primary: '#101010',
        secondary: '#d8d8ee',
        accent: '#5B5C9D',
      },
    },
  },
});
