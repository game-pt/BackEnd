// Propmt Atoms Props Type
export interface IPropmpt {
  type: string;
  data: string[][] | null;
}

export interface IPromptInterface {
  gameType: string;
  sendEventHandler?: () => void;
  sendPromptHandler: (text: string) => void;
}

export interface IChoiceGroup {
  gameType: string;
  onClickEvent?: () => void;
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
