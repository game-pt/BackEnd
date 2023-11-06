import IngameButton from '@/atoms/IngameButton';
import { IChoiceGroup } from '@/types/components/Prompt.types';

const ChoiceGroup = (props: IChoiceGroup) => {
  const dummydata = ['1번 선택지', '2번 선택지', '3번 선택지'];

  const sendEvent = () => {
    if (props.onClickEvent) props.onClickEvent();
  }

  return (
    <div className="w-3/4 h-3/4 flex py-4 flex-wrap gap-3 my-auto justify-center overflow-y-scroll items-center text-black">
      {dummydata.map((e, i) => (
        <IngameButton
          key={`${i}_choice_button`}
          width="49%"
          height="fit-content"
          type="multi"
          text={e}
          onClickEvent={() => sendEvent()}
        />
      ))}
    </div>
  );
};

export default ChoiceGroup;
