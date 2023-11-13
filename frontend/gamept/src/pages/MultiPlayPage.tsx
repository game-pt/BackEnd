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
import {
  IEventType,
  IActsType,
  IGetPromptType,
  ISubtaskType,
} from '@/types/components/Prompt.types';
import DiceModal from '@/organisms/DiceModal';
import { IDice } from '@/types/components/Dice.types';
import { useAtom } from 'jotai';
import { characterStatusAtom } from '@/jotai/CharacterStatAtom';

const MultiPlayPage = () => {
  const [chat, setChat] = useState<string[] | null>(null);
  const [event, setEvent] = useState<IEventType | null>({
    eventCode: 'EVT-001',
    eventName: '전투',
    acts: [
      {
        actCode: 'act-001',
        actName: '일반 공격',
        subtask: 'NONE',
      },
      {
        actCode: 'act-002',
        actName: '스킬',
        subtask: 'SKILL',
      },
      {
        actCode: 'act-003',
        actName: '아이템',
        subtask: 'ITEM',
      },
    ],
  });
  const [isPromptFetching, setIsPromptFetching] = useState<boolean>(false);
  const [isShowDice, setIsShowDice] = useState<boolean>(false);
  const [dice, setDice] = useState<IDice | null>(null);
  const client = useRef<CompatClient | null>(null);
  const [_getPrompt, setPrompt] = usePrompt();
  const promptAtom = usePromptAtom();
  const [status, setStatus] = useAtom(characterStatusAtom);
  const gameCode = 'NngIB9';
  const playerCode = 'NngIB9-aZxrO5';
  const db = useIndexedDB('prompt');
  const navigate = useNavigate();

  useEffect(() => {
    console.log(event);
    if (promptAtom[promptAtom.length - 1][0].mine) setIsPromptFetching(true);
  }, [promptAtom, event]);

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

      client.current.onStompError = function (frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
      };

      // 멀티플레이 용
      // 유저 데이터 업데이트 시 정보 송수신용
      client.current.subscribe(
        `/topic/dice/${gameCode}`,
        (message) => {
          const body = JSON.parse(message.body);

          if (body.dice1) {
            console.log(body);
            console.log(event);
            setDice(body);
            setIsShowDice(true);
          }
        },
        {}
      );

      // 선택지 데이터 정보 송신용
      client.current.subscribe(
        `/topic/select/${gameCode}`,
        (message) => {
          const body = JSON.parse(message.body);

          if (body.prompt !== undefined) {
            console.log(body);
            // 원본 데이터와 프롬프트 구분
            const prompt = body.prompt.split('\n').map((e: string) => {
              return { msg: e, mine: false };
            });

            setPrompt(prompt);
            setIsPromptFetching(false);
          }

          if (body.gameOverYn === 'Y') {
            // 게임 종료 API 호출
            setEvent(null);
            navigate('/ending');
            return;
          }

          if (body.itemYn === 'Y') {
            // Item 획득 로직
          }
        },
        {}
      );

      // 하위 선택지 조회 데이터 송수신용
      client.current.subscribe(
        `/topic/subtask/${gameCode}`,
        (message) => {
          // 하위 선택지 요청에 대한 데이터가 들어온다면
          // 선택지에다가 하위 선택지로 업데이트를 해줘야한다.
          const body = JSON.parse(message.body);

          const newActs: IActsType[] = body.map((e: ISubtaskType) => {
            return {
              actCode: e.code,
              actName: e.name,
              subtask: `isSubTask_${e.code.split("-")[0]}`
            }
          })
          console.log(body);
          // event 상태를 업데이트
          // acts 부분만 body로 변경
          setEvent((prevEvent) => {
            if (prevEvent !== null) {
              return { ...prevEvent, acts: newActs };
            } else return null;
          });
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
            const prompt = body.prompt.split('\n').map((e: string) => {
              return { msg: e, mine: false };
            });

            setPrompt(prompt);
            setIsPromptFetching(false);
          }

          if (body.endYn === 'Y') {
            // 종료 API 호출
            setEvent(null);
          }

          // setHP
          // HP 상태 값 변화
          // body.playerHp
        },
        {}
      );

      // 프롬프트 데이터 송수신용
      client.current.subscribe(
        `/topic/prompt/${gameCode}`,
        async (message) => {
          const body = JSON.parse(message.body);

          // 프롬포트가 있다면
          if (body.prompt !== undefined) {
            console.log(body);
            // 원본 데이터와 프롬프트 구분
            const prompt = body.prompt.split('\n').map((e: string) => {
              return { msg: e, mine: false };
            });

            setPrompt(prompt);
            setIsPromptFetching(false);
          }

          // Event가 있다면
          if (body.event !== null) {
            // setEvent(body.event);

            const ChoiceFromDB = (await db.getAll())
              .filter((v) => v.choice !== undefined)
              .map((e) => e);

            // 직전 선택지 인덱스 디비에 저장
            if (ChoiceFromDB.length === 0) {
              await db.add({ choice: body });
            } else if (ChoiceFromDB.length <= 1) {
              await db.update({ choice: body, id: ChoiceFromDB[0].id });
            } else {
              for (let i = 0; i < ChoiceFromDB.length - 1; i++) {
                await db.deleteRecord(ChoiceFromDB[i].id);
              }
            }
          }
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
            client.current.unsubscribe('sub-4');
            client.current.unsubscribe('sub-5');
          }
        });
      } catch (err) {
        console.log('disconneted Failed');
      }
      client.current = null;
    } else console.log('Already Disconnected!!!');
  };

  const getDicesHandler = () => {
    if (client.current) {
      client.current.send(
        `/dice/${gameCode}`,
        {},
        JSON.stringify({
          playerCode
        })
      )
    }
  }

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
  };

  const getSubtaskHandler = (eventCode: string, subtask: string) => {
    if (client.current) {
      client.current.send(
        `/subtask/${gameCode}`,
        {},
        JSON.stringify({
          playerCode,
          eventCode,
          subtask,
        })
      );
    }
  };

  const sendEventHandler = async (choice: IActsType) => {
    // 주사위 돌리고 난 후
    // 선택지 선택 요청
    console.log(choice);
    console.log(promptAtom);
    // 사용자가 선택한 선택지 송신 메서드
    if (client.current) {
      const body: IGetPromptType = (await db.getAll())
        .filter((v) => v.choice !== undefined)
        .map((e) => e.choice)[0];
      // SubTask를 선택했다면
      const checkSub = choice.subtask.split("_");
      console.log("body ", body);
      if (checkSub.length > 1 && checkSub[0] === "isSubTask") {
        client.current.send(
          `/fight/${gameCode}`,
          {},
          JSON.stringify({
            playerCode,
            actCode: choice.actCode,
            subtask: checkSub[1],
            // gmonsterCode: body.monster
            gmonsterCode: 'ILQFak'
          })
        )
        console.log("전투")
        getDicesHandler();
        return;
      }

      // subtask가 있다면
      if (choice.subtask !== 'NONE') {
        console.log(event);
        if (choice.subtask === 'ITEM' && status.itemList.length === 0) {
          Swal.fire({
            title: '경고문',
            text: '가지고 계신 아이템이 없습니다! 다른 선택지를!',
            width: 600,
            padding: '2.5rem',
            color: '#FBCB73',
            background: '#240903',
            confirmButtonText: '확인',
            confirmButtonColor: '#F0B279',
            customClass: {
              container: 'font-hol',
              popup: 'rounded-lg',
              title: 'text-48 mb-8',
            },
          });
          return;
        }
        // getSubtaskHandler(body.event.eventCode, choice.subtask);
        if (event)
          getSubtaskHandler(event.eventCode, choice.subtask);

        return;
      }

      client.current.send(
        `/select/${gameCode}`,
        {},
        JSON.stringify({
          gameCode,
          raceCode: 'RAC-001',
          jobCode: 'JOB-001',
          nickName: playerCode,
        })
      );
      getDicesHandler();
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
        <PromptInterface
          event={event}
          isFetching={isPromptFetching}
          gameType="single"
          sendEventHandler={sendEventHandler}
          sendPromptHandler={sendPromptHandler}
        />
      </div>
      {(dice && isShowDice) && <DiceModal dice1={dice.dice1} dice2={dice.dice2} dice3={dice.dice3} onClose={() => setIsShowDice(false)} />}
    </div>
  );
};

export default MultiPlayPage;
