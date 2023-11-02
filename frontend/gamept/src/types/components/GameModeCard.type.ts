// export interface IGameModeCard {
//   modeName: string;
//   modeType: number;
//   imgUrl: string;
//   onClickEvent: () => void;
// }

export interface IGameModeCardResponse {
  modeName: string;
  modeType: number;
  imgUrl: string;
}

export interface IGameModeCard extends IGameModeCardResponse {
  onClickEvent?: () => void;
}
