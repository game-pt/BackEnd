// import React from 'react';
import Logo from '../assets/logo/logo.png'
import ProfileInterface from '../assets/interface/profile_interface.png'
import SideInterface from '../organisms/SideInterface';
import './SinglePlay.css'; // CSS 파일 가져오기

function SinglePlayPage() {
  return (
    <div className="singleplay-container" style={{display:'flex'}}>
      <img src={Logo} alt="로고" className='logo' />
      <div className='interface-container'>
      <SideInterface/>
      <img src={ProfileInterface}  className='profile-interface' style={{width:'400px'}} />
      </div>
    </div>
  );
}

export default SinglePlayPage;
