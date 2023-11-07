// import React from 'react';
import Logo from '@/assets/logo/logo.png';
import SideInterface from '@/organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
import ProfileInterface from '@/organisms/ProfileInterface';
// import './SinglePlay.css'; // CSS 파일 가져오기

const SinglePlayPage = () => {
  return (
    <div className="w-screen h-screen flex font-hol bg-backgroundDeep text-primary">
      <div className="w-400 h-full flex flex-col justify-between items-start">
        <img src={Logo} alt="로고" className="w-[300px]" />
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
        <PromptInterface gameType='single' />
      </div>
    </div>
  );
};

export default SinglePlayPage;
