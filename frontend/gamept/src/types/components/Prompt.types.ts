// Propmt Atoms Props Type
export interface IPropmpt {
  type: string;
  data: string | null;
}

export interface IPromptInterface {
  sendHandler?: (roomId: string, playerName: string, inputMessage: string) => void;
}