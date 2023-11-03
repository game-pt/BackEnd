import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from "@stomp/stompjs";

let stompClient: CompatClient | null = null;

const overStomp = () => {
  stompClient = Stomp.over(() => {
    return new SockJS(import.meta.env.VITE_SOCKET_URL);
  });

  stompClient.debug = () => {};
};

const connectStomp = (callback: () => void) => {
  if (stompClient) // stomp 객체 있으면 연결 시도
    stompClient.connect({}, callback);
  else // stomp 객체 없으면 overStomp 호출
    overStomp();
};



export { stompClient, overStomp, connectStomp };