import { IGetPromptType } from "@/types/components/Prompt.types";

const getPromptTopic = (response: IGetPromptType) => {
  const origin = {};
  let prompt = '';

  return { origin, prompt };
}

export { getPromptTopic };