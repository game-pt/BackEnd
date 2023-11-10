/**
 * 생성한 캐릭터의 스탯을 관리하는 atom
 * 싱글 플레이 기준으로 관리, 멀티 플레이가 개발된다면 atom을 객체 배열로 변경
 * name : 플레이어 이름
 * race : 종족
 * job : 직업
 * imgCode : 캐릭터 코드 - 프로필 이미지 불러오는 용도
 * stat : 스탯 배열
 */

import { atom, useSetAtom, useAtomValue } from 'jotai';
import { IPlayerStatusResponse } from '@/types/components/MakeGameProcess.type';
import { ICharacterStatusAtom } from '@/types/components/CharacterStatAtom.types';
import { IProfileInterface } from '@/types/components/ProfileInterface.type';

export const characterStatusAtom = atom<ICharacterStatusAtom>({
  nickname: '',
  race: '',
  job: '',
  gender: '',
  imgCode: '',
  level: 0,
  hp: 0,
  exp: 0,
  statList: [],
  skillList: [],
  itemList: [],
});

// 캐릭터 생성 시 캐릭터 정보 atom 초기화
export const initCharacterStatusAtom = atom(
  (get) => get(characterStatusAtom),
  (_get, set, status: IPlayerStatusResponse) => {
    set(characterStatusAtom, {
      nickname: status.nickname,
      race: status.race.name,
      job: status.job.name,
      imgCode: '',
      hp: status.hp,
      level: status.level,
      exp: status.exp,
      statList: status.statList.map<{
        statName: string;
        statValue: number;
        code: string;
      }>((element) => ({
        statName: element.name,
        statValue: element.value,
        code: element.code,
      })),
      skillList: status.job.skillList.map<{ name: string; desc: string }>(
        (element) => ({ ...element })
      ),
      itemList: status.itemList.map<{
        code: string;
        name: string;
        desc: string;
        weight: number;
      }>((element) => ({ ...element })),
    });
  }
);

// 이미지코드 초기화
export const initExtra = atom(
  null,
  (_get, set, imgCode: string, gender: number) => {
    set(characterStatusAtom, (prev) => ({
      ...prev,
      imgCode,
      gender: gender === 0 ? '남성' : '여성',
    }));
  }
);

// get 캐릭터 스탯, set 캐릭터 스텟
export const statControlAtom = atom(
  (get) => get(characterStatusAtom).statList,
  (
    get,
    set,
    changedStat: Array<{ statName: string; statValue: number; code: string }>
  ) => {
    set(characterStatusAtom, {
      ...get(characterStatusAtom),
      statList: changedStat.map<{
        statName: string;
        statValue: number;
        code: string;
      }>((element) => ({ ...element })),
    });
  }
);

// useStatAtom : statList 가져오기
// useUpdateStatAtom(statList) : 넣은 statList 기준으로 스탯 갱신
export const useStatAtom = () => useAtomValue(statControlAtom);
export const useUpdateStatAtom = () => useSetAtom(statControlAtom);

// 프로필 인터페이스 반영 및 가져오기
export const profileInterfaceControlAtom = atom(
  (get) => {
    const status = get(characterStatusAtom);
    return {
      hp: status.hp,
      exp: status.exp,
      level: status.level,
    };
  },
  (get, set, profileChanged: IProfileInterface) => {
    set(characterStatusAtom, {
      ...get(characterStatusAtom),
      ...profileChanged,
    });
  }
);

export const useProfileAtom = () => useAtomValue(profileInterfaceControlAtom);
export const useUpdateProfileAtom = () =>
  useSetAtom(profileInterfaceControlAtom);

// 스킬 가져오기 필요시 구현

// 아이템 set 및 get은 추후 구현 (사용안할수도 있으니)
