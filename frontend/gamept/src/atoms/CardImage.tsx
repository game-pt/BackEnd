/* 
* 방 생성 프로세스 카드에 사용되는 이미지 컴포넌트
* 기본 이미지에 radial 방향으로 gradient를 깔아준 형태 (가장자리로 갈수록 어두움)
* props : {
  url : asset에 있는 이미지 url
  alt : alt 텍스트
}
*/

import { ICardImage } from '@/types/components/Image.types';

const CardImage = (props: ICardImage) => {
  // const imgUrl = new URL(props.url, import.meta.url).href;
  const imgUrl = props.url;

  return (
    <div className="relative w-full h-[315px]">
      <img
        src={imgUrl}
        className="absolute rounded-t-[10px]  w-full h-full object-cover"
        alt={props.alt}
      />
      <div className="absolute bg-gradient-radial from-black/0  to-black/30 rounded-[10px] w-full h-full"></div>
    </div>
  );
};

export default CardImage;
