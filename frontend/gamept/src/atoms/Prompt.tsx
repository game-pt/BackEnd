import { IPropmpt } from '@/types/components/Prompt.types';
import LoadingSpinner1 from './LoadingSpinner1';

/**
 * props => data (프롬프트 출력 데이터), type (인게임, 엔딩 여부)
 * 현재 더미 데이터로 출력 중 / props.data 를 출력해줘야함
 */
const Prompt = (props: IPropmpt) => {
  if (props.type === undefined) return <LoadingSpinner1 />;
  const promptHeight =
    props.type === 'in-game' ? 'h-[330px]' : 'min-h-[330px] h-[60%]';
  return (
    <div
      className={`max-w-[1100px] min-w-[500px] mx-auto relative bg-transparent w-full + ${promptHeight}`}
    >
      {props.data && (
        <div className={props.type}>
          {props.data.map((e, i) => (
            e.map((v, j) => (
              <p key={`prompt_${i}_${j}`}>{v}</p>
            ))
          ))}
        </div>
      )}

      <div className="w-full h-1/6 absolute bg-gradient-to-t from-transparent from-30% to-backgroud"></div>
    </div>
  );
};

export default Prompt;
