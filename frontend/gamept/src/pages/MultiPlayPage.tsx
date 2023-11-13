import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useState, useRef, useEffect } from 'react';
import Logo from '@/assets/logo/logo.png';
import SideInterface from '@/organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
import ProfileInterface from '@/organisms/ProfileInterface';
import { useIndexedDB } from 'react-indexed-db-hook';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import usePrompt from '@/hooks/usePrompt';
import { usePromptAtom } from '@/jotai/PromptAtom';
import { IEventType, IActsType } from '@/types/components/Prompt.types';

const MultiPlayPage = () => {
  const [chat, setChat] = useState<string[] | null>(null);
  const [event, setEvent] = useState<IEventType | null>(null);
  const [isPromptFetching, setIsPromptFetching] = useState<boolean>(false);
  const client = useRef<CompatClient | null>(null);
  const [_getPrompt, setPrompt] = usePrompt();
  const promptAtom = usePromptAtom();
  const gameCode = 'gq6KR1';
  const playerCode = 'gq6KR1-jBSe9s';
  const db = useIndexedDB('prompt');
  const navigate = useNavigate();

  useEffect(() => {
    if (promptAtom[promptAtom.length - 1][0].mine) setIsPromptFetching(true);
  }, [promptAtom])

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
      client.current.subscribe(
        `/topic/player/${gameCode}`,
        (message) => {
          console.log(JSON.parse(message.body));
          // message.body를 통해 데이터 받아서
          // 프로필 업데이트 로직 수행
          // 해당 방 구독하는 모든 플레이어들 데이터 저장하는 객체에다가 업데이트
        },
        {}
      );

      // 전투 이벤트 업데이트 시 정보 송수신용
      client.current.subscribe(
        `/topic/fight/${gameCode}`,
        (message) => {
          const body = JSON.parse(message.body);

          if (body.prompt !== undefined) {
            console.log(body);
            // 원본 데이터와 프롬프트 구분
            const prompt = body.prompt.split("\n").map((e: string) => {
              return { msg: e, mine: false }
            });

            setPrompt(prompt);
            setIsPromptFetching(false);
          }

          if (body.endYn === "Y") setEvent(null);

          // setHP
          // HP 상태 값 변화
          // body.playerHp

        },
        {}
      );

      // 연결 성공 시 해당 방을 구독하면 서버로부터 이벤트 발생
      client.current.subscribe(
        `/topic/prompt/${gameCode}`,
        async (message) => {
          const body = JSON.parse(message.body);

          // 프롬포트가 있다면
          if (body.prompt !== undefined) {
            console.log(body);
            // 원본 데이터와 프롬프트 구분
            const prompt = body.prompt.split("\n").map((e: string) => {
              return { msg: e, mine: false }
            });

            setPrompt(prompt);
            setIsPromptFetching(false);
          }

          // Event가 있다면
          if (body.event !== null) {
            // setEvent(body.event);
            
            const ChoiceFromDB = (await db.getAll()).map((e) => e.content);
            if (ChoiceFromDB.length === 0) {
              await db.add({ choice: body });
            } else {
              await db.deleteRecord(1);
            }

          }
          console.log("setEvent")
          setEvent({
            "eventCode": "EVT-001",
            "eventName": "전투",
            "acts": [
              {
                  actCode: "act-001",
                  actName: "일반 공격",
                  subtask: 'NONE'
              },
              {
                  actCode: "act-002",
                  actName: "스킬",
                  subtask: "SKILL"
              },
              {
                  actCode: "act-003",
                  actName: "아이템",
                  subtask: "ITEM"
              }
          ]
        });
        },
        {}
      );

      // 채팅 구독
      client.current.subscribe(
        `/topic/chat/${gameCode}`,
        async (message) => {
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
            client.current.unsubscribe('sub-3');
          }
        });
      } catch (err) {
        console.log('disconneted Failed');
      }
      client.current = null;
    } else console.log('Already Disconnected!!!');
  };

  const sendPromptHandler = (text: string) => {
    // 사용자가 입력한 프롬프트 송신 메서드
    if (client.current) {
      client.current.send(
        `/prompt/${gameCode}`,
        {},
        JSON.stringify({
          playerCode,
          prompt: text,
        })
      );
      setIsPromptFetching(true);
      setPrompt([{ msg: text, mine: true }]);
    }
  }

  const sendEventHandler = (choice: IActsType) => {
    // 주사위 돌리고 난 후
    // 선택지 선택 요청

    console.log(choice);
    // 사용자가 선택한 선택지 송신 메서드
    if (client.current) {
      client.current.send(
        `/event/${gameCode}`,
        {},
        JSON.stringify({
          gameCode,
          raceCode: 'RAC-001',
          jobCode: 'JOB-001',
          nickName: playerCode,
        })
      );
    }
      
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
      title: '경고문',
      text: '이 게임 정보가 삭제됩니다람쥐!',
      width: 600,
      padding: '2rem',
      color: '#FBCB73',
      background: '#240903',
      showDenyButton: true,
      confirmButtonText: '머무르기',
      confirmButtonColor: '#F0B279',
      denyButtonText: '나가기',
      denyButtonColor: '#C9C9C9',
      customClass: {
        container: 'font-hol',
        popup: 'rounded-lg',
        title: 'text-48 mb-8',
        htmlContainer: 'text-36',
      },
    }).then((result) => {
      if (result.isDenied) {
        disConnected();
        db.clear();
        navigate('/');
      }
    });
  };

  useEffect(() => {
    if (client.current === null) {
      connectHandler();
    }

    return () => {
      disConnected();
    };
  }, []);

  return (
    <div className="w-screen h-screen flex font-hol bg-backgroundDeep text-primary">
      <div className="w-400 h-full flex flex-col justify-between items-start">
        <img src={Logo} alt="로고" className="w-[300px]" />
        <div className="w-full h-[400px] flex justify-center">
          <SideInterface sendChat={sendChatHandler} chat={chat} />
        </div>
        <ProfileInterface />
      </div>
      <div className="basis-3/4 h-full mr-2">
        <div className="w-full flex justify-end py-1 pr-10">
          <TextButton text="게임 나가기" onClickEvent={() => leaveGame()} />
        </div>
        <PromptInterface event={event} isFetching={isPromptFetching} gameType="single" sendEventHandler={sendEventHandler} sendPromptHandler={sendPromptHandler} />
      </div>
    </div>
  );
};

export default MultiPlayPage;
