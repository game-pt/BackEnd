/**
 * 싱글/멀티 여부 및 스토리 종류 관리하는 atom
 * gameModeAtom : 싱글/멀티 여부를 boolean으로 관리
 *  true : single / muilty : false
 * gameStoryAtom : 스토리 종류 관리하는 atom
 *  문자열 코드로 관리?
 */

import { atom } from 'jotai';

export const gameModeAtom = atom(true);
export const gameStoryAtom = atom('fantasy');

export const selectGameModeAtom = atom(
  (get) => get(gameModeAtom),
  (get, set, selectedMode: boolean) => {
    get(gameModeAtom); // get 사용하지 않음 문제 해결용
    set(gameModeAtom, () => selectedMode);
  }
);

export const selectGameStoryAtom = atom(
  (get) => get(gameStoryAtom),
  (get, set, selectedStory: string) => {
    get(gameStoryAtom);
    set(gameStoryAtom, () => selectedStory);
  }
);
