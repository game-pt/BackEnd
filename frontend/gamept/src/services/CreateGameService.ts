import axios from 'axios';

const api = import.meta.env.VITE_SERVER_URL;

export const fetchGetStories = async () => {
  const url = api + 'game/story';
  try {
    const response = await axios.get(url);
    return response.data;
  } catch {
    console.log('스토리 요청 실패');
  }
};

export const fetchPostGame = async (storyCode: string) => {
  const url = api + 'game';
  try {
    const response = await axios.post(url, { storyCode });
    return response.data;
  } catch {
    console.log('방 생성 실패');
  }
};
