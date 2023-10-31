import { useState } from 'react';
import './SideInterface.css'; // Import your CSS file

function SideInterface() {
  const [selectedTab, setSelectedTab] = useState('스탯'); // 초기 탭 설정
  const tabContents: Record<string, string> = {
    '스탯': '스탯 내용',
    '스킬': '스킬 내용',
    '채팅': '채팅 내용',
    '장비': '장비 내용',
  };

  const changeTab = (tabName: string) => {
    setSelectedTab(tabName);
  };
  const [selectedTabColor, setSelectedTabColor] = useState('#331812'); // 초기 탭 색상 설정

  return (
    <div>
      <div className="tab-header" style={{ display: 'flex' }}>
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
            {tabContents[selectedTab]}
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
