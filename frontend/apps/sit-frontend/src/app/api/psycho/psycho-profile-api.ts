import {
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
        url: `/v1/profiles/${id}`,
        method: 'PATCH',
        body,
      }),
    }),

    getPsycho: build.query<Psycho, number>({
      query: (id) => ({
        url: `/v1/profiles/${id}?mode=PSYCHO`,
      }),
    }),
  }),
});
