import SelectButton from '@/atoms/SelectButton';
import Input from '@/atoms/Input';

const MultiplayModal = () => {
  return (
    <div className="font-hol w-[590px] border-[3px] pt-10 border-primary h-[470px] rounded-[10px] bg-backgroundDeep/80">
      <div className="flex flex-col items-center gap-10">
        <div className=" text-primary text-28 mb-10">멀티 플레이</div>
        <SelectButton
          height="72px"
          text="+ 멀티플레이 생성"
          width="420px"
          onClickEvent={() => {}}
        />
      </div>
      <div>
        <div className="text-primary text-28 my-10">멀티 플레이 참여</div>
        <Input
          height="40px"
          onClickEvent={() => {}}
          placeholder="참여 코드를 입력해주세요."
          setValue={() => {}}
          width="436px"
        />
      </div>
    </div>
  );
};

export default MultiplayModal;
