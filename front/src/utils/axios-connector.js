<<<<<<< HEAD
// import baseAxios from 'axios';
// import { getToken } from './JWT';
import axios from 'axios'
=======
import axios from 'axios';
>>>>>>> 5487458d42b3c97ac02b153739ec10cc44928cce

const baseURL = "https://i6a102.p.ssafy.io/api/v1"
const axiosConnector = axios.create({
  baseURL
})

axiosConnector.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt')

    if (token)
    {
      config.headers['Authorization'] = token;
      // true로 하면 cors
      // config.withCredentials = false;
    } else {
      config.withCredentials = false;
    }

    return config;
  },
  (error) => {

    return Promise.reject(error);
  }
);

// axios 객체 생성
export default axiosConnector;
