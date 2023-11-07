export interface IProcessLevel {}

export interface ISelectGameMode {
  onGoSelectStory: () => void;
}

export interface ISelectGameStory {
  onGoMakeCharacter: () => void;
}

export interface ISelectCharacter extends IProcessLevel {
  type: string;
  apiURL: string;
  gender: number;
  onNextLevel: () => void;
  onPreviosLevel: () => void;
  onSetCharacter: (gender: number, select: string) => void;
  playerStats?: Array<{ statType: string; statValue: number }>;
}

export interface ICharacterStatus {
  race: string;
  className: string;
  gender: number;
  name: string;
}
