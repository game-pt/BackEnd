import { atom, useAtomValue, useSetAtom } from 'jotai';

const promptAtom = atom([['']]);

const updatePromptAtom = atom(
  (get) => get(promptAtom),
  (get, set, update: string[]) => {
    if (update.length == 0) {
      return;
    }
    const currentPrompt = get(promptAtom);
    const updatedPrompt =
      currentPrompt[0][0] === '' && currentPrompt.length === 1
        ? [update]
        : [...currentPrompt, update];
    set(promptAtom, updatedPrompt);
  }
);

export const useUpdatePromptAtom = () => useSetAtom(updatePromptAtom);

export const usePromptAtom = () => useAtomValue(promptAtom);
