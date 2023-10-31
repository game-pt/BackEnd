import { useEffect, useState } from 'react';
import Dice from '../atoms/Dice';

const DiceModal = () => {
  const [throwDice, setThrowDice] = useState(false);
  const [dice, setDice] = useState({
    dice1: 3,
    dice2: 1,
    dice3: 1,
  });

  useEffect(() => {
    setTimeout(() => {
      console.log('주사위 결과 값이 나온다면');
      setDice({
        dice1: Math.floor(Math.random() * 6) + 1,
        dice2: Math.floor(Math.random() * 6) + 1,
        dice3: Math.floor(Math.random() * 6) + 1,
      });
      setThrowDice(true);
    }, 3000);
  }, []);

  return (
    <div className="card w-2/3 h-full bg-backgroud rounded-lg bg-opacity-80 border-primary border-4">
      <p className="font-hol text-32 text-primary">주사위 결과</p>
      <div className="diceCard relative flex justify-center mt-20">
        <Dice
          throw={throwDice}
          dice1={dice.dice1}
          dice2={dice.dice2}
          dice3={dice.dice3}
        />
      </div>
      <p className="font-hol text-24 text-primary">
        선택을 성공할 확률이 높아졌습니다.
      </p>
      <button className="mt-4">버튼 컴포넌트</button>
    </div>
  );
};

export default DiceModal;
