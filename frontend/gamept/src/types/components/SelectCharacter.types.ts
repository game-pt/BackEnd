export interface ISelectCharacter {
  type: string;
  apiURL: string;
  playerStats?: Array<{ statType: string; statValue: number }>;
}
