import {
  IMakeCharacterStat,
  IStatText,
} from '@/types/components/MakeCharacterStat.type';

const StatText = (props: IStatText) => {
  return (
    <div className="flex flex-row items-center text-white text-18">
      <div className="w-10 text-left  grow-0">{props.statType}</div>
      <div className="grow-0">: {props.baseStat}</div>
      <div className="text-right text-14 grow text-primary self-auto">
        {itoa(props.correctionStat)}
      </div>
    </div>
  );
};

const MakeCharacterStatContainer = (props: IMakeCharacterStat) => {
  return (
    <div className="bg-[url(./assets/MakeCharacterStatPanel.svg)] grid grid-cols-2 gap-y-2 w-[250px] h-[155px] p-5">
      {props.baseStats.map((stat, idx) => (
        <StatText
          statType={statType[idx]}
          baseStat={stat}
          correctionStat={props.correctionStats && props.correctionStats[idx]}
          key={idx}
        />
      ))}
    </div>
  );
};

const statType = ['힘', '민첩', '지능', '행운', '매력'];

const itoa = (input: number): string => {
  console.log(input);
  if (input > 0) {
    return `(+${input})`;
  } else if (input < 0) {
    return `(-${-input})`;
  }
  console.log(input);
  return '';
};

export default MakeCharacterStatContainer;
