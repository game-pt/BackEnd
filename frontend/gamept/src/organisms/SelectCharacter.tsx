/**
 * 종족/ 직업 선택 컴포넌트 구현
 * @praams props
 * type: string : 종족/직업 구분
 * apiURL : string : 종족/직업에 따라 전송할 api 경로
 * playerStats? : Array<{statType: string; statValue: number}> : 직업 선택시 전송할 기본 스탯
 * @returns void
 */

import { ISelectCharacter } from '@/types/components/MakeGameProcess.type';
// import {
//   // ICharacterCard,
//   ICharacterResponse,
//   // IStatObject,
// } from '@/types/components/CharacterCard.types';
// import { useEffect, useState } from 'react';
import CharacterCard from './CharacterCard';

const SelectCharacter = (props: ISelectCharacter) => {
  // const [select, setSelect] = useState()
  // const [characterList, setCharacterList] = useState<ICharacterCard[]>([]);

  // useEffect(() => {
  //   // API 받아오기
  //   // const getCharacterListResponse = getCharacterList(props.apiURL);

  //   // 더미 데이터
  //   // const getCharacterListResponse = getClassList;

  //   // 받아온 response를 type에 맞게 변경 후 setList
  //   if (props.type === '종족') {
  //     setCharacterList(changeResponseToRace(getRaceList));
  //   } else {
  //     setCharacterList(
  //       changeResponseToClass(
  //         getClassList,
  //         props.playerStats ?? [
  // { statType: '힘', statValue: 10 },
  // { statType: '민첩', statValue: 10 },
  // { statType: '지능', statValue: 10 },
  // { statType: '행운', statValue: 10 },
  // { statType: '매력', statValue: 10 },
  //         ]
  //       )
  //     );
  //   }
  // }, [props.type]);
  console.log(props.data, '데이타 화긴');
  const statList = props.statList ?? [
    { statName: '건강', statValue: 10 },
    { statName: '힘', statValue: 10 },
    { statName: '민첩', statValue: 10 },
    { statName: '지능', statValue: 10 },
    { statName: '행운', statValue: 10 },
    { statName: '매력', statValue: 10 },
  ];

  return (
    <div>
      <div className="text-primary text-32 font-hol pt-[100px] text-center">
        당신의 {props.type}은 무엇입니까?
      </div>
      <div className="flex flex-row gap-10 justify-center my-[30px]">
        {props.data.map((character) => (
          <CharacterCard
            baseStats={props.type === '종족' ? character.statList : statList}
            code={character.code}
            correctionStats={character.bonusList}
            onSetCharacter={props.onSetCharacter}
            type={props.type}
            name={character.name}
            gender={props.gender}
            onNextLevel={props.onNextLevel}
            raceCode={props.raceCode}
          />
        ))}
      </div>
      <div className="text-primary text-28 font-hol text-center">
        {props.type}에 따라 {props.type === '종족' ? '기본' : '추가'} 스탯이
        결정됩니다.
      </div>
    </div>
  );
};

// const changeResponseToRace = (input: ICharacterResponse[]) => {
//   return input.map((characterInfo) => ({
//     characterCode: 'ABC-001',
//     type: 'race',
//     codeName: characterInfo.codeName,
//     baseStats: characterInfo.stats.map((statObj) => ({ ...statObj })),
//     correctionStats: characterInfo.stats.map((statObj) => ({
//       ...statObj,
//       statValue: 0,
//     })),
//   }));
// };

// const changeResponseToClass = (
//   input: ICharacterResponse[],
//   baseStats: IStatObject[]
// ) => {
//   return input.map((characterInfo) => ({
//     characterCode: 'ABC-001',
//     type: 'class',
//     codeName: characterInfo.codeName,
//     baseStats: [...baseStats],
//     correctionStats: characterInfo.stats.map((statObj) => ({
//       ...statObj,
//     })),
//   }));
// };

// const getRaceList: ICharacterResponse[] = [
//   {
//     codeName: '인간',
//     stats: [
//       {
//         statType: '힘',
//         statValue: 10,
//       },
//       {
//         statType: '민첩',
//         statValue: 10,
//       },
//       {
//         statType: '지혜',
//         statValue: 10,
//       },
//       {
//         statType: '행운',
//         statValue: 10,
//       },
//       {
//         statType: '매력',
//         statValue: 10,
//       },
//     ],
//   },
// ];

// getRaceList.push(getRaceList[0]);
// getRaceList.push(getRaceList[0]);
// getRaceList.push(getRaceList[0]);

// const getClassList: ICharacterResponse[] = [
//   {
//     codeName: '전사',
//     stats: [
//       {
//         statType: '힘',
//         statValue: 5,
//       },
//       {
//         statType: '민첩',
//         statValue: -2,
//       },
//       {
//         statType: '지혜',
//         statValue: -5,
//       },
//       {
//         statType: '행운',
//         statValue: 3,
//       },
//       {
//         statType: '매력',
//         statValue: 4,
//       },
//     ],
//   },
// ];

// getClassList.push(getClassList[0]);
// getClassList.push(getClassList[0]);
// getClassList.push(getClassList[0]);

export default SelectCharacter;
