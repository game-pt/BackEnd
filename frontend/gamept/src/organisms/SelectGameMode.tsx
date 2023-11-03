/**
 * 싱글/멀티플레이 선택 관련 organism 구성
 * multiplay 클릭 시 관련 모달 출현
 * 다음 단계로 넘어가는 로직 미구현
 * @params
 * @returns void
 */

import { useState } from 'react';
import GameModeCard from './GameModeCard';
import { IGameModeCard } from '@/types/components/GameModeCard.type';
import MultiplayModal from './MultiplayModal';

const SelectGameMode = () => {
  const [isShowModal, setIsShowModal] = useState(false);
  return (
    <div className="pt-[100px] w-full h-full">
      <div className="text-primary text-32 text-center font-hol mb-[30px] caret-transparent ">
        게임 모드를 선택하세요.
      </div>
      <div className="flex flex-row gap-10 justify-center w-[60%] mx-auto">
        {response.map((card, idx) => (
          // 멀티플레이의 modal 컨트롤을 위해 wrapper 역할을 하는 div를 추가 설정
          <div
            className="w-full"
            onClick={
              idx === 1 && !isShowModal
                ? () => {
                    setIsShowModal(true);
                  }
                : undefined
            }
          >
            <GameModeCard
              imgUrl={card.imgUrl}
              modeName={card.modeName}
              modeType={card.modeType}
              onClickEvent={() => {}}
              key={idx}
            />
          </div>
        ))}
      </div>
      {isShowModal && <MultiplayModal onClose={() => setIsShowModal(false)} />}
    </div>
  );
};

const response: IGameModeCard[] = [
  {
    imgUrl: 'SinglePlay.svg',
    modeName: '싱글 플레이',
    modeType: 0,
  },
  {
    imgUrl: 'Multiplay.svg',
    modeName: '멀티 플레이',
    modeType: 0,
  },
];

export default SelectGameMode;
