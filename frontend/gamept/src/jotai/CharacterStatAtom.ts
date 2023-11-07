/**
 * 생성한 캐릭터의 스탯을 관리하는 atom
 * 싱글 플레이 기준으로 관리, 멀티 플레이가 개발된다면 atom을 객체 배열로 변경
 * name : 플레이어 이름
 * race : 종족
 * job : 직업
 * code : 캐릭터 코드 - 프로필 이미지 불러오는 용도
 * stat : 스탯 배열
 */

import { atom } from 'jotai';

export const characterStatusAtom = atom({
  name: '',
  race: '',
  job: '',
  code: '',
  stat: {},
});

export const selectCharacterStatusAtom = atom(
  (get) => get(characterStatusAtom),
  (_get, set, changedStat: Record<string, number>[]) => {
    set(characterStatusAtom, (prev) => ({
      ...prev,
      stat: { ...prev.stat, ...changedStat },
    }));
  }
);
