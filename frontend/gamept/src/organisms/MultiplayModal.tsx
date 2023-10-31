import IngameButton from '@/atoms/IngameButton';

const MultiplayModal = () => {
  return (
    <div className="font-hol text-secondaryContainer">
      <div className="text-primary text-28">멀티 플레이</div>
      <IngameButton
        type="solo"
        height="72px"
        text="+ 멀티플레이 생성"
        width="420px"
        onClickEvent={() => {}}
      />
      <div className="text-primary text-28">멀티 플레이 참여</div>
    </div>
  );
};

export default MultiplayModal;
