import { useEffect, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import './SideInterface.css'; // Import your CSS file
import { SkillValuesType, TabContent } from '@/types/components/Tab.types';
import { CompatClient, Stomp } from '@stomp/stompjs';

import ChattingTab from '@/atoms/ChattingTab';
import { ISideInterface } from '@/types/components/SideInterface.types';
import { TbNavigation } from 'react-icons/tb';
import { GiCardDiscard } from 'react-icons/gi';

//////////////////////////////////////////////////////////////////////////
/* 빌드 에러 방지용 임시 주석
const fetchInitialStats = async () => {
  try {
    const response = await axios.get('/api/initial-stats'); // API 엔드포인트를 백엔드에 맞게 수정
    return response.data;
  } catch (error) {
    console.error('Failed to fetch initial stats:', error);
    return {}; // API 요청 실패 시 빈 객체 반환
  }
};
*/
/////////////////////////////////////////////////////////////////////////

const StatTab = () => {
  const [statList, setStatList] = useState<Record<string, number>>({
    // 힘: 10,
    // 건강: 10,
    // 지능: 10,
    // 민첩: 10,
    // 매략: 10,
    // 행운: 10
  });

  // useEffect(() => {
  //   // WebSocket 연결 설정
  //   const socket = new SockJS(import.meta.env.VITE_SOCKET_URL); // 서버 주소와 WebSocket 엔드포인트를 설정합니다.

  //   // 초기 스탯을 백엔드에서 가져와 설정
  //   socket.onopen = () => {
  //     socket.send('fetchInitialStats'); // 서버로 초기 스탯을 가져오라는 메시지를 보냅니다.
  //   };

  //   // 서버로부터 스탯 데이터를 수신하면 업데이트합니다.
  //   socket.onmessage = (event) => {
  //     const receivedData = JSON.parse(event.data);
  //     if (receivedData.type === 'initialStats') {
  //       setStatList(receivedData.stats);
  //     }
  //   };

  //   // 에러 처리
  //   socket.onerror = (error) => {
  //     console.error('WebSocket error:', error);
  //   };

  //   // 연결 종료 시
  //   socket.onclose = () => {
  //     console.log('WebSocket connection closed.');
  //   };

  //   // 컴포넌트 언마운트 시 WebSocket 연결 종료
  //   return () => {
  //     socket.close();
  //   };
  // }, []);

  const [statPoints, setStatPoints] = useState(20);

  const increaseStat = (statName: string) => {
    if (statPoints > 0) {
      // 서버로 스탯 업데이트 메시지를 보냄
      const socket = new SockJS(import.meta.env.VITE_SOCKET_URL);
      socket.send(JSON.stringify({ type: 'updateStat', statName }));

      // 클라이언트에서 먼저 업데이트한 후 서버로 메시지를 보냅니다.
      setStatList((prevState) => ({
        ...prevState,
        [statName]: prevState[statName] + 1,
      }));
      setStatPoints(statPoints - 1);
    }
  };

  // const levelUp = async () => {
  //   try {
  //     const response = await axios.post('/api/level-up'); // 레벨업을 백엔드에 맞게 수정
  //     if (response.status === 200) {
  //       setStatPoints(statPoints + 1);
  //     }
  //   } catch (error) {
  //     console.error('Failed to level up:', error);
  //   }
  // };

  return (
    <div className="w-full h-full flex flex-col p-8 py-10 bg-transparent text-xl justify-between">
      {Object.keys(statList).map((statName, i) => (
        <div key={`stat_${i}`} className="w-full flex">
          <p className="basis-1/4 text-center">{statName}</p>
          <p className="basis-3/4 text-center">{statList[statName]}</p>
          {statPoints > 0 && (
            <button
              onClick={() => increaseStat(statName)}
              className="bg-transparent px-0 py-0"
            >
              <TbNavigation />
            </button>
          )}
        </div>
      ))}
      <div className="text-base flex items-center justify-center">
        <p>스탯 포인트 :&nbsp;</p>
        <div className="border-2 shadow-md border-primary px-2">
          {statPoints}
        </div>
      </div>
    </div>
  );
};

const SkillTab = () => {
  const [skillList, setSkillList] = useState<SkillValuesType[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 서버에서 스킬 목록을 가져오는 함수
    const fetchSkills = async () => {
      try {
        // API 엔드포인트에 맞게 수정
        const response = await axios.get(
          import.meta.env.VITE_SERVER_URL + '/player',
          {
            // 요청에 필요한 데이터가 있다면 여기에 추가
            params: {
              gameCode: 'MmwNxr',
              playerCode: 'MmwNxr-kX3zr1',
            },
          }
        );

        // "job" 객체에서 "skillList"를 추출
        const jobSkillList = response.data.job.skillList;

        // jobSkillList를 객체로 변환하여 상태에 저장
        const updatedSkillList = jobSkillList.map(
          (skill: { name: string; desc: string }) => ({
            name: skill.name,
            img: `path-to-images/${skill.name}.png`, // 이미지 경로에 대한 로직은 필요에 따라 수정
            desc: skill.desc,
          })
        );

        setSkillList(updatedSkillList); // 서버에서 받아온 스킬 목록을 상태에 저장
        setLoading(false); // 로딩 상태를 해제
      } catch (error) {
        console.error('스킬 목록을 불러오는 중 오류가 발생했습니다.', error);
        setLoading(false); // 오류 발생 시에도 로딩 상태를 해제
      }
    };

    fetchSkills();
  }, []);

  return (
    <div className="w-full h-full flex flex-col p-6 bg-transparent text-16 overflow-y-auto">
      {loading ? (
        <div>스킬을 불러오는 중입니다...</div>
      ) : (
        skillList.map((skill, i) => (
          <div key={`skill_${i}`} className="w-full flex my-2 items-center">
            <img
              className="w-[63px]"
              src={`./src/assets/skill/${skill.img}`}
              alt={`${i}_skill`}
            />
            <p className="basis-3/4 text-left pl-4 text-white">
              {skill.name}: {skill.desc}
            </p>
          </div>
        ))
      )}
    </div>
  );
};

