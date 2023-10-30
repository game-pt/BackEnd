export interface ISelectCharacter {
  type: number;
  characterCode: number;
  typeName: string;
  baseStats: number[];
  correctionStats: number[];
  onClickEvent: () => void;
}

export interface ISwitchGender {
  gender: number;
  onClickEvent: () => void;
}
