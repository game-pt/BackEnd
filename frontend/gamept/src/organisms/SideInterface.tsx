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
      color: "#441812"
    },
    '스킬': {
      content: <LoadingSpinner1 />,
      color: "#441812"
    },
    '채팅': {
      content: <LoadingSpinner1 />,
      color: "#441812"
    },
    '장비': {
      content: <LoadingSpinner1 />,
      color: "#441812"
    },
  };

  const changeTab = (tabName: string) => {
    setSelectedTab(tabName);
  };

  return (
    <div>
      <div className="tab-header flex">
        {}
        <button
          className={`custom-button ${selectedTab === '스탯' ? 'active-tab' : ''}`}
          style={{
            backgroundColor: '#331812',
          }}
          onClick={() => {
            changeTab('스탯');
            setSelectedTabColor('#331812');
          }}
        >
          스탯
        </button>
        <button
          className={`custom-button ${selectedTab === '스킬' ? 'active-tab' : ''}`}
          style={{
            backgroundColor: '#381D17',
          }}
          onClick={() => {
            changeTab('스킬');
            setSelectedTabColor('#381D17');
          }}
        >
          스킬
        </button>
        <button
          className={`custom-button ${selectedTab === '채팅' ? 'active-tab' : ''}`}
          style={{
            backgroundColor: '#3D221C',
          }}
          onClick={() => {
            changeTab('채팅');
            setSelectedTabColor('#3D221C');
          }}
        >
          채팅
        </button>
        <button
          className={`custom-button ${selectedTab === '장비' ? 'active-tab' : ''}`}
          style={{
            backgroundColor: '#422721',
          }}
          onClick={() => {
            changeTab('장비');
            setSelectedTabColor('#422721');
          }}
        >
          장비
        </button>
      </div>

      <div className="tab-content">
        {selectedTab in tabContents && (
          <div className='content-area' style={{ width: '344px', height: '368px', backgroundColor: selectedTabColor }}>
            {tabContents[selectedTab].content}
          </div>
        )}
      </div>

      {/* 새로운 영역 추가 */}
      <div className="additional-content">
        {/* 여기에 추가 탭 내용을 표시할 컴포넌트나 요소를 추가할 수 있습니다. */}
      </div>
    </div>
  );
}

export default SideInterface;
