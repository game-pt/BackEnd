import { ISelectButton } from '@/types/components/Button.types';

const SelectButton = (props: ISelectButton) => {
  const buttonStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div className="button-area" style={buttonStyle}>
      <button
        className={`w-full h-full font-bold outline-transparent focus:outline-[2px] active:outline-black focus:outline-offset-[-2px] focus:outline focus:border-transparent focus:outline-secondary bg-secondaryContainer bg-button-background hover:bg-secondaryHover text-24 text-center `}
        onClick={props.onClickEvent}
      >
        {props.text}
      </button>
    </div>
  );
};

export default SelectButton;
