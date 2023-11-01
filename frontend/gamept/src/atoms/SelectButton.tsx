import { ISelectButton } from '@/types/components/Button.types';

const SelectButton = (props: ISelectButton) => {
  const buttonStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div className="button-area" style={buttonStyle}>
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
