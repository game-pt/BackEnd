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
import { useNavigate } from 'react-router-dom';

const CreateGamePage = () => {
  const [isSelectStory, setIsSelectStory] = useState(false);
  const navigate = useNavigate();
  const goSelectStory = () => {
    setIsSelectStory(true);
  };

  const goMakeCharacter = () => {
    navigate('/createCharacter');
  };
  return (
    <div className="w-full h-full">
      <Logo />
      {!isSelectStory && <SelectGameMode onGoSelectStory={goSelectStory} />}
      {isSelectStory && <SelectGameStory onGoMakeCharacter={goMakeCharacter} />}
    </div>
  );
};

export default CreateGamePage;
