import axios from "axios";

const baseURL = "https://i6a102.p.ssafy.io/api/v1"
const token = localStorage.getItem('jwt')

// axios 객체 생성
export default axios.create({
  baseURL,
  headers: token ? { Authorization: token } : { },
  xhrFields: token ? { withCredentials: true } : { },
});
