import { useParams } from 'react-router-dom';

export const useGetNumberPathParam = (
  paramName: string
): number | undefined => {
  const params = useParams();
  const param = params[paramName];

  if (param && !isNaN(Number(param))) {
    return Number(param);
  } else {
    return undefined;
  }
};
