/*
 * 캐릭터 이름 설정 인풋 창
 * 상위 컴포넌트에서 name과 setName을 props내려서 관리
 * 글자 수가 12Byte를 넘을 경우 (한글 2, 영어 1) 제한 및 안내 문구 출력
 */

import { IMakeCharacterName } from '@/types/components/MakeCharacterName.type';
import { ChangeEvent, useState } from 'react';

const MakeCharacterName = (props: IMakeCharacterName) => {
  const [isInputExceed, setIsInputExceed] = useState(false);

  const handleChangeInput = (e: ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;

    if (isByteExceed(input)) {
      setIsInputExceed(true);
      return;
    }

    setIsInputExceed(false);
    props.setCharacterName(e.target.value);
  };

  return (
    <div className="w-[60vw] min-w-[400px]">
      <div className="font-hol text-32 text-primary mb-[30px]">
        당신의 이름은 무엇입니까?
      </div>
      <input
        className="bg-containerLight rounded-[10px] text-center w-full h-[75px] placeholder:text-gray-300 outline-none px-4 text-white font-hol text-32"
        placeholder="이름을 입력해주세요."
        type="text"
        onChange={handleChangeInput}
        value={props.characterName}
      />
      {isInputExceed && (
        <div className="font-hol text-white text-18 mt-2">
          최대 글자 수를 초과했습니다.
        </div>
      )}
    </div>
  );
};

const isByteExceed = (input: string) => {
  const length = input.length;
  const MAX_BYTE = 12;
  let size = 0;

  for (let i = 0; i < length; i++) {
    size += input.charCodeAt(i) >> 7 ? 2 : 1;
    if (size > MAX_BYTE) return true;
  }
  return false;
};

export default MakeCharacterName;
