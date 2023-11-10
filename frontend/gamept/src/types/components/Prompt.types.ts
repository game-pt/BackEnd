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
  event: IActsType[] | null;
  sendEventHandler?: (e: IActsType) => void;
  sendPromptHandler?: (text: string) => void;
}

export interface IChoiceGroup {
  gameType: string;
  data?: IActsType[] | null; 
  onClickEvent?: (e: IActsType) => void;
}

export interface IGetPromptType {
  gameCode: string;
  prompt: string;
  event: {
    eventCode: string;
    eventName: string;
    acts: [
      {
        actCode: string;
        actName: string;
        subtask: string;
      },
    ];
  };
}

export interface IActsType {
  actCode: string;
  actName: string;
  subtask: string | null;
}