export interface IStatObject {
  statType: string;
  statValue: number;
}

export interface ICharacterCard {
  type: number;
  characterCode: number;
  typeName: string;
  baseStats: IStatObject[];
  correctionStats: IStatObject[];
  onClickEvent: () => void;
}

export interface ISwitchGender {
  gender: number;
  onClickEvent: () => void;
}

export interface ICharacterResponse {
  typeName: string;
  stats: IStatObject[];
}
