import axios from 'axios';
import SERVER from '@/api/server'
import router from '../router';

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

axiosConnector.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const { data, status } = error.response;
    //TODO: 개발기간엔 뺴놓자는 의견 수렴
    //if (status == 500) router.push({ name: 'Error' })
    return Promise.reject(error);
  }
);

// axios 객체 생성
export default axiosConnector;
