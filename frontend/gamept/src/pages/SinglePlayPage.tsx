// import React from 'react';
import Logo from '../assets/logo/logo.png';
import ProfileInterface from '../assets/interface/profile_interface.png';
import SideInterface from '../organisms/SideInterface';
import PromptInterface from '@/organisms/PromptInterface';
// import './SinglePlay.css'; // CSS 파일 가져오기

const SinglePlayPage = () => {
  return (
    <div className="w-[1536px] h-full flex font-hol bg-backgroundDeep text-primary">
      <div className="h-full flex flex-col justify-between items-center">
        <img src={Logo} alt="로고" className="w-[300px]" />
        <SideInterface />
        <img
          src={ProfileInterface}
          className="bottom-0 profile-interface w-[400px]"
        />
      </div>
      <div className="w-full">
        <PromptInterface />
      </div>
    </div>
  );
};

export default SinglePlayPage;
