export interface IMultiPlayModal {
  setIsShowModal: React.Dispatch<React.SetStateAction<boolean>>;
  closeModal: () => void;
  isShowModal: boolean;
}
