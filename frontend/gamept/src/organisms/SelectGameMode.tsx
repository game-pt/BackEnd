/**
 * 게임 모드 선택 카드 구현
 * @param props
 * modeName : string;
 * modeType: number;
 * imgUrl: string;
 * onClickEvent: () => void;
 *
 * @returns void;
 * 백 API reponse 구성에 따라 변경될 여지가 있음
 */

import CardImage from '@/atoms/CardImage';
import { ISelectGameMode } from '@/types/components/SelectGameMode.type';

const SelectGameMode = (props: ISelectGameMode) => {
  return (
    <div
      className=" w-[470px] h-[445px] bg-containerLight rounded-[10px]"
      onClick={props.onClickEvent}
    >
      <CardImage
        url={'../assets/' + props.imgUrl}
        alt={`${props.modeName} 이미지`}
      />
      <div className="leading-[130px] items-center h-[130px] font-hol text-28 text-primary shadow-lg">
        {props.modeName}
      </div>
    </div>
  );
};

export default SelectGameMode;
