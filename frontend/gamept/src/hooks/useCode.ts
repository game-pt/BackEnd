import { useIndexedDB } from 'react-indexed-db-hook';
import { useAtom } from 'jotai';
import { useCallback, useEffect, useState } from 'react';
import { playerCodeAtom, gameCodeAtom } from '@/jotai/MakeGameAtom';

export const useGameCode: () => [string, (gameCode: string) => void] = () => {
  const db = useIndexedDB('codeStore');
  const [getGameCodeAtom, setGameCodeAtom] = useAtom(gameCodeAtom);
  // const [gameCode, setGCode] = useState(getGameCodeAtom);
  // atom 가져와서 반환
  // atom이 널이면 디비값 가져와서 atom 갱신 후 반환
  //

  useEffect(() => {
    const checkGameCode = async () => {
      if (getGameCodeAtom === '') {
        const dbData = (await db.getByID(1)) ?? '';
        setGameCodeAtom(dbData);
        setGameCode(dbData);
        return;
      }
      setGameCode(getGameCodeAtom);
    };
    checkGameCode();
  }, [getGameCodeAtom]);

  const setGameCode = useCallback(
    (gameCode: string) => {
      // 기존의 db에 저장된 값을 지우고 새로운 값 갱신, atom에도 갱신
      db.clear();
      db.add({ gameCode: gameCode });
      console.log('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', gameCode);
      setGameCodeAtom(gameCode);
    },
    [db, setGameCodeAtom]
  );
  return [getGameCodeAtom, setGameCode];
};
