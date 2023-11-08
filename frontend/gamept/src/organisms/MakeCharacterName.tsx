/*
 * 캐릭터 이름 설정 인풋 창
 * 상위 컴포넌트에서 name과 setName을 props내려서 관리
 * 글자 수가 12Byte를 넘을 경우 (한글 2, 영어 1) 제한 및 안내 문구 출력
 */

import { IMakeCharacterName } from '@/types/components/MakeCharacterName.type';
import { ChangeEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import { initCharacterStatusAtom } from '@/jotai/CharacterStatAtom';
// import { useAtom } from 'jotai';
import SelectButton from '@/atoms/SelectButton';

const MakeCharacterName = (props: IMakeCharacterName) => {
  const [isInputValid, setIsInputValid] = useState(InputStatement.VALID);
  // const [, setCharacterStatus] = useAtom(initCharacterStatusAtom);
  const navigate = useNavigate();

  // 시작하기 버튼 눌렀을 경우
  const handleSubmitInput = () => {
    if (!isNameOK(props.characterStatus.nickname)) {
      // 올바르지 않은 네임입니다.
      return;
    }

    // atom 갱신
    // setCharacterStatus(props.characterStatus);

    props.onNextLevel();
  };

  const handleEnter = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter') handleSubmitInput();
  };

  const handleChangeInput = (e: ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;

    if (isByteExceed(input)) {
      setIsInputValid(InputStatement.EXCEED);
      return;
    }

    setIsInputValid(InputStatement.VALID);
    props.setCharacterName((prev) => ({
      ...prev,
      nickname: e.target.value,
    }));
  };

  return (
    <div className="w-[60vw] min-w-[400px] h-full m-auto p-[100px] flex flex-col justify-center items-center">
      <div className="font-hol text-32 text-primary mb-20 text-center ">
        당신의 이름은 무엇입니까?
      </div>
      <input
        className="bg-containerLight rounded-[10px] text-center w-full h-[75px] placeholder:text-gray-300 outline-none px-4 text-white font-hol text-32"
        placeholder="이름을 입력해주세요."
        type="text"
        onChange={handleChangeInput}
        value={props.characterStatus.nickname}
        onKeyDown={handleEnter}
      />
      <div
        className={`font-hol text-white text-20 my-5 ${
          isInputValid !== InputStatement.VALID ? '' : 'invisible'
        }`}
      >
        {isInputValid === InputStatement.EXCEED &&
          '최대 글자 수를 초과했습니다.'}
        {isInputValid === InputStatement.BADINPUT &&
          '올바르지 않은 입력입니다.'}
        {isInputValid === InputStatement.VALID && '칸채우기'}
      </div>
      {props.characterStatus.nickname.length > 0 && (
        <SelectButton
          height="75px"
          onClickEvent={handleSubmitInput}
          text="시작하기"
          width="350px"
          disabled={isInputValid === InputStatement.VALID ? undefined : true}
        />
      )}
    </div>
  );
};

const isByteExceed = (input: string) => {
  const length = input.length;
  const MAX_BYTE = 12;
  let size = 0;

  for (let i = 0; i < length; i++) {
    size += input.charCodeAt(i) >> 7 ? 2 : 1; // 아스키 코드 범위 벗어나면 2바이트 취급
    if (size > MAX_BYTE) return true;
  }
  return false;
};

const isNameOK = (input: string) => {
  // 욕 검사?
  input;
  return true;
};

const enum InputStatement {
  VALID,
  EXCEED,
  BADINPUT,
}

export default MakeCharacterName;
