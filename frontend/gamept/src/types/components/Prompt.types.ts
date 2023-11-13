// Propmt Atoms Props Type
export interface IPropmpt {
  type: string;
  data: IPromptHistory[][] | null;
  isFetching: boolean;
}

export interface IPromptHistory {
  msg: string;
  mine: boolean;
}

export interface IPromptInterface {
  gameType: string;
  isFetching: boolean;
  event: IEventType | null;
  sendEventHandler?: (e: IActsType) => void;
  sendPromptHandler?: (text: string) => void;
}

export interface IChoiceGroup {
  gameType: string;
  data?: IActsType[] | null; 
  onClickEvent?: (e: IActsType) => void;
}

export interface IMonsterType {
  code: string;
  monsterLevel: number;
  monsterAttack: number;
}

export interface IGetPromptType {
  gameCode: string;
  prompt: string;
  itemYn: string | null;
  monster: string | null;
  event: IEventType;
}

export interface ISubtaskType {
  code: string;
  name: string;
  desc: string;
}

export interface IActsType {
  actCode: string;
  actName: string;
  subtask: string;
}

export interface IEventType {
  eventCode: string;
  eventName: string;
  acts: IActsType[] | null;
}

export interface ISendChoiceType {
  actCode: string;
  playerCode: string;
  gmonsterCode?: string;
  subtask?: string;
}