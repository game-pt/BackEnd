// Propmt Atoms Props Type
export interface IPropmpt {
  type: string;
  data: string | null;
}

export interface IPromptInterface {
  sendEventHandler?: () => void;
}

export interface IChoiceGroup {
  onClickEvent?: () => void;
}