import {
  ApiMode,
  CreatePsychoProfileRequest,
  Psycho,
} from 'apps/sit-frontend/src/app/api/psycho/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';

export const psychoProfileApi = gatewayApi.injectEndpoints({
  endpoints: (build) => ({
    createOrUpdatePsycho: build.mutation<
      Psycho,
      CreatePsychoProfileRequest & {
        readonly id: number;
      }
    >({
      query: ({ id, ...body }) => ({
        url: `profiles/${id}`,
        method: 'PATCH',
        body,
      }),
    }),

    getMyProfile: build.query<Psycho, void>({
      query: () => ({
        url: `profiles/my`,
      }),
    }),

    getPsycho: build.query<
      Psycho,
      {
        id: number;
        mode: ApiMode;
      }
    >({
      query: ({ id, mode }) => ({
        url: `profiles/${id}?mode=${mode}`,
      }),
    }),
  }),
});
