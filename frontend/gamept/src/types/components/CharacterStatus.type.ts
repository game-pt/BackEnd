export interface ICharacterStatus {
  name: string;
  race: string;
  job: string;
  code: string;
  stat: Record<string, number>[];
}
