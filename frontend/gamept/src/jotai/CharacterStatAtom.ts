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
import { IPlayerStatusResponse } from '@/types/components/MakeGameProcess.type';
import { ICharacterStatusAtom } from '@/types/components/CharacterStatAtom.types';

export const characterStatusAtom = atom<ICharacterStatusAtom>({
  nickname: '',
  race: '',
  job: '',
  imgCode: '',
  hp: 0,
  exp: 0,
  statList: [],
  skillList: [],
  itemList: [],
});

// 캐릭터 정보 가져오기 및 초기화
// 이거 백에서 받은 API 그대로 박고 여기서 조절하는 방향으로 가자
// 그러려면 타입 정의가 필요함.
export const initCharacterStatusAtom = atom(
  (get) => get(characterStatusAtom),
  (_get, set, status: IPlayerStatusResponse) => {
    set(characterStatusAtom, {
      nickname: status.nickname,
      race: status.race.name,
      job: status.job.name,
      imgCode: '',
      hp: status.hp,
      exp: status.exp,
      statList: status.statList.map<{ statName: string; statValue: number }>(
        (element) => ({ statName: element.name, statValue: element.value })
      ),
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

// 캐릭터 스탯 가져오기 및 스탯 수정
export const statControlAtom = atom(
  (get) => get(characterStatusAtom).statList,
  (get, set, changedStat: Array<{ statName: string; statValue: number }>) => {
    set(characterStatusAtom, {
      ...get(characterStatusAtom),
      statList: changedStat.map<{ statName: string; statValue: number }>(
        (element) => ({ ...element })
      ),
    });
  }
);

// 스킬 가져오기 필요시 구현

// 아이템 set 및 get은 추후 구현 (사용안할수도 있으니)
