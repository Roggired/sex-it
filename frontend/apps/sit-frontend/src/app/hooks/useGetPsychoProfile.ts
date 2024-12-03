import {psychoProfileApi} from "../api/psycho/psycho-profile-api";
import {useGetNumberPathParam} from "./useGetNumberPathParam";

export const useGetPsychoProfile = () => {
  const {data} = psychoProfileApi.useGetMyProfileQuery();
  const psychoId = useGetNumberPathParam('psychoId')

  return {
    id: data?.id ?? psychoId,
    isPsycho: !!data?.id,
    data
  }
}
