/**
 * MultyplayModal 구현
 * 로직은 아직 미구현입니다.
 * @param
 * @returns
 */

import SelectButton from '@/atoms/SelectButton';
import { IMultiPlayModal } from '@/types/components/MultiPlayModal.types';
import Input from '@/atoms/Input';
import { useRef, useEffect } from 'react';
const MultiplayModal = (props: IMultiPlayModal) => {
  const modalRef = useRef<HTMLDivElement>(null);

  const handleModalClose = (e: MouseEvent) => {
    if (modalRef.current && !modalRef.current.contains(e.target as Node)) {
      props.closeModal();
    }
  };

  // 이벤트리스너 관리
  useEffect(() => {
    document.addEventListener('mousedown', handleModalClose);
    return () => {
      document.addEventListener('mousedown', handleModalClose);
    };
  }, []);

  return (
    <div className="w-full h-full absolute bg-black/30 top-0 left-0">
      <div
        ref={modalRef}
        className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 font-hol w-[590px] border-[3px] pt-10 border-primary h-[470px] rounded-[10px] bg-backgroundDeep/80"
      >
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
    </div>
  );
};

export default MultiplayModal;
