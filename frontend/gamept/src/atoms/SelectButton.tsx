import { ISelectButton } from '@/types/components/Button.types';

const SelectButton = (props: ISelectButton) => {
  const buttonStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div className="button-area" style={buttonStyle}>
      <button
        className={`w-full h-full text-center font-bold bg-secondaryContainer bg-button-background hover:bg-secondaryHover text-24`}
        onClick={props.onClickEvent}
      >
        {props.text}
      </button>
    </div>
  );
};

export default SelectButton;
