import { IInput } from '@/types/components/Input.types';

/**
 * @param props
 * {
 * width, height => 너비와 높이
 * placeholder => 플레이스홀더
 * setValue => 인풋에 입력된 텍스트 값 부모 컴포넌트에 세팅할 메서드
 * onClickEvent => 버튼 클릭 시 호출될 메서드
 * }
 */
const Input = (props: IInput) => {
  const inputStyle = {
    width: props.width,
    height: props.height,
  };

  return (
    <div
      className="min-w-400 min-h-[40px] mx-auto relative flex justify-end items-center"
      style={inputStyle}
    >
      <input
        placeholder={props.placeholder}
        className={`w-full h-full px-2 pr-7 bg-backgroud border-primary border-2 rounded-sm font-hol text-white text-18`}
        onChange={(e) => props.setValue(e.target.value)}
      />
      <img
        className="absolute h-1/2 mr-3 hover:scale-110 duration-normal"
        src="./src/assets/InputButton.svg"
        alt="Input_Button"
        onClick={props.onClickEvent}
      />
    </div>
  );
};

export default Input;
