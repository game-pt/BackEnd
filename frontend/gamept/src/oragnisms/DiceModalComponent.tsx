import React, { useEffect, useState } from "react";
import DiceComponent from "../atoms/DIceComponent";

const DiceModalComponent = () => {
  const [throwDice, setThrowDice] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      console.log('timeout!');
      setThrowDice(true)
    }, 3000);
  })

  return (
    <div className="card w-full h-96 bg-backgroud">
      <h1>주사위 결과</h1>
      <div className="diceCard relative flex justify-center mt-10">
        <DiceComponent throw={throwDice} />
      </div>
    </div>
  );
}

export default DiceModalComponent;