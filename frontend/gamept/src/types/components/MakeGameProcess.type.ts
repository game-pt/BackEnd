export interface IProcessLevel {}

export interface ISelectGameMode {
  onGoSelectStory: () => void;
}

export interface ISelectGameStory {}

export interface ISelectCharacter extends IProcessLevel {
  type: string;
  apiURL: string;
  playerStats?: Array<{ statType: string; statValue: number }>;
}
