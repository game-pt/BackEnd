/**
 * 일반 버튼 공통 컴포넌트, 프로필 이미지 연동 불가능, 텍스트 가운데 정렬
 * @prarams props
 * width: string;
 * height: string;
 * text: string;
 * isShadow?: boolean;
 * onClickEvent: () => void;
 * @returns void
 */

import { ISelectButton } from '@/types/components/Button.types';

const SelectButton = (props: ISelectButton) => {
  const buttonStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div
      className={`button-area ${props.isShadow ? 'shadow-lg' : ''}`}
      style={buttonStyle}
    >
      <button
        className="gamept-button text-center"
        onClick={props.onClickEvent}
      >
        {props.text}
      </button>
    </div>
  );
};

export default SelectButton;
