import axios from 'axios';

const api = import.meta.env.VITE_SERVER_URL;

export const fetchGetStories = async () => {
  // 스토리 목록 받아오는 get 요청
  const url = api + '/game/story';
  try {
    const response = await axios.get(url);
    console.log('스토리', response);
    return response.data;
  } catch (err) {
    console.log(err, '스토리 요청 실패');
  }
};

export const fetchPostGame = async (storyCode: string) => {
  // 게임 생성하는 post 요청
  const url = api + '/game';
  console.log('쏘토리코뜨', storyCode);
  try {
    const response = await axios.post(url, { storyCode });
    return response.data;
  } catch (err) {
    console.log(err, '방 생성 실패');
  }
};
