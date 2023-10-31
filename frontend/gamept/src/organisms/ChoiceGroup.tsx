import IngameButton from '@/atoms/IngameButton';

const ChoiceGroup = () => {
  const dummydata = ['1번 선택지', '2번 선택지', '3번 선택지', '4번 선택지'];

  return (
    <div className="w-3/4 flex flex-wrap gap-3 my-auto">
      {dummydata.map((e, i) => (
        <IngameButton
          width="49%"
          height="60px"
          type="single"
          text={e}
          onClickEvent={() => console.log(i + '번 선택!!')}
        />
      ))}
    </div>
  );
};

export default ChoiceGroup;
