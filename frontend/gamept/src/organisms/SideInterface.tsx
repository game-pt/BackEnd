import { useState } from 'react';
import './SideInterface.css'; // Import your CSS file
import { SkillValuesType, TabContent } from '@/types/components/Tab.types';
import LoadingSpinner1 from '@/atoms/LoadingSpinner1';

const StatTab = () => {
  const statList: Record<string, number> = {
    힘: 10,
    민첩: 10,
    지능: 10,
    행운: 10,
    매력: 10,
  };

  return (
    <div className="w-full h-full flex flex-col p-8 py-10 bg-transparent text-28 justify-between">
      {Object.keys(statList).map((e, i) => (
        <div key={`stat_${i}`} className="w-full flex">
          <p className="basis-1/4 text-center">{e}</p>
          <p className="basis-3/4 text-left pl-4">{statList[e]}</p>
        </div>
      ))}
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
      img: 'Diving_assault.png',
      desc: '독수리를 부른다.',
    },
    토하기: {
      img: 'Drain_mana.png',
      desc: '과음 후 파전 만들기.',
    },
    '세게 때리기': {
      img: 'Power_of_blessing.png',
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
    장비: {
      content: <LoadingSpinner1 />,
      color: '#422721',
    },
    채팅: {
      content: <LoadingSpinner1 />,
      color: '#3D221C',
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
