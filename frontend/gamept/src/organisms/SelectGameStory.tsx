/**
 * 스토리 선택 단계 organism 구현
 * setStory로 스토리 code 설정
 * 캐릭터 생성으로 navigate
 * @params props
 * onGoMakeCharacter : () => void // 캐릭터 생성 페이지로 navigate
 * @returns void
 */

import { IGameModeCardResponse } from '@/types/components/GameModeCard.type';
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
            // API에서 url을 받아오지 않으므로 modeType을 파일명으로 만들어 url을 매핑시킬 예정
            // 추후 imgUrl로 넣기 직전에 확장자를 더해주는 작업을 해줘야함
            imgUrl={card.modeType}
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

const getStoryList: IGameModeCardResponse[] = [
  {
    modeName: '판타지',
    modeType: 'Story-fantasy.jpg',
  },
  {
    modeName: '좀비',
    modeType: 'Story-zombie.jpg',
  },
  {
    modeName: '미스테리',
    modeType: 'MultiPlay.svg',
  },
];

export default SelectGameStory;
