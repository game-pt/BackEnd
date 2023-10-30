import Input from '@/atoms/Input';
import Prompt from '@/atoms/Prompt';
import { useState } from 'react';

const PromptInterface = () => {
  const [text, setText] = useState('');
  // const testName = '이우석';

  return (
    <div className="relative max-w-[1100px] min-w-[720px] h-[660px] mx-auto border-primary border-4">
      {/* 이 곳에 AI를 통해 생성한 배경 이미지를 백그라운드로 삽입 예정 */}
      <div
        className="absolute inset-0 bg-[url(./assets/interface/PromptInterface.png)] bg-no-repeat bg-auto pointer-events-none"
        style={{ zIndex: 21 }}
      ></div>

      <Prompt type="in-game" data={null} />
      <div className="w-full h-[255px]"></div>
      <Input
        width={'1072px'}
        height={'40px'}
        placeholder={`프롬프트를 입력해주세요.`}
        setValue={(e: string) => setText(e)}
        onClickEvent={() => console.log(text, 'Click!')}
      />
    </div>
  );
};

export default PromptInterface;
