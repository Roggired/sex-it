import {
  ApiMode,
  CreatePsychoProfileRequest, GetPsychoListParams,
  Psycho, PsychoCatalogueView,
} from 'apps/sit-frontend/src/app/api/psycho/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';
import { PageView } from "../common";

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

    getPsychoList: build.query<PageView<PsychoCatalogueView>, GetPsychoListParams>({
      query: (params) => ({
        url: 'profiles/filtered',
        method: 'POST',
        body: params.body,
        params: {
          pageNumber: params.pageNumber,
          pageSize: params.pageSize,
        }
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
