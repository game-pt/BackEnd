/*
 * 캐릭터 종족/직업 선택 컴포넌트 구현
 * 캐릭터 코드에 따른 프로필 이미지 세팅
 * SwitchGenderBtn 클릭 시 성별 전환되어서 이미지 제공
 * 캐릭터 코드를 어떻게 처리할지 아직 미정
 * onClickEvent로 케릭터 세팅할때 setState 등을 관리할 예정?
 */

import {
  ICharacterCard,
  ISwitchGender,
} from '@/types/components/CharacterCard.types';
import ProfileImage from '@/atoms/ProfileImage';
import MakeCharacterStatContainer from '@/atoms/MakeCharacterStatContainer';
import SwitchToMale from '@/assets/switchToMale.png';
import SwitchToFemale from '@/assets/switchToFemale.png';
import { useState, MouseEvent } from 'react';

const SwitchGenderBtn = (props: ISwitchGender) => {
  return (
    <div
      className="absolute bg-[url(./assets/MakeCharacterStatPanel.svg)] bg-center outline outline-2 outline-black/70 w-[40px] h-auto rounded-md p-2 right-5 bg-url"
      onClick={props.onClickEvent}
    >
      {props.gender === 0 ? (
        <img src={SwitchToMale} alt="성별 전환 버튼" />
      ) : (
        <img src={SwitchToFemale} alt="성별 전환 버튼" />
      )}
    </div>
  );
};

const CharacterCard = (props: ICharacterCard) => {
  const [gender, setGender] = useState(props.gender);

  const handleGenderBtn = (event: MouseEvent) => {
    event.stopPropagation();
    setGender(1 - (gender ?? 1));
  };

  return (
    <div
      onClick={() => {
        if (props.onSetCharacter) {
          props.onSetCharacter(gender ?? 0, props.characterCode);
        }
        if (props.onNextLevel) {
          props.onNextLevel();
        }
      }}
      className="relative w-[300px] h-[430px] bg-containerLight drop-shadow-xl rounded-[10px] p-5 flex flex-col justify-between caret-transparent"
    >
      <SwitchGenderBtn gender={gender ?? 1} onClickEvent={handleGenderBtn} />
      <ProfileImage
        hasBorderAsset
        size={160}
        imgCode={131}
        alt="프로필 이미지"
        className="mx-auto"
      />
      <div className="text-24 text-primary font-hol text-center">
        {props.codeName}
      </div>
      <MakeCharacterStatContainer
        baseStats={props.baseStats}
        correctionStats={props.correctionStats}
        cardType={props.type}
      />
    </div>
  );
};

export default CharacterCard;
