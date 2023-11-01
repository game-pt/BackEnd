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
        className="w-full h-full font-bold bg-secondaryContainer bg-button-background hover:bg-secondaryHover text-24 text-left"
        onClick={props.onClickEvent}
      >
        {props.text}
        {props.type === 'multi' && (
          /** 투표한 플레이어가 있다면 프로필 이미지 띄워주기. */
          <div className="logo"></div>
        )}
      </button>
    </div>
  );
};

export default IngameButton;