const ItemTab = () => {
  const [itemList, setItemList] = useState<{
    [key: string]: { img: string; desc: string };
  }>({
    빨간포션: {
      img: 'Small Potion_00.png',
      desc: '마시면 체력+10.',
    },
    크리스탈: {
      img: 'protection crystal.png',
      desc: '가지고 있으면 공격력+10.',
    },
    보석: {
      img: 'Gems_03.png',
      desc: '가지고 있으면 공격력+1.',
    },
    망원경: {
      img: 'advance lens.png',
      desc: '멀리보기 가능.',
    },
  });

  const [hoveredItem, setHoveredItem] = useState('');

  const handleMouseEnter = (itemName: string) => {
    setHoveredItem(itemName);
  };

  const handleMouseLeave = () => {
    setHoveredItem('');
  };

  const handleItemDelete = (itemName: string) => {
    const updatedItemList = { ...itemList };
    delete updatedItemList[itemName];
    setItemList(updatedItemList);
  };

  return (
    <div className="w-full h-full flex flex-col p-6 bg-transparent text-16">
      {Object.keys(itemList).map((e, i) => (
        <div
          key={`item_${i}`}
          className="w-full flex my-2 items-center"
          onMouseEnter={() => handleMouseEnter(e)}
          onMouseLeave={handleMouseLeave}
        >
          <div className="border border-[#4F3F2B] rounded">
            <img
              className="w-[63px] rounded"
              src={`./src/assets/items/${itemList[e].img}`}
              alt={`${i}item`}
            />
          </div>
          <div className="basis-3/4 text-left pl-4 text-white">
            {e}: {itemList[e].desc}
          </div>
          {hoveredItem === e && (
            <button
              className="bg-transparent px-0 py-0 ml-2"
              onClick={() => handleItemDelete(e)}
            >
              <GiCardDiscard />
            </button>
          )}
        </div>
      ))}
    </div>
  );
};

const SideInterface = (props: ISideInterface) => {
  const client = useRef<CompatClient | null>(null);
  const [selectedTab, setSelectedTab] = useState('스탯'); // 초기 탭 설정
  const [selectedTabColor, setSelectedTabColor] = useState('#331812'); // 초기 탭 색상 설정
  const gameCode = 'MmwNxr';
  const playerCode = 'MmwNxr-kX3zr1';

  useEffect(() => {
    connectHandler();
    // sendEventHandler();
  }, []);

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

      client.current.subscribe(`/topic/player`, (message) => {
        console.log(JSON.parse(message.body));
      });
    });
  };

  //////////////////////////////////////////////////////////////////////////
  /* 빌드 에러 방지용 임시 주석
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
  */
  //////////////////////////////////////////////////////////////////////////

  const sendEventHandler = () => {
    if (client.current)
      client.current.send(
        `/player`,
        {},
        JSON.stringify({
          gameCode: gameCode,
          nickName: playerCode,
        })
      );
  };

  const tabContents: Record<string, TabContent> = props.sendChat
    ? {
        스탯: {
          content: <StatTab />,
          color: '#290E08',
        },
        스킬: {
          content: <SkillTab />,
          color: '#2E130D',
        },
        아이템: {
          content: <ItemTab />,
          color: '#331812',
        },
        채팅: {
          content: <ChattingTab chat={props.chat} sendChat={props.sendChat} />,
          color: '#422721',
        },
      }
    : {
        스탯: {
          content: <StatTab />,
          color: '#331812',
        },
        스킬: {
          content: <SkillTab />,
          color: '#381D17',
        },
        아이템: {
          content: <ItemTab />,
          color: '#3D221C',
        },
      };

  const changeTab = (tabName: string) => {
    setSelectedTab(tabName);
  };

  return (
    <div className="w-[350px] text-lg" onClick={sendEventHandler}>
      <div className="tab-header w-full flex">
        {Object.keys(tabContents).map((e, i) => (
          <button
            key={`tabContent_${i}`}
            // className={`custom-button ${selectedTab === e ? 'active-tab' : ''}`}
            className={`p-[1.5%] border-none outline-none rounded-none rounded-t-md focus:outline-transparent focus:outline focus:outline-2`}
            style={{
              width: `${100 / Object.keys(tabContents).length}%`,
              backgroundColor: tabContents[e].color,
            }}
            onClick={() => {
              changeTab(e);
              setSelectedTabColor(tabContents[e].color);
            }}
          >
            {e}
          </button>
        ))}
      </div>

      <div className="tab-content">
        {selectedTab in tabContents && (
          <div
            className="content-area w-[350px] h-[368px]"
            style={{ backgroundColor: selectedTabColor }}
          >
            {tabContents[selectedTab].content}
          </div>
        )}
      </div>
    </div>
  );
};

export default SideInterface;
