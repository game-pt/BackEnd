import { usePromptAtom, useUpdatePromptAtom } from "@/jotai/PromptAtom";
import { useCallback, useMemo } from "react";
import { useIndexedDB } from "react-indexed-db-hook";

const usePrompt = () => {
  const db = useIndexedDB('prompt');
  const getPrompt = useMemo(async () => {
    const getAtom = usePromptAtom();
    const setAtom = useUpdatePromptAtom();
    // 초기값 상태라면 => 초기화되었다면 => 새로고침했거나 처음
    if (getAtom[0][0] === '') {
      const get = (await db.getAll()).map(e => `${e}`);
      // Index DB에도 데이터가 없다면 처음
      if (get.length === 0) return [['']];
      // 있다면 Index DB 데이터를 set해주기
      else setAtom(get);

      return usePromptAtom();
    } else return getAtom;
  }, [db]);
  const setPrompt = useCallback((update: string[]) => {
    const setter = useUpdatePromptAtom();
    db.add({ content: update });
    setter(update);
  }, [db]);

  return [getPrompt, setPrompt];
}

export default usePrompt;