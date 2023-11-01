import { useState } from 'react';
import './SideInterface.css'; // Import your CSS file
import { TabContent } from '@/types/components/Tab.types';
import LoadingSpinner1 from '@/atoms/LoadingSpinner1';

const SideInterface = () => {
  const [selectedTab, setSelectedTab] = useState('스탯'); // 초기 탭 설정
  const [selectedTabColor, setSelectedTabColor] = useState('#331812'); // 초기 탭 색상 설정
  
  const tabContents: Record<string, TabContent> = {
    '스탯': {
      content: <LoadingSpinner1 />,
      color: "#331812"
    },
    '스킬': {
      content: <LoadingSpinner1 />,
      color: "#381D17"
    },
    '채팅': {
      content: <LoadingSpinner1 />,
      color: "#3D221C"
    },
    '장비': {
      content: <LoadingSpinner1 />,
      color: "#422721"
    },
  };

  const changeTab = (tabName: string) => {
    setSelectedTab(tabName);
  };

  return (
    <div className='w-[350px]'>
      <div className="tab-header w-full flex">
        {Object.keys(tabContents).map((e, _i) => (
          
          <button
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
          <div className='content-area w-[350px] h-[368px]' style={{ backgroundColor: selectedTabColor }}>
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
}

export default SideInterface;
