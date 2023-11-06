export interface IStatObject {
  statType: string;
  statValue: number;
}

export interface ICharacterCard {
  type: string;
  gender?: number;
  characterCode: string;
  codeName: string;
  baseStats: IStatObject[];
  correctionStats: IStatObject[];
  onNextLevel?: () => void;
  onSetCharacter?: (gender: number, select: string) => void;
}

export interface ISwitchGender {
  gender: number;
  onClickEvent: () => void;
}

export interface ICharacterResponse {
  codeName: string;
  stats: IStatObject[];
}
