import Gauge from '@/atoms/Gauge';
import axios from 'axios';


const ProfileInterface = () => {
  const dummyProfilImg = 'player_profile';

  const handleLevelUp = async () => {
    try {
      const response = await axios.post('/api/level-up'); // 백엔드 엔드포인트를 적절하게 수정
      if (response.status === 200) {
        // 레벨업 성공한 경우 경험치 게이지를 초기화하거나 다른 조치를 취할 수 있음
      }
    } catch (error) {
      console.error('Failed to level up:', error);
    }
  };

  return (
    <div className="w-[400px] h-[173.58px] flex bg-[url(./src/assets/interface/profile_interface.png)] bg-cover">
      <div className="min-w-[174px] h-full flex justify-center items-center">
        <img
          className="min-w-[150px]"
          src={`./src/assets/${dummyProfilImg}.png`}
          alt="player_profile"
        />
      </div>
      <div className="w-full h-full flex flex-col">
      <div className="w-full">
        <Gauge value={100} total={100} type="hp" />
        <div style={{ marginTop: "-15px" }}>
          <Gauge value={0} total={10} type="exp" onLevelUp={handleLevelUp} />
        </div>
      </div>
      <div className="w-full my-auto text-left px-6">
          <p>이우석</p>
          <p>오크 / 플레임위자드</p>
          <p>Lv.9</p>
        </div>

        </div>
      </div>
  );
};

export default ProfileInterface;
