import { usePromptAtom, useUpdatePromptAtom } from "@/jotai/PromptAtom";
import { useCallback, useEffect, useState } from "react";
import { useIndexedDB } from "react-indexed-db-hook";

const usePrompt: () => [string[], (update: string[]) => void] = () => {
  const db = useIndexedDB('prompt');
  const getAtom = usePromptAtom();
  const setAtom = useUpdatePromptAtom();
  const [promptData, setPromptData] = useState<string[]>(getAtom[getAtom.length - 1]);

  const initializePrompt = useCallback(async () => {
    // IndexedDB에서 데이터 가져오기
    const dataFromDB = (await db.getAll()).map(e => e.content);
    if (dataFromDB.length === 0) {
      // 데이터가 없으면 초기화
      setAtom([]);
      setPromptData([]);
    } else {
      console.log(dataFromDB);
      // 데이터가 있으면 상태 업데이트
      setAtom(dataFromDB);
      setPromptData(dataFromDB);
    }
  }, [db, setAtom]);

  useEffect(() => {
    if (getAtom[0][0] === '') {
      // 초기 상태인 경우
      initializePrompt();
    }
    setPromptData(getAtom[getAtom.length - 1]);
  }, [initializePrompt]);

  const setPrompt = useCallback((update: string[]) => {
    db.add({ content: update });
    setAtom(update);
  }, [db, setAtom]);

  return [promptData, setPrompt];
}

export default usePrompt;
