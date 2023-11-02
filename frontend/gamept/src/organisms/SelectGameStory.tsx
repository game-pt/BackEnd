/**
 * 스토리 선택 단계 organism 구현
 * 로직 미구현
 * @params
 * @returns
 */

import { IGameModeCard } from '@/types/components/GameModeCard.type';
import GameModeCard from './GameModeCard';
// import { useState } from 'react';

const SelectGameStory = () => {
  // const [selected, setSelected] = useState(0);

  return (
    <div className="pt-[100px] w-full h-full">
      <div className="text-primary text-32 text-center font-hol mb-[30px] caret-transparent ">
        이야기 종류를 선택하세요.
      </div>
      <div className="flex flex-row gap-10 justify-center w-[80%] mx-auto">
        {getStoryList.map((card, idx) => (
          // 멀티플레이의 modal 컨트롤을 위해 wrapper 역할을 하는 div를 추가 설정
          <GameModeCard
            imgUrl={card.imgUrl}
            modeName={card.modeName}
            modeType={card.modeType}
            onClickEvent={() => {}}
            key={idx}
          />
        ))}
      </div>
    </div>
  );
};

const getStoryList: IGameModeCard[] = [
  {
    imgUrl: 'SinglePlay.svg',
    modeName: '판타지',
    modeType: 0,
  },
  {
    imgUrl: 'MultiPlay.svg',
    modeName: '좀비',
    modeType: 0,
  },
  {
    imgUrl: 'MultiPlay.svg',
    modeName: '미스테리',
    modeType: 0,
  },
];

export default SelectGameStory;
