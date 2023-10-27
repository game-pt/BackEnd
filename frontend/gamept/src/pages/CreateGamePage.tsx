import CardImage from '../atoms/CardImage';
import ProfileImage from '@/atoms/ProfileImage';
import Logo from '@/atoms/Logo';

const CreateGamePage = () => {
  return (
    <>
      <CardImage
        url="../assets/Leonardo_Diffusion_XL_a_painting_of_one_adventurer_facing_the_1.jpg"
        alt=""
      />
      <ProfileImage size={160} imgCode={131} alt="" />
      <Logo />
    </>
  );
};

export default CreateGamePage;
