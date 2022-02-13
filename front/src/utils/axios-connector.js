import axios from 'axios';
import SERVER from '@/api/server'

const baseURL = SERVER.URL  // 드래곤볼 검토
// const baseURL = "https://tupli.kr"
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
