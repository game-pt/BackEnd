import { IStatObject } from './CharacterCard.types';

export interface IMakeCharacterStat {
  baseStats: IStatObject[];
  correctionStats: IStatObject[];
}

export interface IStatText {
  statType: string;
  baseStat: number;
  correctionStat: number;
}
