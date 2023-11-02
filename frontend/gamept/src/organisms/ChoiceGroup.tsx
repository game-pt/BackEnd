import IngameButton from '@/atoms/IngameButton';

const ChoiceGroup = () => {
  const dummydata = ['1번 선택지 Hello World Hi There', '2번 선택지', '3번 선택지'];

  return (
    <div className="w-3/4 h-3/4 flex py-4 flex-wrap gap-3 my-auto justify-center overflow-y-scroll items-center text-black">
      {dummydata.map((e, i) => (
        <IngameButton
          key={`${i}_choice_button`}
          width="49%"
          height="fit-content"
          type="single"
          text={e}
          onClickEvent={() => console.log(i + '번 선택!!')}
        />
      ))}
    </div>
  );
};

export default ChoiceGroup;
