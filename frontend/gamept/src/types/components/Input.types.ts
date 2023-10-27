export interface IInput {
  width: string;
  height: string;
  placeholder: string;
  setValue: (value: string) => void;
  onClickEvent: () => void;
}