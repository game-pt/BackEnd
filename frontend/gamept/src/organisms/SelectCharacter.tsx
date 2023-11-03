/**
 *
 */

import { ISelectCharacter } from '@/types/components/SelectCharacter.types';
import {
  ICharacterCard,
  ICharacterResponse,
  IStatObject,
} from '@/types/components/CharacterCard.types';
import { useEffect, useState } from 'react';
import CharacterCard from './CharacterCard';

const SelectCharacter = (props: ISelectCharacter) => {
  // const [select, setSelect] = useState()
  const [characterList, setCharacterList] = useState<ICharacterCard[]>([]);

  useEffect(() => {
    // API 받아오기
    // const getCharacterListResponse = getCharacterList(props.apiURL);

    // 더미 데이터
    const getCharacterListResponse = getRaceList;

    // 받아온 response를 type에 맞게 변경 후 setList
    if (props.type === '종족') {
      setCharacterList(changeResponseToRace(getCharacterListResponse));
    } else {
      setCharacterList(
        changeResponseToClass(
          getCharacterListResponse,
          props.playerStats ?? [
            { statType: '힘', statValue: 10 },
            { statType: '민첩', statValue: 10 },
            { statType: '지능', statValue: 10 },
            { statType: '행운', statValue: 10 },
            { statType: '매력', statValue: 10 },
          ]
        )
      );
    }
  }, []);
  console.log('문제가뭘까', characterList);

  return (
    <div>
      <div className="text-primary text-32 font-hol pt-[100px]">
        당신의 {props.type}은 무엇입니까?
      </div>
      <div className="flex flex-row gap-10 justify-center my-[30px]">
        {characterList &&
          characterList.map((card) => (
            <CharacterCard
              baseStats={card.baseStats}
              characterCode={card.characterCode}
              correctionStats={card.correctionStats}
              onClickEvent={() => {}}
              type={card.type}
              typeName={card.typeName}
            />
          ))}
      </div>
      <div className="text-primary text-28 font-hol">
        {props.type}에 따라 {props.type === '종족' ? '기본' : '추가'} 스탯이
        결정됩니다.
      </div>
    </div>
  );
};

const changeResponseToRace = (input: ICharacterResponse[]) => {
  return input.map((characterInfo) => ({
    characterCode: 0,
    type: 0,
    typeName: characterInfo.typeName,
    baseStats: characterInfo.stats.map((statObj) => ({ ...statObj })),
    correctionStats: characterInfo.stats.map((statObj) => ({
      ...statObj,
      statValue: 0,
    })),
    onClickEvent: () => {},
  }));
};

const changeResponseToClass = (
  input: ICharacterResponse[],
  baseStats: IStatObject[]
) => {
  return input.map((characterInfo) => ({
    characterCode: 0,
    type: 0,
    typeName: characterInfo.typeName,
    baseStats: [...baseStats],
    correctionStats: characterInfo.stats.map((statObj) => ({
      ...statObj,
    })),
    onClickEvent: () => {},
  }));
};

const getRaceList: ICharacterResponse[] = [
  {
    typeName: '인간',
    stats: [
      {
        statType: '힘',
        statValue: 10,
      },
      {
        statType: '민첩',
        statValue: 10,
      },
      {
        statType: '지혜',
        statValue: 10,
      },
      {
        statType: '행운',
        statValue: 10,
      },
      {
        statType: '매력',
        statValue: 10,
      },
    ],
  },
];

getRaceList.push(getRaceList[0]);
getRaceList.push(getRaceList[0]);
getRaceList.push(getRaceList[0]);

const getClassList: ICharacterResponse[] = [
  {
    typeName: '전사',
    stats: [
      {
        statType: '힘',
        statValue: 5,
      },
      {
        statType: '민첩',
        statValue: -2,
      },
      {
        statType: '지혜',
        statValue: -5,
      },
      {
        statType: '행운',
        statValue: 3,
      },
      {
        statType: '매력',
        statValue: 4,
      },
    ],
  },
];

getClassList.push(getClassList[0]);
getClassList.push(getClassList[0]);
getClassList.push(getClassList[0]);

export default SelectCharacter;
