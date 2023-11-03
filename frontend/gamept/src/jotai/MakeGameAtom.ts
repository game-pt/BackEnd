/**
 * 싱글/멀티 여부 및 스토리 종류 관리하는 atom
 * gameModeAtom : 싱글/멀티 여부를 boolean으로 관리
 *  true : single / muilty : false
 * gameStoryAtom : 스토리 종류 관리하는 atom
 *  문자열 코드로 관리?
 */

import { atom } from 'jotai';

export const gameModeAtom = atom(0);
export const gameStoryAtom = atom('fantasy');

export const selectGameModeAtom = atom(
  (get) => get(gameModeAtom),
  (_get, set, selectedMode: number) => {
    set(gameModeAtom, () => selectedMode);
  }
);

export const selectGameStoryAtom = atom(
  (get) => get(gameStoryAtom),
  (_get, set, selectedStory: string) => {
    set(gameStoryAtom, () => selectedStory);
  }
);
