import { atom, useAtomValue, useSetAtom } from 'jotai';
import { atomWithDefault } from 'jotai/utils';
import { useIndexedDB } from 'react-indexed-db-hook';

const initialPrompt = async () => {
  const db = useIndexedDB('prompt');
  const get = await db.getAll();
  return get;
};

const getPrompt = async (update: string[]) => {
  const db = useIndexedDB('prompt');
  await db.add({ content: update });
  const get = await db.getAll();
  return get;
};

const promptAtom = atomWithDefault(initialPrompt);

const updatePromptAtom = atom(null, async (_get, set, update: string[]) => {
  set(promptAtom, getPrompt(update));
});

export const useUpdatePrompt = () => useSetAtom(updatePromptAtom);

export const usePrompt = () => useAtomValue(promptAtom);
