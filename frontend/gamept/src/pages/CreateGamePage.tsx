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
import { useQuery } from 'react-query';
import { fetchGetStories } from '@/services/CreateGameService';

const CreateGamePage = () => {
  const [isSelectStory, setIsSelectStory] = useState(false);

  const { data } = useQuery({
    queryKey: ['getStories'],
    queryFn: () => fetchGetStories(),
  });

  const goSelectStory = () => {
    setIsSelectStory(true);
  };

  // if (isSuccess) {
  //   console.log(data);
  // }

  return (
    <div className="w-screen h-screen bg-backgroundDeep">
      <Logo />
      {!isSelectStory && <SelectGameMode onGoSelectStory={goSelectStory} />}
      {isSelectStory && <SelectGameStory stories={data} />}
    </div>
  );
};

export default CreateGamePage;
