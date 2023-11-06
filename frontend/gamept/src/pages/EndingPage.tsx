/**
 * 엔딩 페이지
 * 배경 이미지 작업은 백과 협의가 필요
 * prompt 높이를 endingpage에 맞게 확대
 * @params props
 * @returns
 */

import Prompt from '@/atoms/Prompt';
import Logo from '@/atoms/Logo';
import SelectButton from '@/atoms/SelectButton';
import { useNavigate } from 'react-router-dom';

const EndingPage = () => {
  const navigate = useNavigate();
  return (
    <div className="font-hol relative w-screen h-screen mx-auto bg-backgroundDeep">
      <div className="text-primary flex flex-col items-center w-full h-full gap-10 pt-10">
        <Logo className="relative mx-auto" />
        <Prompt data={null} type="ending" />
        <SelectButton
          height="70px"
          onClickEvent={() => {
            navigate('/createGame');
          }}
          text="메인으로"
          width="305px"
          isShadow
        />
      </div>
    </div>
  );
};

export default EndingPage;
