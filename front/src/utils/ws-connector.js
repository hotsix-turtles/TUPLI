import Stomp from "webstomp-client"
import SockJS from "sockjs-client"

// TODO: 나중에 드래곤볼
const baseURL = "https://i6a102.p.ssafy.io/api/v1" + "/ws-stomp"
const sock = new SockJS(baseURL);

export default Stomp.over(sock);
