import { IIngameButton } from '@/types/components/Button.types';

/**
 * @param props
 *  width: string;
 *  height: string;
 *  type: string;
 *  text: string;
 *  onClickEvent: () => void;
 *
 * @returns void;
 */
const IngameButton = (props: IIngameButton) => {
  const buttonStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div className="button-area" style={buttonStyle}>
      <button
        className="gamept-button text-left"
        onClick={props.onClickEvent}
      >
        {props.text}
        {props.type === 'multi' && (
          <div className="flex">
            <div className="basis-1/2">{props.text}</div>
            /** 투표한 플레이어가 있다면 프로필 이미지 띄워주기. */
            <div className="logo"></div>
          </div>
        )}
      </button>
    </div>
  );
};

export default IngameButton;
