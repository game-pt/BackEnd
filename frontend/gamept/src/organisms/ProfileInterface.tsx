import Gauge from '@/atoms/Gauge';

const ProfileInterface = () => {
  const dummyProfilImg = 'player_profile';

  return (
    <div className="w-[450px] h-[197px] flex bg-[url(/src/assets/interface/profile_interface.png)] bg-cover">
      <div className="min-w-[195.28px] h-full flex justify-center items-center">
        <img
          className="min-w-[176px]"
          src={`./src/assets/${dummyProfilImg}.png`}
          alt="player_profile"
        />
      </div>
      <div className="w-full h-full flex flex-col">
        <div className="w-full h-[110px]">
        <Gauge value={100} total={100} type="hp" /> 
        <Gauge value={0} total={100} type="exp" />
        </div>
        <div className="text-left ml-6">
          <p>이우석</p>
          <p>인간 전사</p>
          <p>LV</p>
        </div>
      </div>
    </div>
  );
};

export default ProfileInterface;
