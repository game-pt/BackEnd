import Logo from '@/atoms/Logo';
import SelectGameMode from '@/organisms/SelectGameMode';
import SelectGameStory from '@/organisms/SelectGameStory';

const CreateGamePage = () => {
  return (
    <div className="w-full h-full">
      <Logo />
      {false && <SelectGameMode />}
      <SelectGameStory />
      {/* <LoadGameModal /> */}
      {/* <CardImage
        url="../assets/Leonardo_Diffusion_XL_a_painting_of_one_adventurer_facing_the_1.jpg"
        alt=""
      /> */}
      {/* <MakeCharacterStatContainer
        baseStats={[10, 10, 10, 10, 10]}
        correctionStats={[0, -1, 0, 1, 10]}
      /> */}
      {/* <SelectCharacter
        type={1}
        typeName="엘프"
        characterCode={131}
        baseStats={[10, 10, 10, 10, 10]}
        correctionStats={[0, 0, 0, 0, 0]}
        onClickEvent={() => {}}
      /> */}
    </div>
  );
};

export default CreateGamePage;
