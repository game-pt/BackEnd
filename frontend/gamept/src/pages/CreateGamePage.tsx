import Logo from '@/atoms/Logo';
import SelectGameMode from '@/organisms/SelectGameMode';
import SelectGameStory from '@/organisms/SelectGameStory';
import { useState } from 'react';
const CreateGamePage = () => {
  const [isSelectStory, setIsSelectStory] = useState(false);

  return (
    <div className="w-full h-full">
      <Logo />
      {!isSelectStory && <SelectGameMode />}
      {isSelectStory && <SelectGameStory />}
    </div>
  );
};

export default CreateGamePage;
