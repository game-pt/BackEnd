export const getImgCode = (
  gender: number | undefined,
  raceCode: string,
  jobCode?: string
) => {
  console.log('코드확인', gender, raceCode, jobCode);
  let imgCode = 'profile' + gender ?? 0;
  imgCode += raceCode.substring(6);
  console.log('종족추가', imgCode);
  if (jobCode) {
    imgCode += jobCode.substring(5);
  }
  console.log('직업추가', imgCode);
  imgCode += '.svg';
  return imgCode;
};
