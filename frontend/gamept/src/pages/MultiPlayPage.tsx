import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useState, useRef, useEffect } from 'react';
import Logo from '@/assets/GamePTLogo.svg';
import SideInterface from '@/organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
import ProfileInterface from '@/organisms/ProfileInterface';

const MultiPlayPage = () => {
  const [history, setHistory] = useState<string[] | null>(null);
  const [chat, setChat] = useState<string[] | null>(null);
  const client = useRef<CompatClient | null>(null);
  const gameCode = 'LD0ZpL';

  // 웹소캣 객체 생성
  const connectHandler = () => {
    const sock = new SockJS(import.meta.env.VITE_SOCKET_URL);
    client.current = Stomp.over(() => sock);

    // 웹 소켓 연결 정보 콘솔에 안뜨게 하기 >> 코드 프리징 시 주석 풀기
    // client.current.debug = () => null;

    client.current.connect({}, () => {
      // 연결 성공 시 해당 방을 구독하면 서버로부터 이벤트 발생 & 프롬프트 추가 시 마다 메세지 수신
      client.current?.subscribe(
        `/topic/player/${gameCode}`,
        (message) => {
          // 기존 프롬프트 내역에 새로운 메시지 추가
          setHistory((prevHistory) => {
            console.log(history);
            return prevHistory
              ? [...prevHistory, JSON.parse(message.body)]
              : JSON.parse(message.body);
          });

        },
        {}
      );

      client.current?.subscribe(
        `/topic/chat/${gameCode}`,
        (message) => {
          // 기존 대화 내역에 새로운 메시지 추가
          setChat((prevChat) => {
            return prevChat
              ? [...prevChat, message.body]
              : [message.body];
          });
        },
        {}
      );
    });
  };

  const disConnected = () => {
    if (client.current !== null) {
      client.current.disconnect();
      client.current = null;
    }
    else console.log("Already Disconnected!!!");
  }

  const sendEventHandler = () => {
    if (client.current)
      client.current.send(
        `/player/${gameCode}`,
        {},
        JSON.stringify({
          gameCode: gameCode,
          raceCode: 'RAC-001',
          jobCode: 'JOB-001',
          nickName: 'LD0ZpL-mGRkeI',
        })
      );
  };

  const sendChatHandler = (text: string) => {
    if (client.current)
      client.current.send(
        `/chat/${gameCode}`,
        {},
        JSON.stringify({
          playerCode: "LD0ZpL-mGRkeI",
          message: text,
        })
      );
  };

  useEffect(() => {
    if (client.current === null) connectHandler();
  });

  return (
    <div className="w-screen h-screen flex font-hol bg-backgroundDeep text-primary">
      <div className="basis-1/3 h-full flex flex-col justify-between items-start">
        <img src={Logo} alt="로고" className="w-[200px] m-4 ml-6" />
        <div className="w-full h-[400px] flex justify-center">
          <SideInterface sendChat={sendChatHandler} chat={chat} />
        </div>
        <ProfileInterface />
      </div>
      <div className="basis-3/4 h-full mr-2">
        <div className="w-full flex justify-end py-1 pr-10">
          <TextButton
            text="게임 나가기"
            onClickEvent={() => disConnected()}
          />
        </div>
        <PromptInterface sendEventHandler={sendEventHandler} />
      </div>
    </div>
  );
};

export default MultiPlayPage;
