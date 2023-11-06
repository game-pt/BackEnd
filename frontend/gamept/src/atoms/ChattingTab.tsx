import { useState } from 'react';
import Input from './Input';
import { IChattingTab } from '@/types/components/SideInterface.types';

const ChattingTab = (props: IChattingTab) => {
  const [text, setText] = useState('');

  const sendChatting = () => {
    if (props.sendChat) props.sendChat(text);
    setText('');
  };

  return (
    <div className="w-full h-full flex flex-col p-2 bg-transparent text-28 justify-between">
      <div className="overflow-y-scroll w-full h-[328px]">
        {props.chat &&
          props.chat.map((e, i) => (
            <div key={`chat_row_${i}`} className="flex mx-2 text-16">
              <p>{e}</p>
            </div>
          ))}
      </div>
      <div>
        <Input
          width="334px"
          height="auto"
          placeholder="채팅을 입력해주세요."
          value={text}
          setValue={(e: string) => setText(e)}
          onClickEvent={() => sendChatting()}
        />
      </div>
    </div>
  );
};

export default ChattingTab;
