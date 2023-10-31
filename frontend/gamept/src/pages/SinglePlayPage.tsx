// import React from 'react';
import Logo from '../assets/logo/logo.png';
import ProfileInterface from '../assets/interface/profile_interface.png';
import SideInterface from '../organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
import TextButton from '@/atoms/TextButton';
// import './SinglePlay.css'; // CSS 파일 가져오기

const SinglePlayPage = () => {
  return (
    <div className="w-screen h-screen flex font-hol bg-backgroundDeep text-primary">
      <div className="h-full flex flex-col justify-between items-center">
        <img src={Logo} alt="로고" className="w-[300px]" />
        <SideInterface />
        <img
          src={ProfileInterface}
          className="bottom-0 profile-interface w-[400px]"
        />
      </div>
      <div className="w-full h-full mx-4">
        <div className="w-full flex justify-end py-1 pr-10">
          <TextButton text={`게임 나가기`} onClickEvent={() => console.log('진짜 나가요?')} />
        </div>
        <PromptInterface />
      </div>
    </div>
  );
};

export default SinglePlayPage;
