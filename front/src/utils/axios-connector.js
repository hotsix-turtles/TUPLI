// import baseAxios from 'axios';
// import { getToken } from './JWT';

// const axios = baseAxios.create({
//   baseURL: SERVER.URL,
//   headers: {
//     'Content-Type': 'application/json',
//   },
// });

// axios.interceptors.request.use((config) => {
//   config.headers.Authorization = `${getToken()}`;
//   return config;
// });

// export default axios;

// import axios from "axios";
// import SERVER from '@/api/server'
// import { getToken } from './JWT';

// const baseURL = SERVER.URL
// // const token = localStorage.getItem('jwt')

// // axios 객체 생성
// export default axios.create({
//   baseURL,
//   headers: getToken() ? { Authorization: `${getToken()}` } : { },
//   xhrFields: getToken() ? { withCredentials: true } : { },
// });

import axios from "axios";
import SERVER from '@/api/server'

// axios 객체 생성
export default axios.create({
  baseURL: SERVER.URL,
  headers: {
    Authorization:localStorage.getItem("jwt")
  },
  xhrFields: {
    withCredentials: true
  }
});
