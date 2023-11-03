/**
 * 게임 생성 페이지
 * isSelectStory 값에 따라 싱글/멀티 선택 or 스토리 선택 단계 표현
 * @params props
 * @returns void
 */

import Logo from '@/atoms/Logo';
import SelectGameMode from '@/organisms/SelectGameMode';
import SelectGameStory from '@/organisms/SelectGameStory';
import { useState } from 'react';
const CreateGamePage = () => {
  const [isSelectStory, setIsSelectStory] = useState(false);

  const goSelectStory = () => {
    setIsSelectStory(true);
  };
  console.log(setIsSelectStory);

  return (
    <div className="w-full h-full">
      <Logo />
      {!isSelectStory && <SelectGameMode onGoSelectStory={goSelectStory} />}
      {isSelectStory && <SelectGameStory />}
    </div>
  );
};

export default CreateGamePage;
