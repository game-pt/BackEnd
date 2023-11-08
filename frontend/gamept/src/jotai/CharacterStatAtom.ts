/**
 * 생성한 캐릭터의 스탯을 관리하는 atom
 * 싱글 플레이 기준으로 관리, 멀티 플레이가 개발된다면 atom을 객체 배열로 변경
 * name : 플레이어 이름
 * race : 종족
 * job : 직업
 * imgCode : 캐릭터 코드 - 프로필 이미지 불러오는 용도
 * stat : 스탯 배열
 */

import { atom } from 'jotai';
import { ICharacterStatus } from '@/types/components/CharacterStatus.type';

export const characterStatusAtom = atom({
  nickname: '',
  race: '',
  job: '',
  imgCode: '',
  stat: {},
});

export const characterSkillAtom = atom<Array<{ name: string; desc: string }>>(
  []
);

//Array<{ name: string; desc: string }>
// 캐릭터 정보 갱신할 경우 사용, 변경된 스탯으로 이루어진 배열을 매개변수로 전달
export const setCharacterStatusAtom = atom(
  (get) => get(characterStatusAtom),
  (_get, set, changedStat: Record<string, number>[]) => {
    set(characterStatusAtom, (prev) => ({
      ...prev,
      stat: { ...prev.stat, ...changedStat },
    }));
  }
);

// 캐릭터 생성 시 사용
export const initCharacterStatusAtom = atom(
  (get) => get(characterStatusAtom),
  (_get, set, status: ICharacterStatus) => {
    set(characterStatusAtom, () => ({
      ...status,
    }));
  }
);

// 스킬 가져오기
export const getSkillAtom = atom((get) => get(characterSkillAtom));

// 스킬 init
export const setSkillAtom = atom(
  null,
  (_get, set, skills: Array<{ name: string; desc: string }>) => {
    set(
      characterSkillAtom,
      skills.map((element) => ({ ...element }))
    );
  }
);
