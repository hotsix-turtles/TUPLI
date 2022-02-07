import axios from "axios";
import SERVER from '@/api/server'

const baseURL = SERVER.URL
const token = localStorage.getItem('jwt')

// axios 객체 생성
export default axios.create({
  baseURL,
  headers: token ? { Authorization: token } : { },
  xhrFields: token ? { withCredentials: true } : { },
});
