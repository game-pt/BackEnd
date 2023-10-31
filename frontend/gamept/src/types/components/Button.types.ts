export interface IIngameButton {
  width: string;
  height: string;
  type: string;
  text: string;
  onClickEvent: () => void;
}

export interface ITextButton {
  text: string;
  onClickEvent: () => void;
}