export interface ICardImage {
  url: string;
  alt: string;
}

export interface IProfileImage {
  onClickEvent?: any;
  hasBorderAsset?: boolean;
  size: number;
  imgCode: number;
  alt: string;
  className?: string;
}
