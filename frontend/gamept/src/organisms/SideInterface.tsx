import { useEffect, useState } from 'react';
import axios from 'axios';
import './SideInterface.css'; // Import your CSS file
import { SkillValuesType, ItemValuesType, TabContent } from '@/types/components/Tab.types';
import LoadingSpinner1 from '@/atoms/LoadingSpinner1';
import { TbNavigation } from "react-icons/tb";

const fetchInitialStats = async () => {
  try {
    const response = await axios.get('/api/initial-stats'); // API 엔드포인트를 백엔드에 맞게 수정
    return response.data;
  } catch (error) {
    console.error('Failed to fetch initial stats:', error);
    return {}; // API 요청 실패 시 빈 객체 반환
  }
};

const StatTab = () => {
  const [statList, setStatList] = useState<Record<string, number>>({
  });
  
  useEffect(() => {
    // 초기 스탯을 백엔드에서 가져와 설정
      fetchInitialStats()
        .then(statData => {
          setStatList(statData);
        })
  }, []);

  const [statPoints, setStatPoints] = useState(5);

  const increaseStat = (statName: string) => {
    if (statPoints > 0) {
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
            <button onClick={() => increaseStat(statName)} className="bg-transparent px-0 py-0">
              <TbNavigation />
            </button>
          )}
        </div>
      ))}
      <div className="text-base flex items-center justify-center">
        <p>스탯 포인트 :&nbsp;</p>
        <div className="border-2 shadow-md border-primary px-2">{statPoints}</div>
      </div>
    </div>
  );
};

const SkillTab = () => {
  const skillList: Record<string, SkillValuesType> = {
    얼리기: {
      img: 'Blizzard.png',
      desc: '상대방을 얼린다.',
    },
    독수리: {
      img: 'Diving assault.png',
      desc: '독수리를 부른다.',
    },
    토하기: {
      img: 'Drain mana.png',
      desc: '과음 후 파전 만들기.',
    },
    '세게 때리기': {
      img: 'Power of blessing.png',
      desc: '읏~챠~.',
    },
  };

  return (
    <div className="w-full h-full flex flex-col p-6 py-8 bg-transparent text-16 justify-between">
      {Object.keys(skillList).map((e, i) => (
        <div key={`skill_${i}`} className="w-full flex my-2 items-center">
          <img
            className="w-[63px]"
            src={`./src/assets/skill/${skillList[e].img}`}
            alt={`${i}_skill`}
          />
          <p className="basis-3/4 text-left pl-4 text-white">
            {e}: {skillList[e].desc}
          </p>
        </div>
      ))}
    </div>
  );
};
const ItemTab = () => {
  const itemList: Record<string, ItemValuesType> = {
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
  };

  return (
    <div className="w-full h-full flex flex-col p-6 py-4 bg-transparent text-16 justify-between">
      {Object.keys(itemList).map((e, i) => (
        <div key={`skill_${i}`} className="w-full flex my-2 items-center">
          <div className="border border-[#4F3F2B] rounded">
            <img
              className="w-[63px] rounded"
              src={`./src/assets/items/${itemList[e].img}`}
              alt={`${i}_skill`}
            />
          </div>
          <p className="basis-3/4 text-left pl-4 text-white">
            {e}: {itemList[e].desc}
          </p>
        </div>
      ))}
    </div>
  );
};
const SideInterface = () => {
  const [selectedTab, setSelectedTab] = useState('스탯'); // 초기 탭 설정
  const [selectedTabColor, setSelectedTabColor] = useState('#331812'); // 초기 탭 색상 설정

  const tabContents: Record<string, TabContent> = {
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
    채팅: {
      content: <LoadingSpinner1 />,
      color: '#422721',
    },
  };

  const changeTab = (tabName: string) => {
    setSelectedTab(tabName);
  };

  return (
    <div className="w-[350px]">
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

      {/* 새로운 영역 추가 ->>??? 어떤 영역인가요??
      <div className="additional-content">
        여기에 추가 탭 내용을 표시할 컴포넌트나 요소를 추가할 수 있습니다.
      </div> */}
    </div>
  );
};

export default SideInterface;
