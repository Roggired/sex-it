import {
  AcceptedApplication,
  Application,
  CreateApplicationRequest,
} from 'apps/sit-frontend/src/app/api/applications/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';

export const applicationsApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    createApplication: builder.mutation<object, CreateApplicationRequest>({
      query: (body) => ({
        url: 'v1/applications',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['CalSlots'],
    }),

    getApplications: builder.query<Array<Application>, number>({
      query: (psychoId) => ({
        url: 'v1/applications',
        params: {
          psychoId,
        },
      }),
      providesTags: ['PsychoApps'],
    }),

    getAcceptedApplications: builder.query<Array<AcceptedApplication>, string>({
      query: (params) => ({
        url: 'v1/applications/accepted',
      }),
    }),

    acceptApplication: builder.mutation<void, number>({
      query: (appId) => ({
        url: `v1/applications/${appId}/accept`,
        method: 'POST',
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots'],
    }),

    rejectApplication: builder.mutation<void, number>({
      query: (appId) => ({
        url: `v1/applications/${appId}/reject`,
        method: 'POST',
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots'],
    }),
  }),
});
