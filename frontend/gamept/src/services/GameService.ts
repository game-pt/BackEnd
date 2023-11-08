import { useUpdatePromptAtom } from "@/jotai/PromptAtom";
import { IGetPromptType } from "@/types/components/Prompt.types";
import { useIndexedDB } from 'react-indexed-db-hook';

const getPromptData = async (response: IGetPromptType) => {
  const origin = {};
  const prompt = response.prompt.split("\n\n");

  const db = useIndexedDB('prompt');

  const get = await db.getAll();

  const updatePrompt = useUpdatePromptAtom();

  updatePrompt(get.map(e => `${e}`));

  return { origin, prompt };
};

export { getPromptData };
