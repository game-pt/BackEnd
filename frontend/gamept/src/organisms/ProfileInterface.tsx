import Gauge from '@/atoms/Gauge';

const ProfileInterface = () => {
  const dummyProfilImg = 'player_profile';

  return (
    <div className="w-[450px] h-[197px] flex bg-[url(./src/assets/interface/profile_interface.png)] bg-cover">
      <div className="min-w-[195.28px] h-full flex justify-center items-center">
        <img
          className="min-w-[176px]"
          src={`./src/assets/${dummyProfilImg}.png`}
          alt="player_profile"
        />
      </div>
      <div className="w-full h-full flex flex-col">
        <div className="w-full h-[95px]">
          <div className="text-14 flex justify-around">
            <p>이우석</p>
            <p>Lv.20</p>
            <p>신한 개발자</p>
          </div>
          <Gauge nowHP={100} totalHP={100} type="hp" />
          <Gauge nowHP={100} totalHP={100} type="exp" />
        </div>
        <div></div>
      </div>
    </div>
  );
};

export default ProfileInterface;
