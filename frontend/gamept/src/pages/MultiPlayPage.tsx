import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useState, useRef, useEffect } from 'react';
import Logo from '@/assets/GamePTLogo.svg';
import SideInterface from '@/organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
import ProfileInterface from '@/organisms/ProfileInterface';
import { getPromptTopic } from '@/services/GameService';
import { useIndexedDB } from 'react-indexed-db-hook';
import Swal from 'sweetalert2'
import { useNavigate } from 'react-router-dom';

const MultiPlayPage = () => {
  const [history, setHistory] = useState<string[] | null>(null);
  const [chat, setChat] = useState<string[] | null>(null);
  const client = useRef<CompatClient | null>(null);
  const gameCode = 'YMi8mg';
  const playerCode = 'YMi8mg-yl7q7k';
  const db = useIndexedDB("prompt");
  const navigate = useNavigate();

  // 웹소캣 객체 생성
  const connectHandler = () => {
    const sock = new SockJS(import.meta.env.VITE_SOCKET_URL);
    client.current = Stomp.over(() => sock);

    // 웹 소켓 연결 정보 콘솔에 안뜨게 하기 >> 코드 프리징 시 주석 풀기
    // client.current.debug = () => null;

    client.current.connect({}, () => {
      if (client.current == null) {
        console.log('Error');
        return;
      }
      
      // 멀티플레이 용
      // 유저 데이터 업데이트 시 정보 송수신용
      client.current.subscribe(`/topic/player/${gameCode}`, (message) => {
        console.log(JSON.parse(message.body));
        // message.body를 통해 데이터 받아서
        // 프로필 업데이트 로직 수행
        // 해당 방 구독하는 모든 플레이어들 데이터 저장하는 객체에다가 업데이트
      }, {});

      // 연결 성공 시 해당 방을 구독하면 서버로부터 이벤트 발생 & 프롬프트 추가 시 마다 메세지 수신
      client.current.subscribe(
        `/topic/game/${gameCode}`,
        (message) => {
          const body = JSON.parse(message.body);
          // 프롬포트 응답이라면
          if (body.prompt !== undefined) {
            // 원본 데이터와 프롬프트 구분
            const { origin, prompt } = getPromptTopic(body);
            console.log(origin);
            // 인덱스 디비에 프롬프트 데이터 저장.
            db.add({ content: prompt });

          }

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

      // 채팅 구독
      client.current.subscribe(
        `/topic/chat/${gameCode}`,
        (message) => {
          // 기존 대화 내역에 새로운 메시지 추가
          setChat((prevChat) => {
            return prevChat ? [...prevChat, message.body] : [message.body];
          });
        },
        {}
      );
    });
  };

  const disConnected = () => {
    if (client.current !== null) {
      try {
        client.current.debug = () => null;
        client.current.disconnect(() => {
          if (client.current) {
            client.current.unsubscribe('sub-0');
            client.current.unsubscribe('sub-1');
            client.current.unsubscribe('sub-2');
          }
        });
      } catch (err) {
        console.log('disconneted Failed');
      }
      client.current = null;
    } else console.log('Already Disconnected!!!');
  };

  const sendEventHandler = () => {
    if (client.current)
      client.current.send(
        `/player/${gameCode}`,
        {},
        JSON.stringify({
          gameCode: gameCode,
          raceCode: 'RAC-001',
          jobCode: 'JOB-001',
          nickName: playerCode,
        })
      );
  };

  const sendChatHandler = (text: string) => {
    if (client.current)
      client.current.send(
        `/chat/${gameCode}`,
        {},
        JSON.stringify({
          playerCode,
          message: text,
        })
      );
  };

  const leaveGame = () => {
    Swal.fire({
      title: "경고문",
      text: "이 게임 정보가 삭제됩니다람쥐!",
      width: 600,
      padding: "2rem",
      color: "#FBCB73",
      background: "#240903",
      showDenyButton: true,
      confirmButtonText: "머무르기",
      confirmButtonColor: "#F0B279",
      denyButtonText: "나가기",
      denyButtonColor: "#C9C9C9",
      customClass: {
        container: "font-hol",
        popup: "rounded-lg",
        title: "text-48 mb-8",
        htmlContainer: "text-36",
      },
    }).then(result => {
      if (result.isDenied) {
        disConnected();
        db.clear();
        navigate("/");
      }
    })
  }

  useEffect(() => {
    if (client.current === null) {
      connectHandler();
    }

    return () => {
      disConnected();
    };
  }, []);

  useEffect(() => {}, [history]);

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
          <TextButton text="게임 나가기" onClickEvent={() => leaveGame()} />
        </div>
        <PromptInterface gameType="multi" sendEventHandler={sendEventHandler} />
      </div>
    </div>
  );
};

export default MultiPlayPage;
