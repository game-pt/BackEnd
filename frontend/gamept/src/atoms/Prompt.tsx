import { IPropmpt } from '@/types/components/Prompt.types';
import LoadingSpinner1 from './LoadingSpinner1';
import { useEffect, useRef } from 'react';

const Prompt = (props: IPropmpt) => {
  const lastPromptRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (lastPromptRef.current) {
      lastPromptRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [props.isFetching]);

  if (props.type === undefined) return <LoadingSpinner1 />;
  const promptHeight =
    props.type === 'in-game' ? 'h-[330px]' : 'min-h-[330px] h-[60%]';
  return (
    <div
      className={`max-w-[1100px] min-w-[500px] mx-auto relative bg-transparent w-full + ${promptHeight}`}
    >
      {props.data && (
        <div className={props.type}>
          <p className="w-full h-8 bg-transparent"></p>
          {props.data.map((e, i) => (
            <div
              key={`prompt_${i}`}
              className={`my-4 ${e[0].mine && `flex`}`}
              ref={
                (props.data && props.data.length - 1 === i) && !props.isFetching
                  ? lastPromptRef
                  : null
              }
            >
              {e[0].mine && (
                <img
                  src="./src/assets/player_profile.png"
                  className="w-6 h-6 mt-2 mr-2"
                />
              )}
              {e.map((v, j) => (
                <p
                  className={v.mine ? `text-secondaryContainer` : ``}
                  key={`promptMsg_${i}_${j}`}
                >
                  {v.msg}
                </p>
              ))}
            </div>
          ))}
          {props.isFetching && (
            <div className="flex justify-center my-4" ref={lastPromptRef}>
              <LoadingSpinner1 />
            </div>
          )}
        </div>
      )}
      <div className="w-full h-1/6 absolute bg-gradient-to-t from-transparent from-30% to-backgroud"></div>
    </div>
  );
};

export default Prompt;
