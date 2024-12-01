import {psychoProfileApi} from "../api/psycho/psycho-profile-api";

export const useGetPsychoProfile = () => {
  const {data} = psychoProfileApi.useGetMyProfileQuery();
  return {
    id: data?.id,
    data
  }
}
