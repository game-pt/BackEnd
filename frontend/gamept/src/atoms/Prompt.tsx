import { IPropmpt } from '@/types/components/Prompt.types';

/**
 * props => data (프롬프트 출력 데이터), type (인게임, 엔딩 여부)
 * 현재 더미 데이터로 출력 중 / props.data 를 출력해줘야함
 * 프롬프트 받아오는 서비스 로직 선에서 br 태그를 추가한 문자열을 반환해야할 듯 => 보안 측면에서 악질적인 코드
 * 데이터를 문장마다 배열로 받는 식으로 해서 map 돌려서 p 태그로 문장마다 구분하면 될듯
 */
const Prompt = (props: IPropmpt) => {
  return (
    <div className="max-w-[1000px] min-w-[700px] h-[350px] mx-auto relative bg-transparent">
      {props.type && (
        <div className={props.type}>
          <p>
            의 내부규율과 사무처리에 관한 규칙을 제정할 수 있다. 법률이 헌법에
            위반되는 여부가 재판의 전제가 된 경우에는 법원은 헌법재판소에
            제청하여 그 심판에 의하여 재판한다.
          </p>
          <p>
            이우석은 촌장의 부탁을 받아 숲속에서 사라진 아이들을 찾아 이른 아침
            숲 속으로 발걸음을 옮기던 중 키가 큰 고블린을 만났다.
          </p>
          <p>당신은 어떻게 할 것인가?</p>
          <p>나 : 싸운다.</p>
          <p>당신은 어떻게 할 것인가?</p>
          <p>나 : 싸운다.</p>
          <p>당신은 어떻게 할 것인가?</p>
          <p>나 : 싸운다.</p>
          <p>당신은 어떻게 할 것인가?</p>
          <p>나 : 싸운다.</p>
        </div>
      )}

      <div className="w-full h-1/6 absolute bg-gradient-to-t from-transparent from-30% to-backgroud"></div>
    </div>
  );
};

export default Prompt;
