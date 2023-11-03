/**
 * 스토리 선택 단계 organism 구현
 * 로직 미구현
 * @params
 * @returns
 */

import { IGameModeCard } from '@/types/components/GameModeCard.type';
import { ISelectGameStory } from '@/types/components/MakeGameProcess.type';
import { gameStoryAtom } from '@/jotai/MakeGameAtom';
import { useAtom } from 'jotai';
import GameModeCard from './GameModeCard';
// import { useState } from 'react';

const SelectGameStory = (props: ISelectGameStory) => {
  // const [selected, setSelected] = useState(0);
  const [, setStory] = useAtom(gameStoryAtom);

  return (
    <div className="pt-[100px] w-full h-full">
      <div className="text-primary text-32 text-center font-hol mb-[30px] caret-transparent ">
        이야기 종류를 선택하세요.
      </div>
      <div className="flex flex-row gap-10 justify-center w-[80%] mx-auto">
        {getStoryList.map((card, idx) => (
          <GameModeCard
            imgUrl={card.imgUrl}
            modeName={card.modeName}
            modeType={card.modeType}
            onClickEvent={() => {
              setStory(tempDummy[idx]);
              props.onGoMakeCharacter();
            }}
            key={idx}
          />
        ))}
      </div>
    </div>
  );
};

const tempDummy = ['판타지', '좀비', '미스테리'];

const getStoryList: IGameModeCard[] = [
  {
    imgUrl: 'Story-fantasy.jpg',
    modeName: '판타지',
    modeType: 0,
  },
  {
    imgUrl: 'Story-zombie.jpg',
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
