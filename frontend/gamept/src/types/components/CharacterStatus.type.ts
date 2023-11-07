export interface ICharacterStatus {
  nickname: string;
  race: string;
  job: string;
  imgCode: string;
  stat: Record<string, number>[];
}
