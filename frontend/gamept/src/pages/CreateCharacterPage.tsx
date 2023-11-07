/**
 *
 * @returns
 */

import Logo from '@/atoms/Logo';
import SelectCharacter from '@/organisms/SelectCharacter';
import MakeCharacterName from '@/organisms/MakeCharacterName';
import { useState } from 'react';
import { ICharacterStatus } from '@/types/components/MakeGameProcess.type';
import { useNavigate } from 'react-router-dom';
// import { useAtom } from "jotai"

const CreateCharacterPage = () => {
  const [characterStatus, setCharacterStatus] = useState<ICharacterStatus>({
    race: '',
    className: '',
    gender: 0,
    name: '',
  });

  const [processLevel, setProcessLevel] = useState(0);
  const navigate = useNavigate();
  const handleNextLevel = () => setProcessLevel(processLevel + 1);
  const handlePreviousLevel = () => setProcessLevel(processLevel - 1);
  const handleRaceSelect = (gender: number, race: string) => {
    setCharacterStatus({ ...characterStatus, gender, race });
  };
  const handleClassSelect = (gender: number, className: string) => {
    setCharacterStatus({ ...characterStatus, gender, className });
  };
  const createCharacterProcess = [
    <SelectCharacter
      apiURL={gameID}
      type="종족"
      onNextLevel={handleNextLevel}
      gender={characterStatus.gender}
      onSetCharacter={handleRaceSelect}
      onPreviosLevel={() => navigate('/createGame')}
    />,
    <SelectCharacter
      apiURL={gameID}
      type="직업"
      onNextLevel={handleNextLevel}
      gender={characterStatus.gender}
      onPreviosLevel={handlePreviousLevel}
      onSetCharacter={handleClassSelect}
    />,
    <MakeCharacterName
      characterStatus={characterStatus}
      setCharacterName={setCharacterStatus}
    />,
  ];
  return (
    <div className="bg-backgroundDeep w-screen h-screen">
      <Logo />
      {createCharacterProcess[processLevel]}
    </div>
  );
};

const gameID = '';
export default CreateCharacterPage;
