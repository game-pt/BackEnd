export interface IInput {
  width: string;
  height: string;
  placeholder: string;
  value: string;
  setValue: (value: string) => void;
  onClickEvent: () => void;
}