import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useState, useRef, useEffect, useCallback } from 'react';
import Logo from '@/assets/GamePTLogo.svg';
import SideInterface from '@/organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
import ProfileInterface from '@/organisms/ProfileInterface';

const MultiPlayPage = () => {
  const [history, setHistory] = useState<string[] | null>(null);
  const client = useRef<CompatClient | null>(null);
  const gameCode = 'DphDfP';

  // const overStomp = () => {
  //   client.current = Stomp.over(() => {
  //     return new SockJS(import.meta.env.VITE_SOCKET_URL);
  //   });
  // };

  // const subscribe = () => {
  //   console.log('CONNETCTCT');
  //   // callback 함수 설정, 대부분 여기에 sub 함수 씀
  //   client.current?.subscribe(`/topic/player/${gameCode}`, (message) => {
  //     const mes: string = JSON.parse(message.body);
  //     setHistory((prevHistory) => [...prevHistory, mes]);
  //   });
  // };

  // const connectHandler = (subscribe: () => void) => {
  //   console.log("connectedHandler: " , client.current);
  //   if (client.current) {
  //     console.log("IN if")
  //     client.current.connect({}, () => subscribe());
  //   }
  // };

  const connectHandler = () => {
    const sock = new SockJS(import.meta.env.VITE_SOCKET_URL);
    console.log(sock);
    console.log(sock.url);
    client.current = Stomp.over(() => sock);
    console.log(client.current);
    client.current.connect(
      {
      },
      () => {
        console.log("aa");
        
        // 연결 성공 시 해당 방을 구독하면 서버로부터 새로운 매시지를 수신 한다.
        client.current?.subscribe(
          `/topic/player/${gameCode}`,
          (message) => {
            // 기존 대화 내역에 새로운 메시지 추가
            setHistory((prevHistory) => {
              return prevHistory
                ? [...prevHistory, JSON.parse(message.body)]
                : null;
            });
          },
          {
          },
        );
      },
    );
  }

  const sendHandler = useCallback(
    (roomId: string, playerName: string, inputMessage: string) => {
      // if (client.current)
      //   client.current.send(
      //     `${import.meta.env.VITE_SOCKET_URL}/`,
      //     {},
      //     JSON.stringify({
      //       type: 'type',
      //       roomId: roomId,
      //       sender: playerName,
      //       message: inputMessage,
      //     })
      //   );
    },
    []
  );

  // useEffect(() => {
  //   if (client.current && !client.current.connected) connectHandler(subscribe);
  //   else if (!client.current) overStomp();
  //   else subscribe();
  // }, [client.current]);

  useEffect(() => {
    if (!client.current) connectHandler();
  });

  return (
    <div className="w-screen h-screen flex font-hol bg-backgroundDeep text-primary">
      <div className="basis-1/3 h-full flex flex-col justify-between items-start">
        <img src={Logo} alt="로고" className="w-[200px] m-4 ml-6" />
        <div className="w-full h-[400px] flex justify-center">
          <SideInterface />
        </div>
        <ProfileInterface />
      </div>
      <div className="basis-3/4 h-full mr-2">
        <div className="w-full flex justify-end py-1 pr-10">
          <TextButton
            text="게임 나가기"
            onClickEvent={() => console.log('진짜 나가요?')}
          />
        </div>
        <PromptInterface sendHandler={sendHandler} />
      </div>
    </div>
  );
};

export default MultiPlayPage;
