import { atom, useAtomValue, useSetAtom } from 'jotai';

const promptAtom = atom([['']]);

const updatePromptAtom = atom([['']], (get, set, update: string[]) => {
  const currentPrompt = get(promptAtom);
  const updatedPrompt = [...currentPrompt, update];
  set(promptAtom, updatedPrompt);
});

export const useUpdatePromptAtom = () => useSetAtom(updatePromptAtom);

export const usePromptAtom = () => useAtomValue(promptAtom);
