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
  const client = useRef<CompatClient | null>(null);
  const gameCode = '5s2OZy';

  const connectHandler = () => {
    const sock = new SockJS(import.meta.env.VITE_SOCKET_URL);
    client.current = Stomp.over(() => sock);
    client.current.connect({}, () => {
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
          console.log(history);
        },
        {}
      );
    });
  };

  const sendHandler = () => {
    if (client.current)
      client.current.send(
        `/player/${gameCode}`,
        {},
        JSON.stringify({
          gameCode: gameCode,
          raceCode: 'RAC-001',
          jobCode: 'JOB-001',
          nickname: 'Test',
        })
      );
  };

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
