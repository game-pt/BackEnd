import { IGetPromptType } from '@/types/components/Prompt.types';

const getPromptTopic = (response: IGetPromptType) => {
  //////////////////////////////////////
  // 빌드 에러 해결용 임시 코드
  response;
  /////////////////////////////////////////////
  const origin = {};
  let prompt = '';

  return { origin, prompt };
};

export { getPromptTopic };
