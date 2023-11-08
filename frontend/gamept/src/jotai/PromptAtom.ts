import { atom, useAtomValue, useSetAtom } from 'jotai';
import { atomWithDefault } from 'jotai/utils';
// import { useIndexedDB } from 'react-indexed-db-hook';

// const initialPrompt = async () => {
//   const db = useIndexedDB('prompt');
//   const get = await db.getAll();
//   return get.map(e => `${e}`);
// };

// const getPrompt = async (update: string[]) => {
//   const db = useIndexedDB('prompt');
//   await db.add({ content: update });
//   const get = await db.getAll();
//   return get.map(e => `${e}`);
// };

const initialPrompt = () => [['']];

const promptAtom = atomWithDefault(initialPrompt);

const updatePromptAtom = atom(null, (get, set, update: string[]) => {
  const currentPrompt = get(promptAtom);
  const updatedPrompt = [...currentPrompt, update];
  set(promptAtom, updatedPrompt);
});

export const useUpdatePromptAtom = () => useSetAtom(updatePromptAtom);

export const usePromptAtom = () => useAtomValue(promptAtom);
