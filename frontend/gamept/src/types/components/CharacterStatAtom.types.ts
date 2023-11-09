export interface ICharacterStatusAtom {
  nickname: string;
  race: string;
  job: string;
  imgCode: string;
  hp: number;
  exp: number;
  statList: Array<{ statName: string; statValue: number }>;
  skillList: Array<{ name: string; desc: string }>;
  itemList: Array<{ code: string; name: string; desc: string; weight: number }>;
}
