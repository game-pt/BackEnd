import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';

import { stompClient, overStomp, connectStomp } from '@/services/ChatService';
import { useEffect, useState, useRef } from 'react';

const ChattingTab = () => {
  const [message, setMessage] = useState();
  const client = useRef<CompatClient>();

  const connectHandler = () => {
    client.current = Stomp.over(() => {
      const sock = new SockJS(import.meta.env.VITE_SOCKET_URL);
      return sock;
    });
    client.current.connect({}, () => {
      // callback 함수 설정, 대부분 여기에 sub 함수 씀
      client.current?.subscribe(
        `${import.meta.env.VITE_SOCKET_URL}/`,
        (message) => {
          setMessage(JSON.parse(message.body));
        }
      );
    });
  };

  const sendHandler = () => {
    client.current?.send(
        `${import.meta.env.VITE_SOCKET_URL}/`,
        {},
        JSON.stringify({
          type: "type",
          roomId: roomId,
          sender: user.name,
          message: inputMessage
        })
      );
  };

  return (
    <div className="w-full h-full flex flex-col p-8 py-10 bg-transparent text-28 justify-between"></div>
  );
};

export default ChattingTab;
